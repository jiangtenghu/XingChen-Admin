-- MySQL dump 10.13  Distrib 8.0.42, for Linux (aarch64)
--
-- Host: localhost    Database: admin_user
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_tenant`
--

DROP TABLE IF EXISTS `sys_tenant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tenant` (
  `id` bigint NOT NULL COMMENT 'ç§Ÿæˆ·ID',
  `name` varchar(50) NOT NULL COMMENT 'ç§Ÿæˆ·åç§°',
  `code` varchar(50) NOT NULL COMMENT 'ç§Ÿæˆ·ç¼–ç ',
  `status` varchar(10) DEFAULT 'ON' COMMENT 'ç§Ÿæˆ·çŠ¶æ€ï¼ˆONå¯ç”¨ OFFç¦ç”¨ï¼‰',
  `contact_person` varchar(50) DEFAULT NULL COMMENT 'è”ç³»äºº',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT 'è”ç³»ç”µè¯',
  `contact_email` varchar(100) DEFAULT NULL COMMENT 'è”ç³»é‚®ç®±',
  `expire_time` datetime DEFAULT NULL COMMENT 'è¿‡æœŸæ—¶é—´',
  `package_id` bigint DEFAULT NULL COMMENT 'å¥—é¤ID',
  `create_by` varchar(64) DEFAULT '' COMMENT 'åˆ›å»ºè€…',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_by` varchar(64) DEFAULT '' COMMENT 'æ›´æ–°è€…',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT 'åˆ é™¤æ ‡å¿—ï¼ˆ0ä»£è¡¨å­˜åœ¨ 1ä»£è¡¨åˆ é™¤ï¼‰',
  `remark` varchar(500) DEFAULT NULL COMMENT 'å¤‡æ³¨',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ç§Ÿæˆ·ä¿¡æ¯è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tenant`
--

LOCK TABLES `sys_tenant` WRITE;
/*!40000 ALTER TABLE `sys_tenant` DISABLE KEYS */;
INSERT INTO `sys_tenant` VALUES (1,'默认租户','default','ON','管理员','13800138000','admin@example.com',NULL,NULL,'admin','2025-08-21 07:49:21','','2025-08-21 07:49:21',0,'系统默认租户'),(2,'测试租户','test','ON','测试用户','13800138001','test@example.com',NULL,NULL,'admin','2025-08-21 07:49:21','','2025-08-21 07:49:21',0,'测试租户'),(1958438491033563137,'新测试租户','newtest','ON','测试联系人','13800138002','newtest@example.com',NULL,NULL,'system','2025-08-21 15:58:17','system','2025-08-21 15:58:17',0,'新创建的测试租户'),(1958439616537292801,'硅心科技','gx001','ON',NULL,NULL,NULL,NULL,NULL,'system','2025-08-21 16:02:45','system','2025-08-21 16:02:45',0,'硅心科技');
/*!40000 ALTER TABLE `sys_tenant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nickname` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `real_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` int DEFAULT '0',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birthday` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dept_id` bigint DEFAULT NULL,
  `status` int DEFAULT '0',
  `last_login_time` datetime DEFAULT NULL,
  `last_login_ip` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` int DEFAULT '0',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','admin123','管理员','管理员',NULL,0,'admin@example.com',NULL,NULL,NULL,0,NULL,NULL,NULL,'2025-08-21 03:37:52',NULL,'2025-08-21 07:50:06',0,'管理员用户'),(2,'test','test123','æµ‹è¯•ç”¨æˆ·',NULL,NULL,0,'test@example.com',NULL,NULL,NULL,0,NULL,NULL,NULL,'2025-08-21 03:37:52',NULL,'2025-08-21 03:37:52',0,NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'admin_user'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-21  8:56:30
