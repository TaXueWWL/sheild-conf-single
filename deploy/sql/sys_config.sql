/*
Navicat MySQL Data Transfer

Source Server         : localhost-3306
Source Server Version : 50621
Source Host           : 127.0.0.1:3306
Source Database       : gyweixin

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2018-05-30 02:17:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `CONFIG_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONFIG_KEY` varchar(64) NOT NULL COMMENT '配置key',
  `CONFIG_VALUE` varchar(256) NOT NULL COMMENT '配置value',
  `CONFIG_DESC` varchar(256) DEFAULT NULL COMMENT '配置描述',
  `PROJECT_ID` int(11) DEFAULT NULL COMMENT '工程id',
  `PROJECT_NAME` varchar(255) NOT NULL DEFAULT 'common' COMMENT '工程名，公共配置为common',
  `MODULE_ID` int(11) DEFAULT NULL COMMENT '模块id',
  `MODULE_NAME` varchar(255) DEFAULT NULL,
  `CONFIG_SWITCH` int(1) DEFAULT '1' COMMENT '配置开关，默认为0-开启，1-关闭',
  `OPT_USER` varchar(32) DEFAULT 'administrator',
  `INSERT_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  `UPDATE_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`CONFIG_ID`),
  UNIQUE KEY `config-key` (`CONFIG_KEY`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
