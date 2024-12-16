package com.example.seckill_backend.Interceptor;

import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.util.JWT;
import com.example.seckill_backend.util.ThreadLocalUntil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
@Component  //交给spring容器管理
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        log.info("Token: " + token);

        try {
            // 校验token是否合法
            if (token == null) {
                // 构建统一的返回结果：Token 为空，验证失败
                Result result = Result.error("NOT_LOGIN");

                // 设置响应的 Content-Type 为 JSON 格式
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // 使用 Jackson ObjectMapper 将 Result 对象转化为 JSON 字符串
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(result);

                // 写出 JSON 响应
                response.getWriter().write(jsonResponse);

                return false;
            }

            // 解析 JWT
            Map<String, Object> claims = JWT.parseJWT(token);
            log.info("Claims: " + claims.toString());

            // 提取用户名和密码
            String username = (String) claims.get("username");
            String password = (String) claims.get("password");

            // 输出账号和密码（如果需要的话）
            log.info("Username: {}", username);
            log.info("Password: {}", password);

            // 将账号和密码添加到请求属性中，后续的代码可以获取
            request.setAttribute("username", username);
            request.setAttribute("password", password);

            // 你也可以将 claims 设置到 ThreadLocal 中以便后续使用
            ThreadLocalUntil.set(claims);

            return true;
        } catch (Exception e) {
            // Token 校验失败
            log.error("Token error: {}", e.getMessage());

            // 构建统一的返回结果：Token 验证失败
            Result result = Result.error("Token 验证失败");

            // 设置响应的 Content-Type 为 JSON 格式
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // 使用 Jackson ObjectMapper 将 Result 对象转化为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(result);

            // 写出 JSON 响应
            response.getWriter().write(jsonResponse);

            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}