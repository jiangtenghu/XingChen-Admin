-- MySQL dump 10.13  Distrib 8.0.42, for Linux (aarch64)
--
-- Host: localhost    Database: admin_system
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
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL COMMENT 'éƒ¨é—¨id',
  `parent_id` bigint DEFAULT '0' COMMENT 'çˆ¶éƒ¨é—¨id',
  `ancestors` varchar(50) DEFAULT '' COMMENT 'ç¥–çº§åˆ—è¡¨',
  `dept_name` varchar(30) DEFAULT '' COMMENT 'éƒ¨é—¨åç§°',
  `order_num` int DEFAULT '0' COMMENT 'æ˜¾ç¤ºé¡ºåº',
  `leader` varchar(20) DEFAULT NULL COMMENT 'è´Ÿè´£äºº',
  `phone` varchar(11) DEFAULT NULL COMMENT 'è”ç³»ç”µè¯',
  `email` varchar(50) DEFAULT NULL COMMENT 'é‚®ç®±',
  `status` char(1) DEFAULT '0' COMMENT 'éƒ¨é—¨çŠ¶æ€ï¼ˆ0æ­£å¸¸ 1åœç”¨ï¼‰',
  `create_by` varchar(64) DEFAULT '' COMMENT 'åˆ›å»ºè€…',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_by` varchar(64) DEFAULT '' COMMENT 'æ›´æ–°è€…',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT 'åˆ é™¤æ ‡å¿—ï¼ˆ0ä»£è¡¨å­˜åœ¨ 1ä»£è¡¨åˆ é™¤ï¼‰',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='éƒ¨é—¨è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (100,0,'0','æ€»å…¬å¸',0,'ç®¡ç†å‘˜','15888888888','admin@example.com','0','admin','2025-08-21 07:28:15','','2025-08-21 07:28:15',0),(101,100,'0,100','æ·±åœ³æ€»å…¬å¸',1,'ç®¡ç†å‘˜','15888888888','admin@example.com','0','admin','2025-08-21 07:28:15','','2025-08-21 07:28:15',0),(102,100,'0,100','é•¿æ²™åˆ†å…¬å¸',2,'ç®¡ç†å‘˜','15888888888','admin@example.com','0','admin','2025-08-21 07:28:15','','2025-08-21 07:28:15',0);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `id` bigint NOT NULL COMMENT 'å­—å…¸ç¼–ç ',
  `dict_sort` int DEFAULT '0' COMMENT 'å­—å…¸æŽ’åº',
  `dict_label` varchar(100) DEFAULT '' COMMENT 'å­—å…¸æ ‡ç­¾',
  `dict_value` varchar(100) DEFAULT '' COMMENT 'å­—å…¸é”®å€¼',
  `dict_type` varchar(100) DEFAULT '' COMMENT 'å­—å…¸ç±»åž‹',
  `css_class` varchar(100) DEFAULT NULL COMMENT 'æ ·å¼å±žæ€§ï¼ˆå…¶ä»–æ ·å¼æ‰©å±•ï¼‰',
  `list_class` varchar(100) DEFAULT NULL COMMENT 'è¡¨æ ¼å›žæ˜¾æ ·å¼',
  `is_default` char(1) DEFAULT 'N' COMMENT 'æ˜¯å¦é»˜è®¤ï¼ˆYæ˜¯ Nå¦ï¼‰',
  `status` char(1) DEFAULT '0' COMMENT 'çŠ¶æ€ï¼ˆ0æ­£å¸¸ 1åœç”¨ï¼‰',
  `create_by` varchar(64) DEFAULT '' COMMENT 'åˆ›å»ºè€…',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_by` varchar(64) DEFAULT '' COMMENT 'æ›´æ–°è€…',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT 'åˆ é™¤æ ‡å¿—ï¼ˆ0ä»£è¡¨å­˜åœ¨ 1ä»£è¡¨åˆ é™¤ï¼‰',
  `remark` varchar(500) DEFAULT NULL COMMENT 'å¤‡æ³¨',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='å­—å…¸æ•°æ®è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL COMMENT 'èœå•ID',
  `menu_name` varchar(50) NOT NULL COMMENT 'èœå•åç§°',
  `parent_id` bigint DEFAULT '0' COMMENT 'çˆ¶èœå•ID',
  `order_num` int DEFAULT '0' COMMENT 'æ˜¾ç¤ºé¡ºåº',
  `path` varchar(200) DEFAULT '' COMMENT 'è·¯ç”±åœ°å€',
  `component` varchar(255) DEFAULT NULL COMMENT 'ç»„ä»¶è·¯å¾„',
  `query` varchar(255) DEFAULT NULL COMMENT 'è·¯ç”±å‚æ•°',
  `is_frame` int DEFAULT '1' COMMENT 'æ˜¯å¦ä¸ºå¤–é“¾ï¼ˆ0æ˜¯ 1å¦ï¼‰',
  `is_cache` int DEFAULT '0' COMMENT 'æ˜¯å¦ç¼“å­˜ï¼ˆ0ç¼“å­˜ 1ä¸ç¼“å­˜ï¼‰',
  `menu_type` char(1) DEFAULT '' COMMENT 'èœå•ç±»åž‹ï¼ˆMç›®å½• Cèœå• FæŒ‰é’®ï¼‰',
  `visible` char(1) DEFAULT '0' COMMENT 'èœå•çŠ¶æ€ï¼ˆ0æ˜¾ç¤º 1éšè—ï¼‰',
  `status` char(1) DEFAULT '0' COMMENT 'èœå•çŠ¶æ€ï¼ˆ0æ­£å¸¸ 1åœç”¨ï¼‰',
  `perms` varchar(100) DEFAULT NULL COMMENT 'æƒé™æ ‡è¯†',
  `icon` varchar(100) DEFAULT '#' COMMENT 'èœå•å›¾æ ‡',
  `create_by` varchar(64) DEFAULT '' COMMENT 'åˆ›å»ºè€…',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_by` varchar(64) DEFAULT '' COMMENT 'æ›´æ–°è€…',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT 'åˆ é™¤æ ‡å¿—ï¼ˆ0ä»£è¡¨å­˜åœ¨ 1ä»£è¡¨åˆ é™¤ï¼‰',
  `remark` varchar(500) DEFAULT '' COMMENT 'å¤‡æ³¨',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='èœå•æƒé™è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系统管理',0,1,'system',NULL,'',1,0,'M','0','0','','system','admin','2025-08-21 07:49:48','','2025-08-21 07:49:48',0,'系统管理目录'),(2,'用户管理',1,1,'user','system/user/index','',1,0,'C','0','0','system:user:list','user','admin','2025-08-21 07:49:48','','2025-08-21 07:49:48',0,'用户管理菜单'),(3,'菜单管理',1,3,'menu','system/menu/index','',1,0,'C','0','0','system:menu:list','tree-table','admin','2025-08-21 07:49:48','','2025-08-21 07:49:48',0,'菜单管理菜单'),(4,'角色管理',1,2,'role','system/role/index','',1,0,'C','0','0','system:role:list','peoples','admin','2025-08-21 07:49:48','','2025-08-21 07:49:48',0,'角色管理菜单'),(5,'租户管理',0,2,'tenant',NULL,'',1,0,'M','0','0','','tenant','admin','2025-08-21 07:49:48','','2025-08-21 07:49:48',0,'租户管理目录'),(6,'租户列表',5,1,'tenant-list','tenant/tenant/index','',1,0,'C','0','0','tenant:tenant:list','list','admin','2025-08-21 07:49:48','','2025-08-21 07:49:48',0,'租户列表菜单');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL COMMENT 'è§’è‰²ID',
  `role_name` varchar(30) NOT NULL COMMENT 'è§’è‰²åç§°',
  `role_key` varchar(100) NOT NULL COMMENT 'è§’è‰²æƒé™å­—ç¬¦ä¸²',
  `sort` int DEFAULT '0' COMMENT 'æ˜¾ç¤ºé¡ºåº',
  `data_scope` char(1) DEFAULT '1' COMMENT 'æ•°æ®èŒƒå›´ï¼ˆ1ï¼šå…¨éƒ¨æ•°æ®æƒé™ 2ï¼šè‡ªå®šæ•°æ®æƒé™ 3ï¼šæœ¬éƒ¨é—¨æ•°æ®æƒé™ 4ï¼šæœ¬éƒ¨é—¨åŠä»¥ä¸‹æ•°æ®æƒé™ï¼‰',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT 'èœå•æ ‘é€‰æ‹©é¡¹æ˜¯å¦å…³è”æ˜¾ç¤º',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT 'éƒ¨é—¨æ ‘é€‰æ‹©é¡¹æ˜¯å¦å…³è”æ˜¾ç¤º',
  `status` char(1) DEFAULT '0' COMMENT 'è§’è‰²çŠ¶æ€ï¼ˆ0æ­£å¸¸ 1åœç”¨ï¼‰',
  `create_by` varchar(64) DEFAULT '' COMMENT 'åˆ›å»ºè€…',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `update_by` varchar(64) DEFAULT '' COMMENT 'æ›´æ–°è€…',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'æ›´æ–°æ—¶é—´',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT 'åˆ é™¤æ ‡å¿—ï¼ˆ0ä»£è¡¨å­˜åœ¨ 1ä»£è¡¨åˆ é™¤ï¼‰',
  `remark` varchar(500) DEFAULT NULL COMMENT 'å¤‡æ³¨',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='è§’è‰²ä¿¡æ¯è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'è¶…çº§ç®¡ç†å‘˜','super',1,'1',1,1,'0','admin','2025-08-21 07:28:15','','2025-08-21 07:28:15',0,'è¶…çº§ç®¡ç†å‘˜'),(2,'æ™®é€šè§’è‰²','common',2,'2',1,1,'0','admin','2025-08-21 07:28:15','','2025-08-21 07:28:15',0,'æ™®é€šè§’è‰²');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT 'è§’è‰²ID',
  `menu_id` bigint NOT NULL COMMENT 'èœå•ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='è§’è‰²å’Œèœå•å…³è”è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT 'ç”¨æˆ·ID',
  `role_id` bigint NOT NULL COMMENT 'è§’è‰²ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ç”¨æˆ·å’Œè§’è‰²å…³è”è¡¨';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'admin_system'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-21  8:56:31
