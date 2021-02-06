/*
 Navicat MySQL Data Transfer

 Source Server         :
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           :
 Source Database       :

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : utf-8

 Date: 02/06/2021 19:17:31 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `rabbit_application`
-- ----------------------------
DROP TABLE IF EXISTS `rabbit_application`;
CREATE TABLE `rabbit_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application` varchar(255) NOT NULL COMMENT '工程名字',
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT '所属工程';

-- ----------------------------
--  Records of `rabbit_application`
-- ----------------------------
BEGIN;
INSERT INTO `rabbit_application` VALUES ('1', 'mq', null);
COMMIT;

-- ----------------------------
--  Table structure for `rabbit_consumer`
-- ----------------------------
DROP TABLE IF EXISTS `rabbit_consumer`;
CREATE TABLE `rabbit_consumer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `prefetch_size` int(11) NOT NULL DEFAULT '1' COMMENT '每次拉取数量',
  `concurrent` int(11) NOT NULL DEFAULT '1' COMMENT '消费者数量',
  `acknowledge_mode` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否手动确认 0 手动确认 1 自动',
  `queue_id` bigint(20) NOT NULL COMMENT '队列',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT '消费者';

-- ----------------------------
--  Records of `rabbit_consumer`
-- ----------------------------
BEGIN;
INSERT INTO `rabbit_consumer` VALUES ('1', '1', '1', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `rabbit_declare`
-- ----------------------------
DROP TABLE IF EXISTS `rabbit_declare`;
CREATE TABLE `rabbit_declare` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `exchange_type` varchar(255) NOT NULL COMMENT '交换机类型 fan-out、topic、headers、system、direct',
  `durable` tinyint(1) NOT NULL COMMENT '是否持久化队列',
  `priority` tinyint(2) NOT NULL DEFAULT '100' COMMENT '设置队列最大优先级',
  `ttl` bigint(20) DEFAULT NULL COMMENT '延迟时间 毫秒',
  `exchange_durable` tinyint(1) NOT NULL COMMENT '交换机持久 默认 0 true 1 false ',
  `alternate_id` bigint(20) DEFAULT NULL COMMENT '备份交换机 queue_id',
  `dead_routing_key` varchar(255) DEFAULT NULL COMMENT '死信队列',
  `dead_exchange` varchar(255) DEFAULT NULL COMMENT '死信交换机',
  `auto_delete` tinyint(1) DEFAULT '1' COMMENT '是否自动删除 0 ture 1 false',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT '高级特性';

-- ----------------------------
--  Records of `rabbit_declare`
-- ----------------------------
BEGIN;
INSERT INTO `rabbit_declare` VALUES ('1', 'direct', '1', '100', '10000', '1', '1', 'dead_rk', 'dead_rk', '1');
COMMIT;

-- ----------------------------
--  Table structure for `rabbit_queue`
-- ----------------------------
DROP TABLE IF EXISTS `rabbit_queue`;
CREATE TABLE `rabbit_queue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `queue_name` varchar(255) NOT NULL COMMENT '队列名字',
  `routing_key` varchar(255) NOT NULL,
  `exchange` varchar(255) NOT NULL COMMENT '交换机',
  `app_id` bigint(20) NOT NULL COMMENT 'application_id',
  `consumer_id` bigint(20) NOT NULL COMMENT '消费表id',
  `declare_id` bigint(20) DEFAULT NULL COMMENT '高级特性',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT '队列基础信息';

-- ----------------------------
--  Records of `rabbit_queue`
-- ----------------------------
BEGIN;
INSERT INTO `rabbit_queue` VALUES ('1', 'test', 'test', 'test_ex', '1', '1', '1', 'test');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
