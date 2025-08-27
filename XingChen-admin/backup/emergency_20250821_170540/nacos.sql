-- MySQL dump 10.13  Distrib 8.0.42, for Linux (aarch64)
--
-- Host: localhost    Database: nacos
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
-- Table structure for table `config_info`
--

DROP TABLE IF EXISTS `config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'group_id',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8mb4_unicode_ci COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置描述',
  `c_use` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置用途',
  `effect` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置生效',
  `type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置类型',
  `c_schema` text COLLATE utf8mb4_unicode_ci COMMENT '配置模式',
  `encrypted_data_key` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='config_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info`
--

LOCK TABLES `config_info` WRITE;
/*!40000 ALTER TABLE `config_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_tag`
--

DROP TABLE IF EXISTS `config_info_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8mb4_unicode_ci COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='config_info_tag';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_tag`
--

LOCK TABLES `config_info_tag` WRITE;
/*!40000 ALTER TABLE `config_info_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'nacos'
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
