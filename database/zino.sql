-- MySQL dump 10.13  Distrib 5.5.32, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: zino
-- ------------------------------------------------------
-- Server version	5.5.32-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TABLE IF EXISTS `z_blog_collection`;
CREATE TABLE `z_blog_collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `name` varchar(80) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0',
  `type` tinyint(4) DEFAULT '0',
  `createTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `z_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `z_files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `filename` varchar(120) NOT NULL,
  `filepath` varchar(120) NOT NULL,
  `downloadCount` int(11) DEFAULT '0',
  `status` tinyint(4) DEFAULT '0',
  `type` tinyint(4) DEFAULT '0',
  `createTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `z_blog`
--

DROP TABLE IF EXISTS `z_blog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `z_blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `collection` int(11) DEFAULT '0',
  `title` varchar(120) NOT NULL,
  `text` mediumtext NOT NULL,
  `viewCount` int(11) DEFAULT '0',
  `shareCount` int(11) DEFAULT '0',
  `likeCount` int(11) DEFAULT '0',
  `status` tinyint(4) DEFAULT '0',
  `draft` tinyint(4) DEFAULT '0',
  `createTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `z_blog`
--

LOCK TABLES `z_blog` WRITE;
/*!40000 ALTER TABLE `z_blog` DISABLE KEYS */;
/*!40000 ALTER TABLE `z_blog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `z_category`
--

DROP TABLE IF EXISTS `z_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `z_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(120) NOT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `z_category`
--

LOCK TABLES `z_category` WRITE;
/*!40000 ALTER TABLE `z_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `z_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `z_obj_category`
--

DROP TABLE IF EXISTS `z_obj_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `z_obj_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `obj` int(11) NOT NULL,
  `category` int(11) NOT NULL,
  `type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `z_obj_category`
--

LOCK TABLES `z_obj_category` WRITE;
/*!40000 ALTER TABLE `z_obj_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `z_obj_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `z_obj_tag`
--

DROP TABLE IF EXISTS `z_obj_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `z_obj_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `obj` int(11) NOT NULL,
  `tag` int(11) NOT NULL,
  `type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `z_obj_tag`
--

LOCK TABLES `z_obj_tag` WRITE;
/*!40000 ALTER TABLE `z_obj_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `z_obj_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `z_tag`
--

DROP TABLE IF EXISTS `z_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `z_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(80) NOT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `z_tag`
--

LOCK TABLES `z_tag` WRITE;
/*!40000 ALTER TABLE `z_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `z_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `z_todo`
--

DROP TABLE IF EXISTS `z_todo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `z_todo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `title` varchar(120) DEFAULT NULL,
  `url` varchar(300) NOT NULL,
  `sort` int(4) DEFAULT '0',
  `description` varchar(300) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `z_todo`
--

LOCK TABLES `z_todo` WRITE;
/*!40000 ALTER TABLE `z_todo` DISABLE KEYS */;
INSERT INTO `z_todo` VALUES (1,1,'如何打开端口 - 查看主题 • Ubuntu中文论坛','http://forum.ubuntu.org.cn/viewtopic.php?f=171&t=377025',0,NULL,1,'2013-08-22 12:55:50'),(4,1,'生活中的超精准抛投 3【酷客春季】_在线视频观看_土豆网视频 生活中的','http://www.tudou.com/programs/view/UZ-eCBfFTaE/',0,NULL,1,'2013-08-23 09:57:31'),(5,1,'JavaScript 标准教程（alpha）','http://javascript.ruanyifeng.com/',0,NULL,1,'2013-08-26 01:09:53'),(6,1,'ssh','http://happycasts.net/episodes/62',0,NULL,1,'2013-08-26 09:51:52'),(7,1,'Burp Suite使用详解一 – 牛X阿德马','http://www.nxadmin.com/tools/689.html',0,NULL,1,'2013-08-26 10:37:51'),(8,1,'实拍德国獒犬与袋鼠打架 头被强按水中|袋鼠|德国|獒犬_新浪视频','http://video.sina.com.cn/p/news/s/v/2013-08-29/220462847353.html',0,NULL,1,'2013-08-30 02:36:04'),(11,1,'稍后阅读 - view it later','http://www.zinor.net/view/more#',0,NULL,0,'2013-09-03 09:58:00'),(12,1,'万万没想到 第05集：大舌头悟空—专辑：《万万没想到》—在线播放—优酷网，视频高清在线观看','http://v.youku.com/v_show/id_XNjA0Mjk0MzE2.html?f=19559278',0,NULL,1,'2013-09-03 16:13:10'),(13,1,'Git 参考手册','http://gitref.org/zh/index.html',0,NULL,0,'2013-09-04 00:03:01'),(14,1,'小米手机3小米电视发布会全程官方视频[2013.9.5]—在线播放—优酷网，视频高清在线观看','http://v.youku.com/v_show/id_XNjA2MjUzMzM2.html',0,NULL,0,'2013-09-09 09:54:32');
/*!40000 ALTER TABLE `z_todo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `z_user`
--

DROP TABLE IF EXISTS `z_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `z_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  `email` varchar(120) NOT NULL,
  `pwd` varchar(80) NOT NULL,
  `role` tinyint(4) DEFAULT NULL,
  `ident` varchar(40) DEFAULT NULL,
  `activeCode` varchar(40) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `z_user`
--

LOCK TABLES `z_user` WRITE;
/*!40000 ALTER TABLE `z_user` DISABLE KEYS */;
INSERT INTO `z_user` VALUES (1,'zino','smile.zino@gmail.com','12e42382da44fb13cbf1a6b7107738942bdd0d53',127,NULL,'pCGuMDzfOG0L5GTAPZ9oLOkknvHlI1CncRqJwmcr','2013-08-21 00:03:05');
/*!40000 ALTER TABLE `z_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


CREATE TABLE `z_notes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `title` varchar(120) NOT NULL,
  `text` mediumtext NOT NULL,
  `status` tinyint(4) DEFAULT '0',
  `createTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-09-10  0:29:12
