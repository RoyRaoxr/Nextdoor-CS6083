/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : nextdoor

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 17/12/2019 22:08:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `aid` int(10) NOT NULL,
  `uid` int(10) NOT NULL,
  `bid` int(10) NOT NULL,
  `timestamp` datetime NOT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`aid`),
  KEY `application_ibfk_1` (`bid`),
  KEY `application_ibfk_2` (`uid`),
  CONSTRAINT `application_ibfk_1` FOREIGN KEY (`bid`) REFERENCES `block` (`bid`),
  CONSTRAINT `application_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of application
-- ----------------------------
BEGIN;
INSERT INTO `application` VALUES (1, 7, 1, '2019-12-15 13:45:15', 0);
INSERT INTO `application` VALUES (2, 3, 1, '2019-12-15 13:55:37', 1);
INSERT INTO `application` VALUES (3, 10, 1, '2019-12-17 21:15:22', 1);
INSERT INTO `application` VALUES (4, 7, 3, '2019-12-17 21:16:56', 0);
INSERT INTO `application` VALUES (5, 10, 5, '2019-12-17 21:57:55', 1);
INSERT INTO `application` VALUES (6, 10, 4, '2019-12-17 21:59:40', 1);
INSERT INTO `application` VALUES (7, 3, 4, '2019-12-17 22:00:06', 1);
INSERT INTO `application` VALUES (8, 10, 4, '2019-12-17 22:00:38', 2);
INSERT INTO `application` VALUES (9, 10, 4, '2019-12-17 22:05:40', 2);
COMMIT;

-- ----------------------------
-- Table structure for applicationvote
-- ----------------------------
DROP TABLE IF EXISTS `applicationvote`;
CREATE TABLE `applicationvote` (
  `appid` int(10) NOT NULL AUTO_INCREMENT,
  `aid` int(10) NOT NULL,
  `voteid` int(10) NOT NULL,
  `timestamp` datetime NOT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`appid`),
  KEY `applicationvote_ibfk_1` (`aid`),
  KEY `applicationvote_ibfk_2` (`voteid`),
  CONSTRAINT `applicationvote_ibfk_1` FOREIGN KEY (`aid`) REFERENCES `application` (`aid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `applicationvote_ibfk_2` FOREIGN KEY (`voteid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of applicationvote
-- ----------------------------
BEGIN;
INSERT INTO `applicationvote` VALUES (17, 2, 1, '2019-12-15 13:57:04', 1);
INSERT INTO `applicationvote` VALUES (18, 2, 2, '2019-12-15 13:57:35', 1);
INSERT INTO `applicationvote` VALUES (19, 3, 1, '2019-12-17 21:15:33', 1);
INSERT INTO `applicationvote` VALUES (20, 3, 2, '2019-12-17 21:15:50', 1);
INSERT INTO `applicationvote` VALUES (21, 3, 7, '2019-12-17 21:16:12', 1);
INSERT INTO `applicationvote` VALUES (22, 7, 10, '2019-12-17 22:00:21', 1);
INSERT INTO `applicationvote` VALUES (23, 8, 3, '2019-12-17 22:04:59', 0);
INSERT INTO `applicationvote` VALUES (24, 9, 3, '2019-12-17 22:06:03', 0);
COMMIT;

-- ----------------------------
-- Table structure for block
-- ----------------------------
DROP TABLE IF EXISTS `block`;
CREATE TABLE `block` (
  `bid` int(11) NOT NULL,
  `nid` int(11) NOT NULL,
  `bname` varchar(50) NOT NULL,
  `sw_lng` float NOT NULL,
  `sw_lat` float NOT NULL,
  `ne_lng` float NOT NULL,
  `ne_lat` float NOT NULL,
  PRIMARY KEY (`bid`),
  UNIQUE KEY `bid` (`bid`),
  KEY `bid_2` (`bid`),
  KEY `block_ibfk_1` (`nid`),
  CONSTRAINT `block_ibfk_1` FOREIGN KEY (`nid`) REFERENCES `neighborhood` (`nid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of block
-- ----------------------------
BEGIN;
INSERT INTO `block` VALUES (1, 1, 'South Bay Ridge', -74.0398, 40.6177, -74.0292, 40.6227);
INSERT INTO `block` VALUES (2, 1, 'North Bay Ridage', -74.0366, 40.6297, -74.02, 40.6346);
INSERT INTO `block` VALUES (3, 2, 'Poor Park', -74.0241, 40.6405, -74.0084, 40.6427);
INSERT INTO `block` VALUES (4, 2, 'Center Sunset', -74.0103, 40.6458, -73.9959, 40.65);
INSERT INTO `block` VALUES (5, 2, 'Rich Park', -74.0186, 40.6438, -74.0023, 40.653);
COMMIT;

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `userid` int(10) NOT NULL,
  `friendid` int(10) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `nid` int(10) NOT NULL,
  PRIMARY KEY (`userid`,`friendid`),
  KEY `userid` (`userid`),
  KEY `friends_ibfk_2` (`friendid`),
  KEY `friends_ibfk_3` (`nid`),
  CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`uid`),
  CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`friendid`) REFERENCES `user` (`uid`),
  CONSTRAINT `friends_ibfk_3` FOREIGN KEY (`nid`) REFERENCES `neighborhood` (`nid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of friends
-- ----------------------------
BEGIN;
INSERT INTO `friends` VALUES (1, 7, 1, 1);
INSERT INTO `friends` VALUES (7, 2, 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for joinblock
-- ----------------------------
DROP TABLE IF EXISTS `joinblock`;
CREATE TABLE `joinblock` (
  `uid` int(10) NOT NULL,
  `bid` int(10) NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `joinblock_ibfk_1` (`bid`),
  CONSTRAINT `joinblock_ibfk_1` FOREIGN KEY (`bid`) REFERENCES `block` (`bid`),
  CONSTRAINT `joinblock_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of joinblock
-- ----------------------------
BEGIN;
INSERT INTO `joinblock` VALUES (1, 1, '2019-12-03 17:18:54');
INSERT INTO `joinblock` VALUES (2, 1, '2019-12-05 17:19:10');
INSERT INTO `joinblock` VALUES (3, 4, '2019-12-17 22:00:21');
COMMIT;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `mid` int(10) NOT NULL AUTO_INCREMENT,
  `tid` int(10) NOT NULL,
  `author` int(10) NOT NULL,
  `title` varchar(50) NOT NULL,
  `timestamp` datetime NOT NULL,
  `text` varchar(50) NOT NULL,
  `lat` decimal(10,8) DEFAULT NULL,
  `lng` decimal(10,8) DEFAULT NULL,
  `replyid` int(10) DEFAULT NULL,
  PRIMARY KEY (`mid`),
  KEY `message_ibfk_1` (`tid`),
  KEY `message_ibfk_2` (`author`),
  KEY `message_ibfk_3` (`replyid`),
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`tid`) REFERENCES `thread` (`tid`),
  CONSTRAINT `message_ibfk_2` FOREIGN KEY (`author`) REFERENCES `user` (`uid`),
  CONSTRAINT `message_ibfk_3` FOREIGN KEY (`replyid`) REFERENCES `message` (`mid`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of message
-- ----------------------------
BEGIN;
INSERT INTO `message` VALUES (2, 3, 1, 'found a wallet', '2019-11-01 14:00:00', 'I found a wallet', 1.00000000, 2.00000000, 3);
INSERT INTO `message` VALUES (3, 5, 2, 'Found a phone', '2019-11-02 13:00:00', 'I found aiphone', 0.00000000, 0.00000000, 2);
INSERT INTO `message` VALUES (4, 4, 2, 'Hang out', '2019-12-02 00:32:41', 'Movies', 0.00000000, 0.00000000, 3);
INSERT INTO `message` VALUES (5, 3, 1, 'Watch out', '2019-12-02 00:38:22', 'bicycle accident', 0.00000000, 0.00000000, 3);
INSERT INTO `message` VALUES (6, 7, 1, 'nmsl', '2019-12-10 17:03:14', 'eqweqweqweqweqweqweqeqwe', 0.00000000, 0.00000000, 3);
INSERT INTO `message` VALUES (7, 4, 1, '孙笑川', '2019-12-10 17:07:52', '12312312312312312', 0.00000000, 0.00000000, 3);
INSERT INTO `message` VALUES (8, 4, 1, 'TSbiss', '2019-12-10 17:21:46', 'TSbiss', 0.00000000, 0.00000000, 3);
INSERT INTO `message` VALUES (9, 4, 1, 'TSbiss', '2019-12-10 17:22:57', 'biss', 0.00000000, 0.00000000, 3);
INSERT INTO `message` VALUES (10, 11, 1, 'dddsdsdsd', '2019-12-10 17:23:29', 'adasdasdsadsadasdasdasdas', 0.00000000, 0.00000000, 3);
INSERT INTO `message` VALUES (11, 12, 1, 'ddd', '2019-12-14 00:16:48', 'ddddd', 0.00000000, 0.00000000, NULL);
INSERT INTO `message` VALUES (12, 13, 1, 'nmsl', '2019-12-14 00:17:40', 'sadsadasdasdasdasd', 0.00000000, 0.00000000, NULL);
INSERT INTO `message` VALUES (13, 4, 2, '宁好', '2019-12-14 00:50:50', '祝宁健康', 0.00000000, 0.00000000, 4);
INSERT INTO `message` VALUES (14, 4, 2, '宁好', '2019-12-14 00:51:12', '祝宁健康', 41.00000000, -74.00000000, 4);
INSERT INTO `message` VALUES (15, 4, 2, 'asdasdasd', '2019-12-14 00:52:28', 'asdasdasdasd', 0.00000000, 0.00000000, 4);
INSERT INTO `message` VALUES (16, 4, 2, 'asdasdasd', '2019-12-14 00:52:41', 'asdasdasdasd', 41.00000000, -74.00000000, 4);
INSERT INTO `message` VALUES (17, 4, 2, '孙哥', '2019-12-14 00:56:04', '妈耶', 0.00000000, 0.00000000, 4);
INSERT INTO `message` VALUES (18, 4, 2, '孙哥', '2019-12-14 00:56:13', '妈耶', 41.00000000, -74.00000000, 4);
INSERT INTO `message` VALUES (19, 4, 2, 'd', '2019-12-14 00:58:11', 'd', 40.72384540, -74.00000000, 4);
INSERT INTO `message` VALUES (20, 4, 2, 'd', '2019-12-14 01:02:29', 'd', 40.69860200, -73.95767063, 4);
INSERT INTO `message` VALUES (21, 4, 2, 'd', '2019-12-14 01:10:33', 'd', 40.71057427, -73.93810123, 4);
INSERT INTO `message` VALUES (22, 3, 1, 'adasdasd', '2019-12-14 01:12:03', 'asdadasdadasdasd', 0.00000000, 0.00000000, 2);
INSERT INTO `message` VALUES (23, 3, 1, 'adasdasd', '2019-12-14 01:12:13', 'asdadasdadasdasd', 40.70693076, -73.93604130, 2);
INSERT INTO `message` VALUES (24, 5, 2, 'asdasd', '2019-12-14 01:13:16', 'asdadasdasdasdad', 0.00000000, 0.00000000, 3);
INSERT INTO `message` VALUES (25, 5, 2, 'asdasd', '2019-12-14 01:13:22', 'asdadasdasdasdad', 40.72774812, -73.98616642, 3);
INSERT INTO `message` VALUES (26, 4, 1, '你再骂？', '2019-12-14 01:13:48', 'biss', 0.00000000, 0.00000000, 7);
INSERT INTO `message` VALUES (27, 4, 1, '你再骂？', '2019-12-14 01:13:54', 'biss', 40.68610689, -73.96179050, 7);
INSERT INTO `message` VALUES (28, 3, 1, 'eer', '2019-12-14 01:14:06', 'gdfgdfgdfg', 0.00000000, 0.00000000, 22);
INSERT INTO `message` VALUES (29, 3, 1, 'er', '2019-12-14 01:14:10', 'dfgdfdfgdfgdgf', 40.70875254, -73.96522373, 22);
INSERT INTO `message` VALUES (30, 11, 1, 'fs', '2019-12-14 01:14:20', 'asd', 0.00000000, 0.00000000, 10);
INSERT INTO `message` VALUES (31, 11, 1, 'dfgdfgdfdgdfg', '2019-12-14 01:14:26', 'asd', 40.73217097, -73.97277683, 10);
INSERT INTO `message` VALUES (32, 4, 2, 'asd', '2019-12-14 01:17:18', 'asd', 40.64173424, -74.00895793, 4);
INSERT INTO `message` VALUES (33, 14, 1, '郭德纲', '2019-12-14 19:27:56', ' sdadsadas', 0.00000000, 0.00000000, NULL);
INSERT INTO `message` VALUES (34, 15, 1, '于谦', '2019-12-14 19:29:43', '123123123', 0.00000000, 0.00000000, NULL);
INSERT INTO `message` VALUES (35, 16, 1, '岳云鹏', '2019-12-14 19:30:54', '123123123', 0.00000000, 0.00000000, NULL);
INSERT INTO `message` VALUES (36, 17, 1, '牛批', '2019-12-14 19:44:57', ' adasdasd', 0.00000000, 0.00000000, NULL);
INSERT INTO `message` VALUES (37, 18, 1, 'Hello world', '2019-12-17 21:02:49', 'dsrdkjtdjksrhlksdfhgsdfglkjsdfkghsdfg', 0.00000000, 0.00000000, NULL);
INSERT INTO `message` VALUES (38, 19, 1, 'neighbor', '2019-12-17 21:03:17', 'asdasdasdasdasdasdasd', 0.00000000, 0.00000000, NULL);
COMMIT;

-- ----------------------------
-- Table structure for messagestatus
-- ----------------------------
DROP TABLE IF EXISTS `messagestatus`;
CREATE TABLE `messagestatus` (
  `mid` int(10) NOT NULL,
  `uid` int(10) NOT NULL,
  `status` tinyint(1) NOT NULL,
  PRIMARY KEY (`mid`,`uid`),
  KEY `mid` (`mid`),
  KEY `messagestatus_ibfk_2` (`uid`),
  CONSTRAINT `messagestatus_ibfk_1` FOREIGN KEY (`mid`) REFERENCES `message` (`mid`),
  CONSTRAINT `messagestatus_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of messagestatus
-- ----------------------------
BEGIN;
INSERT INTO `messagestatus` VALUES (3, 1, 0);
INSERT INTO `messagestatus` VALUES (4, 1, 0);
COMMIT;

-- ----------------------------
-- Table structure for neighborhood
-- ----------------------------
DROP TABLE IF EXISTS `neighborhood`;
CREATE TABLE `neighborhood` (
  `nid` int(11) NOT NULL,
  `nname` varchar(50) NOT NULL,
  `sw_lng` float NOT NULL,
  `sw_lat` float NOT NULL,
  `ne_lng` float NOT NULL,
  `ne_lat` float NOT NULL,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `nid` (`nid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of neighborhood
-- ----------------------------
BEGIN;
INSERT INTO `neighborhood` VALUES (1, 'Bay Ridge', -74.0329, 40.6112, -74.027, 40.6382);
INSERT INTO `neighborhood` VALUES (2, 'Sunset Park', -74.0266, 40.6359, -73.9806, 40.6571);
INSERT INTO `neighborhood` VALUES (3, 'Boerum Hill', -73.9929, 40.6808, -73.9721, 40.6888);
INSERT INTO `neighborhood` VALUES (4, 'Downtown Brooklyn', -73.9905, 40.6895, -73.9753, 40.6999);
INSERT INTO `neighborhood` VALUES (5, 'East Village', -73.9917, 40.7222, -73.9715, 40.7317);
COMMIT;

-- ----------------------------
-- Table structure for neighbors
-- ----------------------------
DROP TABLE IF EXISTS `neighbors`;
CREATE TABLE `neighbors` (
  `userid` int(10) NOT NULL,
  `neighborid` int(10) NOT NULL,
  PRIMARY KEY (`userid`,`neighborid`),
  KEY `userid` (`userid`),
  KEY `neighbors_ibfk_2` (`neighborid`),
  CONSTRAINT `neighbors_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`uid`),
  CONSTRAINT `neighbors_ibfk_2` FOREIGN KEY (`neighborid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of neighbors
-- ----------------------------
BEGIN;
INSERT INTO `neighbors` VALUES (1, 2);
INSERT INTO `neighbors` VALUES (1, 3);
INSERT INTO `neighbors` VALUES (1, 7);
INSERT INTO `neighbors` VALUES (6, 1);
COMMIT;

-- ----------------------------
-- Table structure for thread
-- ----------------------------
DROP TABLE IF EXISTS `thread`;
CREATE TABLE `thread` (
  `tid` int(11) NOT NULL AUTO_INCREMENT,
  `subject` varchar(50) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`tid`),
  UNIQUE KEY `tid` (`tid`),
  KEY `tid_2` (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of thread
-- ----------------------------
BEGIN;
INSERT INTO `thread` VALUES (3, 'lost and found', 3);
INSERT INTO `thread` VALUES (4, 'hang out', 1);
INSERT INTO `thread` VALUES (5, 'skating', 2);
INSERT INTO `thread` VALUES (7, 'nmsl', 1);
INSERT INTO `thread` VALUES (8, '孙笑川', 0);
INSERT INTO `thread` VALUES (9, 'TSbiss', 3);
INSERT INTO `thread` VALUES (10, 'TSbiss', 3);
INSERT INTO `thread` VALUES (11, 'dddsdsdsd', 2);
INSERT INTO `thread` VALUES (12, 'ddd', 2);
INSERT INTO `thread` VALUES (13, 'nmsl', 1);
INSERT INTO `thread` VALUES (14, '郭德纲', 3);
INSERT INTO `thread` VALUES (15, '于谦', 2);
INSERT INTO `thread` VALUES (16, '岳云鹏', 1);
INSERT INTO `thread` VALUES (17, '牛批', 1);
INSERT INTO `thread` VALUES (18, 'Hello world', 2);
INSERT INTO `thread` VALUES (19, 'neighbor', 0);
COMMIT;

-- ----------------------------
-- Table structure for threadblock
-- ----------------------------
DROP TABLE IF EXISTS `threadblock`;
CREATE TABLE `threadblock` (
  `tid` int(10) NOT NULL,
  `bid` int(10) NOT NULL,
  PRIMARY KEY (`tid`,`bid`),
  KEY `tid` (`tid`),
  KEY `threadblock_ibfk_2` (`bid`),
  CONSTRAINT `threadblock_ibfk_1` FOREIGN KEY (`tid`) REFERENCES `thread` (`tid`),
  CONSTRAINT `threadblock_ibfk_2` FOREIGN KEY (`bid`) REFERENCES `block` (`bid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of threadblock
-- ----------------------------
BEGIN;
INSERT INTO `threadblock` VALUES (7, 1);
INSERT INTO `threadblock` VALUES (14, 1);
INSERT INTO `threadblock` VALUES (15, 1);
INSERT INTO `threadblock` VALUES (15, 2);
INSERT INTO `threadblock` VALUES (18, 1);
INSERT INTO `threadblock` VALUES (18, 2);
COMMIT;

-- ----------------------------
-- Table structure for threadparticipant
-- ----------------------------
DROP TABLE IF EXISTS `threadparticipant`;
CREATE TABLE `threadparticipant` (
  `tid` int(10) NOT NULL,
  `recid` int(10) NOT NULL,
  PRIMARY KEY (`tid`,`recid`),
  KEY `tid` (`tid`),
  KEY `threadparticipant_ibfk_2` (`recid`),
  CONSTRAINT `threadparticipant_ibfk_1` FOREIGN KEY (`tid`) REFERENCES `thread` (`tid`),
  CONSTRAINT `threadparticipant_ibfk_2` FOREIGN KEY (`recid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of threadparticipant
-- ----------------------------
BEGIN;
INSERT INTO `threadparticipant` VALUES (3, 1);
INSERT INTO `threadparticipant` VALUES (5, 1);
INSERT INTO `threadparticipant` VALUES (7, 3);
INSERT INTO `threadparticipant` VALUES (7, 6);
INSERT INTO `threadparticipant` VALUES (7, 7);
INSERT INTO `threadparticipant` VALUES (7, 8);
INSERT INTO `threadparticipant` VALUES (7, 10);
INSERT INTO `threadparticipant` VALUES (10, 1);
INSERT INTO `threadparticipant` VALUES (10, 2);
INSERT INTO `threadparticipant` VALUES (11, 1);
INSERT INTO `threadparticipant` VALUES (11, 2);
INSERT INTO `threadparticipant` VALUES (12, 1);
INSERT INTO `threadparticipant` VALUES (12, 2);
INSERT INTO `threadparticipant` VALUES (13, 6);
INSERT INTO `threadparticipant` VALUES (14, 1);
INSERT INTO `threadparticipant` VALUES (14, 2);
INSERT INTO `threadparticipant` VALUES (14, 8);
INSERT INTO `threadparticipant` VALUES (15, 1);
INSERT INTO `threadparticipant` VALUES (15, 2);
INSERT INTO `threadparticipant` VALUES (15, 8);
INSERT INTO `threadparticipant` VALUES (17, 6);
INSERT INTO `threadparticipant` VALUES (18, 1);
INSERT INTO `threadparticipant` VALUES (18, 2);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `ustreet` varchar(200) NOT NULL,
  `ucity` varchar(59) NOT NULL,
  `ustate` varchar(20) NOT NULL,
  `profile` varchar(200) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `notify` varchar(50) DEFAULT NULL,
  `lastlogouttime` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'jb123', 'e10adc3949ba59abbe56e057f20f883e', 'Jane', 'Bob', '345', 'Brook', 'NY', 'I am from Booklyn', NULL, NULL, '2019-12-17 21:15:36');
INSERT INTO `user` VALUES (2, 'tl456', 'e10adc3949ba59abbe56e057f20f883e', 'Tracy', 'Luo', '123 51st', 'Brook', 'NY', NULL, NULL, NULL, '2019-12-17 21:15:52');
INSERT INTO `user` VALUES (3, 'cr512', 'e10adc3949ba59abbe56e057f20f883e', 'Cream', 'Rod', '210 Lexinton St', 'New York', 'NY', NULL, NULL, NULL, '2019-12-17 22:05:12');
INSERT INTO `user` VALUES (4, 'jw418', '654321', 'joel', 'wang', 'times square', 'New york', 'NY', 'A student', NULL, NULL, NULL);
INSERT INTO `user` VALUES (5, 'mm123', '123456', 'Mark', 'Mazey', '59 4 ave', 'Brook', 'NY', 'I am an engineer', NULL, '9176550000', NULL);
INSERT INTO `user` VALUES (6, 'yw4002', '123', 'YIZHOU', 'WANG', 'jkjhkhjkhk', 'BROOKLYN', 'NY', 'cs student', NULL, 'yw4002@nyu.edu', NULL);
INSERT INTO `user` VALUES (7, 'ww123', 'e10adc3949ba59abbe56e057f20f883e', 'YIZHOU', 'WANG', 'NYU Tandon School of Engineering, 6 MetroTech Center', 'Brooklyn', 'NY', '', NULL, 'yw4002@nyu.edu', '2019-12-17 21:05:31');
INSERT INTO `user` VALUES (8, 'dd123', 'e10adc3949ba59abbe56e057f20f883e', 'dddd', 'dddd', 'd', 'Brooklyn', 'NY', '', NULL, 'yw4002@nyu.edu', NULL);
INSERT INTO `user` VALUES (9, 'tt123', '202cb962ac59075b964b07152d234b70', 'T', 'S', '450 46TH ST FL 1', 'BROOKLYN', 'NY', '', NULL, '', NULL);
INSERT INTO `user` VALUES (10, 'ew123', '202cb962ac59075b964b07152d234b70', 'Earl', 'Wayne', '59th ST Brooklyn', 'New York', 'NY', 'teacher', NULL, '', '2019-12-17 22:05:55');
COMMIT;

-- ----------------------------
-- View structure for userauth
-- ----------------------------
DROP VIEW IF EXISTS `userauth`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `userauth` AS select `user`.`uid` AS `uid`,`user`.`username` AS `username`,`user`.`password` AS `password`,`user`.`lastlogouttime` AS `lastlogouttime` from `user`;

-- ----------------------------
-- Procedure structure for can_join
-- ----------------------------
DROP PROCEDURE IF EXISTS `can_join`;
delimiter ;;
CREATE PROCEDURE `nextdoor`.`can_join`(IN id INT(10), IN blockid INT(10))
BEGIN
	BEGIN
		Declare count_person INT default 0;
        Declare count_pass INT default 0;
        Declare count_no INT default 0;
        Declare apply INT;
		select count(uid) into count_person from joinblock where bid = blockid;
        select aid into apply from application where uid=id and bid=blockid and status=0;
        select count(appid) into count_pass from applicationvote where aid=apply and status=1;
        select count(appid) into count_no from applicationvote where aid=apply and status=0;
        if count_person = 0 then
			call join_block(id,blockid);
            update application set status=1 where aid=apply;
            update application set status=2 where status=0 and uid=id;
		elseif count_person > 3 then
			if count_pass >= count_person then 
				call join_block(id,blockid);
                update application set status=1 where aid=apply;
                update application set status=2 where status=0 and uid=id;
			elseif count_no > count_person - 3 then
				update application set status=2 where aid=apply;
			end if;
		else
			if count_pass = count_person then
				call join_block(id,blockid);
                update application set status=1 where aid=apply;
                update application set status=2 where status=0 and uid=id;
			elseif count_no > 0 then
				update application set status=2 where aid=apply;
			end if;
		end if;
	END;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for delete_friend
-- ----------------------------
DROP PROCEDURE IF EXISTS `delete_friend`;
delimiter ;;
CREATE PROCEDURE `nextdoor`.`delete_friend`(IN id INT(10), IN fid INT(10))
BEGIN
 BEGIN
  Delete from friends where (userid = id and friendid=fid)or (userid = fid and friendid=id);
        Delete from threadparticipant where (recid=id or recid=fid) and tid in (select tid from thread where type=1);
 END;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for delete_neighbors
-- ----------------------------
DROP PROCEDURE IF EXISTS `delete_neighbors`;
delimiter ;;
CREATE PROCEDURE `nextdoor`.`delete_neighbors`(IN id INT(10), IN fid INT(10))
BEGIN
 BEGIN
  Delete from neighbors where neighborid = fid;
        Delete from threadparticipant where (recid=id or recid=fid) and tid in (select tid from thread where type=0);
 END;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for join_block
-- ----------------------------
DROP PROCEDURE IF EXISTS `join_block`;
delimiter ;;
CREATE PROCEDURE `nextdoor`.`join_block`(IN id INT(10), IN joinid INT( 10))
BEGIN
 BEGIN
  Declare thread INT(10);
  Declare done1 Boolean default false;
  Declare cur1 CURSOR FOR Select tid From threadblock Where bid = joinid;
        declare continue HANDLER for not found set done1 = true;
  Insert into joinblock Values(id, joinid, now());
  Open cur1;
        insert_loop:loop
   Fetch cur1 into thread;
            if done1 then
    leave insert_loop;
   end if;
   Insert into threadparticipant
   Values(thread,id);
  End loop;
  Close cur1;
 END;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for leave_block
-- ----------------------------
DROP PROCEDURE IF EXISTS `leave_block`;
delimiter ;;
CREATE PROCEDURE `nextdoor`.`leave_block`(IN id INT(10))
BEGIN
 Delete From neighbors
 Where userid = id;
 Delete From threadparticipant
 Where recid = id and tid not in (select tid from thread where type = 1);
 Delete From joinblock
 Where uid = id;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
