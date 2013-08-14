/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50611
Source Host           : localhost:3306
Source Database       : zino

Target Server Type    : MYSQL
Target Server Version : 50611
File Encoding         : 65001

Date: 2013-08-13 16:24:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `z_blog`
-- ----------------------------
DROP TABLE IF EXISTS `z_blog`;
CREATE TABLE `z_blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `title` varchar(120) DEFAULT NULL,
  `text` mediumtext,
  `view_count` int(11) DEFAULT NULL,
  `share_count` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of z_blog
-- ----------------------------

-- ----------------------------
-- Table structure for `z_category`
-- ----------------------------
DROP TABLE IF EXISTS `z_category`;
CREATE TABLE `z_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(120) NOT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of z_category
-- ----------------------------

-- ----------------------------
-- Table structure for `z_obj_category`
-- ----------------------------
DROP TABLE IF EXISTS `z_obj_category`;
CREATE TABLE `z_obj_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `obj` int(11) NOT NULL,
  `category` int(11) NOT NULL,
  `type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of z_obj_category
-- ----------------------------

-- ----------------------------
-- Table structure for `z_obj_tag`
-- ----------------------------
DROP TABLE IF EXISTS `z_obj_tag`;
CREATE TABLE `z_obj_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `obj` int(11) NOT NULL,
  `tag` int(11) NOT NULL,
  `type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of z_obj_tag
-- ----------------------------

-- ----------------------------
-- Table structure for `z_tag`
-- ----------------------------
DROP TABLE IF EXISTS `z_tag`;
CREATE TABLE `z_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(80) NOT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of z_tag
-- ----------------------------

-- ----------------------------
-- Table structure for `z_todo`
-- ----------------------------
DROP TABLE IF EXISTS `z_todo`;
CREATE TABLE `z_todo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `title` varchar(120) DEFAULT NULL,
  `url` varchar(300) NOT NULL,
  `order` tinyint(4) DEFAULT '0',
  `description` varchar(300) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of z_todo
-- ----------------------------

-- ----------------------------
-- Table structure for `z_user`
-- ----------------------------
DROP TABLE IF EXISTS `z_user`;
CREATE TABLE `z_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) DEFAULT NULL,
  `email` varchar(120) DEFAULT NULL,
  `pwd` varchar(80) DEFAULT NULL,
  `role` tinyint(4) DEFAULT NULL,
  `ident` varchar(20) DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of z_user
-- ----------------------------
