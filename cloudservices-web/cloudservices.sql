/*
Navicat MySQL Data Transfer

Source Server         : 172.21.12.109
Source Server Version : 50530
Source Host           : 172.21.12.109:3306
Source Database       : cloudservices

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2015-05-28 11:24:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for push_message
-- ----------------------------
DROP TABLE IF EXISTS `push_message`;
CREATE TABLE `push_message` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(23) DEFAULT NULL,
  `topic` varchar(512) NOT NULL,
  `messageId` int(11) NOT NULL DEFAULT '0',
  `type` int(6) NOT NULL DEFAULT '0' COMMENT 'TEXT = 1;\r\nHTTP = 2;\r\nACK  = 3;\r\nFILE = 4; \r\nSUB = 5;',
  `ack` tinyint(1) NOT NULL DEFAULT '0',
  `sub` tinyint(1) NOT NULL DEFAULT '0',
  `total` int(11) NOT NULL DEFAULT '1',
  `no` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL,
  `payload` varchar(1024) DEFAULT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `updateTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=404 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for push_user
-- ----------------------------
DROP TABLE IF EXISTS `push_user`;
CREATE TABLE `push_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(23) NOT NULL,
  `password` varchar(20) NOT NULL,
  `registerTime` bigint(20) NOT NULL,
  `updateTime` bigint(20) NOT NULL,
  `resource` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36023 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Procedure structure for pro_update_pushmessage
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_update_pushmessage`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `pro_update_pushmessage`(in uname VARCHAR(23), 
																				in toTopic VARCHAR(512),
																				in mId INT, 
																				in mType INT, 
																				in isAck BINARY(1), 
																				in isSub BINARY(1), 
																				in sTotal INT, 
																				in sNo INT, 
																				in isStatus BINARY(1), 
																				in ctTime BIGINT, 
																				in upTime BIGINT, 
																				in pload VARCHAR(1024))
BEGIN
	DECLARE msgId BIGINT;
	SELECT id INTO msgId FROM `push_message` pm WHERE pm.username = uname AND pm.messageId = mId AND pm.`status` = 0;
	SELECT msgId;
	IF msgId > 0
			THEN UPDATE `push_message` pm SET pm.updateTime = upTime, pm.`status` = isStatus WHERE pm.id = msgId;
	ELSE
			INSERT INTO `push_message` (`username`, `topic`, `messageId`, `type`, `ack`, `sub`, `total`, `no`,
																  `status`, `createTime`, `updateTime`, `payload`) 
			VALUES (uname, toTopic, mId, mType, isAck, isSub, sTotal, sNo, isStatus, ctTime, upTime, pload); 
	END IF;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for pro_update_pushuser
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_update_pushuser`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `pro_update_pushuser`(in uname VARCHAR(23), in pswd VARCHAR(20), in regTime BIGINT, in upTime long, in res VARCHAR(50))
BEGIN
	DECLARE cnt INT;
	SELECT COUNT(id) INTO cnt FROM `push_user` pu WHERE pu.username = uname;
	SELECT cnt;
	IF cnt > 0
			THEN UPDATE `push_user` pu SET pu.updateTime = upTime, pu.resource = res WHERE pu.username = uname;
	ELSE
			INSERT INTO `push_user` (`username`, `password`, `registerTime`, `updateTime`, `resource`) VALUES (uname, pswd, regTime, upTime, res); 
	END IF;
END
;;
DELIMITER ;
