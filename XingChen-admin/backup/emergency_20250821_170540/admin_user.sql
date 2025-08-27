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
  `id` bigint NOT NULL COMMENT '租户ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户名称',
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户编码',
  `status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT 'ON' COMMENT '租户状态（ON启用 OFF禁用）',
  `contact_person` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系邮箱',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `package_id` bigint DEFAULT NULL COMMENT '套餐ID',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` int DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ç§Ÿæˆ·ä¿¡æ¯è¡¨';
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
  `id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '密码',
  `nickname` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '用户昵称',
  `real_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '真实姓名',
  `avatar` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '头像地址',
  `gender` int DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '用户邮箱',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '手机号码',
  `birthday` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '生日',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `status` int DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '最后登录IP',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` int DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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

-- Dump completed on 2025-08-21  9:05:40
