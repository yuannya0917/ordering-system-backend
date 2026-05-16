/*
 Navicat Premium Dump SQL

 Source Server         : DBCCourse
 Source Server Type    : MySQL
 Source Server Version : 80404 (8.4.4)
 Source Host           : localhost:3306
 Source Schema         : software

 Target Server Type    : MySQL
 Target Server Version : 80404 (8.4.4)
 File Encoding         : 65001

 Date: 16/05/2026 22:44:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for administrator
-- ----------------------------
DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator`  (
  `userID` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `merchantName` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`userID`) USING BTREE,
  CONSTRAINT `userid` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of administrator
-- ----------------------------
INSERT INTO `administrator` VALUES ('root1', '管理员1');

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `CollectID` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `dishID` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `LinkUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `userID` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `CollectTime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`CollectID`) USING BTREE,
  INDEX `dishid2`(`dishID` ASC) USING BTREE,
  INDEX `userid3`(`userID` ASC) USING BTREE,
  CONSTRAINT `userid3` FOREIGN KEY (`userID`) REFERENCES `customer` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `dishid2` FOREIGN KEY (`dishID`) REFERENCES `dish` (`dishID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES ('coll01', 'dish1', '', 'user01', '2026-05-09 14:00:00');
INSERT INTO `collect` VALUES ('coll02', 'dish3', '', 'user02', '2026-05-09 14:10:00');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `CommentID` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `OrderID` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `userID` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `Content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `PublishTime` datetime NULL DEFAULT NULL,
  `likes` int NULL DEFAULT NULL,
  PRIMARY KEY (`CommentID`) USING BTREE,
  UNIQUE INDEX `commentid`(`CommentID` ASC) USING BTREE,
  INDEX `userID`(`userID` ASC) USING BTREE,
  INDEX `orderid`(`OrderID` ASC) USING BTREE,
  CONSTRAINT `userid5` FOREIGN KEY (`userID`) REFERENCES `customer` (`userID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `orderid` FOREIGN KEY (`OrderID`) REFERENCES `order` (`orderID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES ('cmt001', 'order01', 'user01', '味道非常好！', '2026-05-09 13:00:00', NULL);
INSERT INTO `comment` VALUES ('cmt002', 'order02', 'user02', '服务不错', '2026-05-09 13:30:00', NULL);

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `userID` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `securityQuestion` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `securityAnswer` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`userID`) USING BTREE,
  CONSTRAINT `userid2` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES ('user01', NULL, '我的生日', '20000101');
INSERT INTO `customer` VALUES ('user02', NULL, '最喜欢的食物', '火锅');

-- ----------------------------
-- Table structure for dish
-- ----------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish`  (
  `dishID` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `dishName` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `dishPrice` int NOT NULL,
  `dishIntroduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `menuID` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`dishID`) USING BTREE,
  UNIQUE INDEX `dishid`(`dishID` ASC) USING BTREE,
  INDEX `menuid`(`menuID` ASC) USING BTREE,
  CONSTRAINT `menuid` FOREIGN KEY (`menuID`) REFERENCES `menu` (`menuID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dish
-- ----------------------------
INSERT INTO `dish` VALUES ('dish1', '麻辣火锅', 68, '四川特色麻辣火锅', 'menu1');
INSERT INTO `dish` VALUES ('dish2', '番茄炒蛋', 18, '家常素菜', 'menu2');
INSERT INTO `dish` VALUES ('dish3', '红烧肉', 38, '肥而不腻', 'menu3');
INSERT INTO `dish` VALUES ('dish4', '鱼香肉丝', 28, '经典川菜', 'menu3');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `menuID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `menuName` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `createTime` datetime NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`menuID`) USING BTREE,
  UNIQUE INDEX `menu`(`menuID` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('menu1', '热销菜品', '2026-05-09 10:00:00', '必点');
INSERT INTO `menu` VALUES ('menu2', '素菜', '2026-05-09 10:00:00', '清淡');
INSERT INTO `menu` VALUES ('menu3', '荤菜', '2026-05-09 10:00:00', '招牌');
INSERT INTO `menu` VALUES ('menu4', '川菜', NULL, '麻辣鲜香');

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `orderID` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userID` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `orderPrice` int NOT NULL,
  `orderTime` datetime NULL DEFAULT NULL,
  `orderNote` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `orderStatus` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`orderID`, `orderPrice`) USING BTREE,
  UNIQUE INDEX `order`(`orderID` ASC) USING BTREE,
  INDEX `userid7`(`userID` ASC) USING BTREE,
  CONSTRAINT `userid7` FOREIGN KEY (`userID`) REFERENCES `customer` (`userID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES ('order01', NULL, 106, '2026-05-09 12:00:00', '不要辣', '1');
INSERT INTO `order` VALUES ('order02', 'user02', 38, '2026-05-09 12:30:00', '多放葱', '0');

-- ----------------------------
-- Table structure for orderdetail
-- ----------------------------
DROP TABLE IF EXISTS `orderdetail`;
CREATE TABLE `orderdetail`  (
  `orderID` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `dishID` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `dishNum` int NOT NULL,
  `dishPrice` int NULL DEFAULT NULL,
  PRIMARY KEY (`dishID`, `orderID`) USING BTREE,
  INDEX `orderid2`(`orderID` ASC) USING BTREE,
  CONSTRAINT `dishid` FOREIGN KEY (`dishID`) REFERENCES `dish` (`dishID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `orderid2` FOREIGN KEY (`orderID`) REFERENCES `order` (`orderID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orderdetail
-- ----------------------------
INSERT INTO `orderdetail` VALUES ('order01', 'dish1', 1, 68);
INSERT INTO `orderdetail` VALUES ('order02', 'dish2', 1, 106);
INSERT INTO `orderdetail` VALUES ('order02', 'dish3', 1, 38);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userID` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userPassword` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`userID`) USING BTREE,
  UNIQUE INDEX `不允许重复`(`userID` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('root1', 'root');
INSERT INTO `user` VALUES ('user01', '123456');
INSERT INTO `user` VALUES ('user02', '456789');

SET FOREIGN_KEY_CHECKS = 1;
