-- MySQL dump 10.13  Distrib 8.2.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: seckill_db
-- ------------------------------------------------------
-- Server version	8.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `flash_sale`
--

DROP TABLE IF EXISTS `flash_sale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flash_sale` (
  `flash_sale_id` int NOT NULL AUTO_INCREMENT COMMENT '秒杀活动ID，主键',
  `product_id` int NOT NULL COMMENT '商品ID',
  `start_time` datetime NOT NULL COMMENT '秒杀开始时间',
  `end_time` datetime NOT NULL COMMENT '秒杀结束时间',
  `flash_price` decimal(10,2) NOT NULL COMMENT '秒杀价格',
  `total_stock` int NOT NULL COMMENT '总库存数量',
  `sold_count` int DEFAULT '0' COMMENT '已售出数量',
  `status` int DEFAULT '1' COMMENT '活动状态（1：进行中，0：已结束）',
  PRIMARY KEY (`flash_sale_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='秒杀活动表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flash_sale`
--

LOCK TABLES `flash_sale` WRITE;
/*!40000 ALTER TABLE `flash_sale` DISABLE KEYS */;
/*!40000 ALTER TABLE `flash_sale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID，主键',
  `user_id` int NOT NULL COMMENT '用户ID',
  `flash_sale_id` int DEFAULT NULL COMMENT '秒杀活动ID',
  `product_id` int NOT NULL COMMENT '订单商品ID',
  `quantity` int NOT NULL COMMENT '订单商品数量',
  `total_price` decimal(10,2) DEFAULT NULL COMMENT '订单总价',
  `status` int DEFAULT NULL COMMENT '订单状态（1：待支付，2：已支付，3：已发货，4：已完成，5：已取消）',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,14,NULL,101,2,15999.98,1,'2024-12-16 15:39:15','2024-12-16 15:39:32'),(2,14,10,102,1,2499.00,2,'2024-12-16 15:39:15','2024-12-16 15:39:32'),(3,14,NULL,103,3,14997.00,3,'2024-12-16 15:39:15','2024-12-16 15:39:32'),(4,15,NULL,104,1,4999.00,4,'2024-12-16 15:39:15','2024-12-16 15:39:32'),(5,15,20,105,5,12495.00,5,'2024-12-16 15:39:15','2024-12-16 15:39:32');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT COMMENT '商品ID，主键',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int NOT NULL COMMENT '商品库存',
  `description` varchar(500) DEFAULT NULL COMMENT '商品描述',
  `status` int NOT NULL COMMENT '商品状态（1：上架，0：下架）',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'iPhone 15',7999.99,100,'Apple 最新款智能手机，搭载 A17 处理器，支持 5G 网络，超高性能与拍照效果',1),(2,'MacBook Pro 16',19999.00,50,'苹果最新款 16 英寸 MacBook Pro，搭载 M2 Pro 芯片，超高性能，适合专业创作',1),(3,'AirPods Pro 2',2499.00,200,'苹果第二代 AirPods Pro，主动噪声取消技术，支持空间音频',1),(4,'索尼电视 55吋',4999.00,30,'索尼 55 吋 4K 高清电视，支持 HDR 和 Dolby Vision，画质细腻',1),(5,'戴尔 XPS 13',8999.00,80,'戴尔 XPS 13 超轻薄笔记本，搭载 Intel i7 处理器，适合商务办公与日常使用',1);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int DEFAULT '1',
  `balance` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (14,'admin','202cb962ac59075b964b07152d234b70',NULL,NULL,'2024-12-16 15:01:32','2024-12-16 15:01:32',1,0.00),(15,'jyang','202cb962ac59075b964b07152d234b70',NULL,NULL,'2024-12-16 15:23:45','2024-12-16 15:23:45',1,0.00);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-16 17:53:24
