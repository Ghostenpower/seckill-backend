package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.FlashSaleMapper;
import com.example.seckill_backend.mapper.OrderMapper;
import com.example.seckill_backend.mapper.ProductMapper;
import com.example.seckill_backend.mapper.UserMapper;
import com.example.seckill_backend.model.*;
import com.example.seckill_backend.util.PagerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class FlashSaleService {
    @Autowired
    private FlashSaleMapper flashSaleMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;

    public FlashSale getFlashSaleById(FlashSale flashSale) {
        return flashSaleMapper.getFlashSaleById(flashSale);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result initializeFlashSale(FlashSale flashSale,User admin) {
        if(!userMapper.isAdmin(admin)){
            return Result.error("权限不足");
        }
        try {
            if (flashSale.getStart_time() > flashSale.getEnd_time()) {
                return Result.error("秒杀结束时间不能早于开始时间");
            }
            // 1. 更新数据库
            flashSaleMapper.initializeFlashSale(flashSale);
            // 2. 将数据写入 Redis
            Map<String, String> flashSaleData = new HashMap<>();
            String flashSaleKeyPrefix = "flashSale:" + flashSale.getFlash_sale_id() + ":";
            flashSaleData.put(flashSaleKeyPrefix + "start_time", flashSale.getStart_time().toString());
            flashSaleData.put(flashSaleKeyPrefix + "end_time", flashSale.getEnd_time().toString());
            flashSaleData.put(flashSaleKeyPrefix + "sold_count", "0");
            flashSaleData.put(flashSaleKeyPrefix + "status", "1");
            flashSaleData.put(flashSaleKeyPrefix + "product_id", flashSale.getProduct_id().toString());
            flashSaleData.put(flashSaleKeyPrefix + "flash_price", flashSale.getFlash_price().toString());
            flashSaleData.put(flashSaleKeyPrefix + "total_stock", flashSale.getTotal_stock().toString());

            // 使用 Redis 的事务机制确保一致性（multi/exec）
            redisTemplate.opsForValue().multiSet(flashSaleData);

            // 如果所有操作成功，返回成功结果
            return Result.success();

        } catch (Exception e) {
            // 捕获异常，进行补偿或回滚
            // 数据库操作会回滚，但 Redis 操作需要手动补偿
            String flashSaleKeyPrefix = "flashSale:" + flashSale.getFlash_sale_id() + ":";

            // 删除之前写入的 Redis 键值，保证 Redis 和数据库一致性
            redisTemplate.delete(flashSaleKeyPrefix + "start_time");
            redisTemplate.delete(flashSaleKeyPrefix + "end_time");
            redisTemplate.delete(flashSaleKeyPrefix + "sold_count");
            redisTemplate.delete(flashSaleKeyPrefix + "status");
            redisTemplate.delete(flashSaleKeyPrefix + "product_id");
            redisTemplate.delete(flashSaleKeyPrefix + "flash_price");
            redisTemplate.delete(flashSaleKeyPrefix + "total_stock");

            // 打印日志或记录错误信息
            log.error("Failed to initialize flash sale", e);

            // 可以选择重新抛出异常，或者返回错误结果
            return Result.error("Failed to initialize flash sale" + e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result initializeFlashSale1(FlashSale flashSale,User admin) {
        if(!userMapper.isAdmin(admin)){
            return Result.error("权限不足");
        }
        try {
            if (flashSale.getStart_time() > flashSale.getEnd_time()) {
                return Result.error("秒杀结束时间不能早于开始时间");
            }
            Integer product_id=flashSale.getProduct_id();
            Product product=new Product();
            product.setProduct_id(product_id);
            List<Product> searchList=productMapper.getProductById(product);
            if(searchList.isEmpty()){
                return Result.error("商品不存在");
            }

            flashSaleMapper.initializeFlashSale(flashSale);
            // 如果所有操作成功，返回成功结果
            return Result.success();

        } catch (Exception e) {
            // 打印日志或记录错误信息
            log.error("Failed to initialize flash sale", e);

            // 可以选择重新抛出异常，或者返回错误结果
            return Result.error("Failed to initialize flash sale" + e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result flashSale(FlashSale flashSale, User user) {
        int flash_sale_id = flashSale.getFlash_sale_id();
        String totalStockKey = "flashSale:" + flash_sale_id + ":total_stock";
        String soldCountKey = "flashSale:" + flash_sale_id + ":sold_count";
        String userKey = "flashSale:" + flash_sale_id + ":user" + user.getUser_id();

        try {
            List<String> keys = List.of(
                    totalStockKey,
                    "flashSale:" + flash_sale_id + ":status",
                    "flashSale:" + flash_sale_id + ":end_time",
                    "flashSale:" + flash_sale_id + ":start_time",
                    "flashSale:" + flash_sale_id + ":product_id",
                    "flashSale:" + flash_sale_id + ":flash_price"
            );

            List<String> values = redisTemplate.opsForValue().multiGet(keys);
            if (values == null || values.size() != keys.size() || values.contains(null)) {
                return Result.error("秒杀活动不存在");
            }

            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) == null) {
                    log.error("Redis key '{}' not found for flash sale ID: {}", keys.get(i), flash_sale_id);
                }
            }

            int total_stock = parseIntegerValue(values.get(0), "total_stock", flash_sale_id);
            int status = parseIntegerValue(values.get(1), "status", flash_sale_id);
            int end_time = parseIntegerValue(values.get(2), "end_time", flash_sale_id);
            int start_time = parseIntegerValue(values.get(3), "start_time", flash_sale_id);
            int product_id = parseIntegerValue(values.get(4), "product_id", flash_sale_id);
            BigDecimal flash_price = new BigDecimal(values.get(5));

            if (status != 1) {
                return Result.error("秒杀活动已结束");
            }
            if (start_time > LocalDateTime.now().atZone(ZoneOffset.UTC).toEpochSecond()-28800) {
                return Result.error("秒杀活动尚未开始");
            }
            if (end_time < LocalDateTime.now().atZone(ZoneOffset.UTC).toEpochSecond()-28800) {
                System.out.println(LocalDateTime.now().atZone(ZoneOffset.UTC).toEpochSecond());
                System.out.println(end_time);
                return Result.error("秒杀活动已结束2");
            }
            if (total_stock <= 0) {
                return Result.error("秒杀活动已售罄");
            }

            // 判断用户资格
            Boolean isParticipated = redisTemplate.hasKey(userKey);
            if (isParticipated != null && isParticipated) {
                return Result.error("您已参与过该秒杀活动");
            }

            synchronized (this) { // 确保同一秒杀活动在同一时间只能被一个线程处理
                // 在这里记录初始值
                int originalSoldCount = parseIntegerValue(redisTemplate.opsForValue().get(soldCountKey), "sold_count", flash_sale_id);

                try {
                    // 修改Redis数据
                    redisTemplate.opsForValue().decrement(totalStockKey);
                    redisTemplate.opsForValue().increment(soldCountKey);

                    // 创建订单
                    System.out.println(flash_sale_id);
                    orderMapper.createFlashOrder(user.getUser_id(), flash_sale_id, product_id, 1, flash_price);
                    flashSaleMapper.decreaseStock(flash_sale_id);

                    // 判断是否库存已空
                    int new_total_stock = parseIntegerValue(redisTemplate.opsForValue().get(totalStockKey), "total_stock", flash_sale_id);
                    if (new_total_stock <= 0) {
                        redisTemplate.opsForValue().set("flashSale:" + flash_sale_id + ":status", "0");
                        FlashSale updateFlashSale = new FlashSale();
                        updateFlashSale.setFlash_sale_id(flash_sale_id);
                        updateFlashSale.setStatus(0);
                        flashSaleMapper.updateFlashSale(updateFlashSale);
                    }
                    redisTemplate.opsForValue().set(userKey, "1");  // 记录用户参与信息
                } catch (Exception e) {
                    // 捕获异常时恢复Redis数据
                    redisTemplate.opsForValue().set(totalStockKey, String.valueOf(total_stock));
                    redisTemplate.opsForValue().set(soldCountKey, String.valueOf(originalSoldCount));
                    throw new RuntimeException("秒杀操作失败", e);
                }
            }

            return Result.success();
        } catch (Exception e) {
            log.error("Failed to process flash sale for user {} and flash sale ID {}: {}", user.getUser_id(), flash_sale_id, e.getMessage());
            return Result.error("秒杀活动处理失败: " + e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result flashSale1(FlashSale flashSale, User user) {
        try {
            FlashSale flashSale1 = flashSaleMapper.getFlashSaleById(flashSale);
            if (flashSale1 == null) {
                return Result.error("秒杀活动不存在");
            }
            if (flashSale1.getStatus() != 1) {
                return Result.error("秒杀活动已结束");
            }
            if (flashSale1.getStart_time() > LocalDateTime.now().atZone(ZoneOffset.UTC).toEpochSecond()-28800) {
                return Result.error("秒杀活动尚未开始");
            }
            if (flashSale1.getEnd_time() < LocalDateTime.now().atZone(ZoneOffset.UTC).toEpochSecond()-28800) {
                return Result.error("秒杀活动已结束");
            }
            if (flashSale1.getTotal_stock() <= 0) {
                return Result.error("秒杀活动已售罄");
            }

            if(flashSaleMapper.checkRecord(user.getUser_id(),flashSale1.getFlash_sale_id())==1){
                return Result.error("您已参与过该秒杀活动");
            }

            if(flashSale1.getTotal_stock()-1==flashSale1.getSold_count()){
                flashSale1.setStatus(0);
                flashSaleMapper.updateFlashSale(flashSale1);
            }
            flashSaleMapper.decreaseStock(flashSale1.getFlash_sale_id());
            orderMapper.createFlashOrder(user.getUser_id(), flashSale1.getFlash_sale_id(), flashSale1.getProduct_id(), 1, flashSale.getFlash_price());

            return Result.success();

        }catch (Exception e) {
            log.error("Failed to process flash sale for user {} and flash sale ID {}: {}", user.getUser_id(), flashSale.getFlash_sale_id(), e.getMessage());
            return Result.error("秒杀活动处理失败: " + e.getMessage());
        }
    }

    private int parseIntegerValue(String value, String keyName, int flash_sale_id) {
        return Optional.ofNullable(value).map(Integer::parseInt).orElseThrow(() -> {
            log.error("Invalid integer format for Redis key 'flashSale:{}:{}': {}", flash_sale_id, keyName, value);
            throw new RuntimeException("Invalid integer format for Redis key 'flashSale:" + flash_sale_id + ":" + keyName + "': " + value);
        });
    }

    public Result getFlashSaleLimit3() {
        return Result.success(flashSaleMapper.getFlashSaleLimit3());
    }

    public Result getFlashSaleProductLimit3() {
        return Result.success(flashSaleMapper.getFlashSaleProductLimit3());
    }

    // luaTest 方法
    public Result luaTest(String key) {
        if (key == null) {
            return Result.error("Key cannot be null");
        }

        try {
            // 将 Lua 脚本作为字符串硬编码
            String luaScript =
                    "if redis.call('EXISTS', KEYS[1]) == 1 then\n" +
                            "    return 'Exists'\n" +
                            "else\n" +
                            "    return 'Not Exists'\n" +
                            "end";

            // 将 Lua 脚本加载为 RedisScript
            RedisScript<String> script = RedisScript.of(luaScript, String.class);

            // 执行 Lua 脚本，传入键名作为参数
            List<String> keys = List.of(key); // 确保 key 不为 null
            System.out.println(keys.get(0));
            Object result = redisTemplate.execute(script, keys);

            // 根据执行结果返回相应的 Result
            return Result.success(result);
        } catch (Exception e) {
            // 错误处理
            log.error("Failed to execute Lua script: {}", e.getMessage(), e);
            return Result.error("Failed to process Lua script: " + e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result getByName(Page<Object> page, FlashSaleView flashSaleView) {
        page.setTotal(flashSaleMapper.getCountByName(flashSaleView.getProduct_name()));
        List<FlashSaleView> flashSaleViews = flashSaleMapper.getByName(flashSaleView.getProduct_name(), page.getPage_size(), PagerUtil.getOffset(page.getPage_num(), page.getPage_size()));
        page.setItems(flashSaleViews);
        return Result.success(page);
    }

    public Result getFlashSaleView(FlashSaleView flashSaleView, Page<Object> page) {
        page.setTotal(flashSaleMapper.getCount(flashSaleView));
        List<FlashSaleView> flashSaleViews = flashSaleMapper.getFlashSaleView( flashSaleView.getProduct_name(),page.getPage_size(), PagerUtil.getOffset(page.getPage_num(), page.getPage_size()));
        page.setItems(flashSaleViews);
        return Result.success(page);
    }

    public Result deleteFlashSale(FlashSale flashSale,User admin) {
        if(!userMapper.isAdmin(admin)){
            return Result.error("权限不足");
        }
        flashSaleMapper.deleteFlashSale(flashSale.getFlash_sale_id());
        return Result.success();
    }

}
