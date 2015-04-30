/*
Navicat MySQL Data Transfer

Source Server         : 172.21.12.109
Source Server Version : 50530
Source Host           : 172.21.12.109:3306
Source Database       : cloudservices

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2015-04-30 18:30:02
*/

SET FOREIGN_KEY_CHECKS=0;

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
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;

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
