/*
 Navicat Premium Data Transfer

 Source Server         : microBlog
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : microblog

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 07/05/2022 00:53:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for avatar
-- ----------------------------
DROP TABLE IF EXISTS `avatar`;
CREATE TABLE `avatar`  (
  `avatar_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`avatar_id`) USING BTREE,
  INDEX `fk_avatar_user`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of avatar
-- ----------------------------
INSERT INTO `avatar` VALUES (0, 0, '0.jpeg', '2022-03-03 22:07:04');
INSERT INTO `avatar` VALUES (100, 11, '0.jpeg', '2022-03-18 16:38:34');
INSERT INTO `avatar` VALUES (101, 12, '0.jpeg', '2022-03-18 16:49:02');
INSERT INTO `avatar` VALUES (102, 12, '1647593363055.jpg', '2022-03-18 16:49:23');
INSERT INTO `avatar` VALUES (103, 13, '0.jpeg', '2022-03-19 22:44:36');
INSERT INTO `avatar` VALUES (104, 11, '1647879782367.png', '2022-03-22 00:23:02');
INSERT INTO `avatar` VALUES (105, 11, '1647880323325.jpg', '2022-03-22 00:32:03');
INSERT INTO `avatar` VALUES (106, 11, '1651592624275.jpg', '2022-05-03 23:43:44');
INSERT INTO `avatar` VALUES (107, 11, '1651594570700.jpg', '2022-05-04 00:16:11');
INSERT INTO `avatar` VALUES (108, 11, '1651691759635.jpg', '2022-05-05 03:16:00');

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
  `blog_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` int NULL DEFAULT NULL COMMENT '-1:删除\r\n1:创建\r\n2:转发\r\n',
  `create_date` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`blog_id`) USING BTREE,
  INDEX `fk_blog_user`(`user_id`) USING BTREE,
  CONSTRAINT `fk_blog_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 87 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES (68, 11, '第一条微博😀😀😃<br/>这个天气是真的好呀', 1, '2022-03-18 16:39:57');
INSERT INTO `blog` VALUES (69, 11, '第二条微博😃😄哈哈哈', 1, '2022-03-18 16:40:46');
INSERT INTO `blog` VALUES (70, 11, '第三条微博', -1, '2022-03-18 16:40:59');
INSERT INTO `blog` VALUES (71, 11, '全球疫情早点结束', 1, '2022-03-18 16:43:08');
INSERT INTO `blog` VALUES (72, 11, '老坛酸菜牛肉面对方便面有多大影响', 1, '2022-03-18 16:43:53');
INSERT INTO `blog` VALUES (73, 11, '无症状感染者需要治疗吗', 1, '2022-03-18 16:44:23');
INSERT INTO `blog` VALUES (74, 11, '该怎么看待男足🙃？？？', -1, '2022-03-18 16:46:12');
INSERT INTO `blog` VALUES (75, 11, '74', -1, '2022-03-18 16:47:53');
INSERT INTO `blog` VALUES (76, 12, '如何看待只能家居呢', 1, '2022-03-18 16:49:58');
INSERT INTO `blog` VALUES (77, 12, '有什么书籍可以推荐的嘛', 1, '2022-03-18 16:50:23');
INSERT INTO `blog` VALUES (78, 11, '疫情', 1, '2022-03-21 22:35:16');
INSERT INTO `blog` VALUES (79, 11, '今天天气真好😀<br/>风景真美丽', 1, '2022-03-22 01:13:10');
INSERT INTO `blog` VALUES (80, 11, '微博test1', 1, '2022-03-22 02:30:17');
INSERT INTO `blog` VALUES (81, 11, '微博test2', 1, '2022-03-22 02:30:23');
INSERT INTO `blog` VALUES (82, 11, '微博test3', 1, '2022-03-22 02:30:29');
INSERT INTO `blog` VALUES (83, 11, 'test微博', 1, '2022-03-22 02:30:38');
INSERT INTO `blog` VALUES (84, 12, '83', -1, '2022-05-01 18:28:37');
INSERT INTO `blog` VALUES (85, 11, '', -1, '2022-05-02 01:44:10');
INSERT INTO `blog` VALUES (86, 12, '68', 2, '2022-05-05 13:30:57');
INSERT INTO `blog` VALUES (87, 11, '68', 2, '2022-05-06 18:00:52');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `blog_id` int NULL DEFAULT NULL,
  `comment_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  `create_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `fk_comment_user`(`user_id`) USING BTREE,
  INDEX `fk_comment_blog`(`blog_id`) USING BTREE,
  CONSTRAINT `fk_comment_blog` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`blog_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (11, 11, 68, '评论评论🥰哈哈哈', 0, '2022-03-18 16:42:04');
INSERT INTO `comment` VALUES (12, 11, 77, '你好呀，很高兴认识你', 0, '2022-03-22 01:47:16');
INSERT INTO `comment` VALUES (13, 11, 77, '天气真好呀', 0, '2022-03-23 22:58:17');
INSERT INTO `comment` VALUES (14, 11, 83, '哈哈哈哈😀哈哈哈', 0, '2022-03-23 23:11:30');
INSERT INTO `comment` VALUES (15, 11, 77, '哈哈哈哈', 0, '2022-05-01 17:33:23');
INSERT INTO `comment` VALUES (16, 12, 77, 'hhhh', 0, '2022-05-05 13:24:47');
INSERT INTO `comment` VALUES (17, 12, 84, 'nihao ', 0, '2022-05-05 13:25:48');
INSERT INTO `comment` VALUES (18, 11, 68, 'hello', 0, '2022-05-06 18:01:01');

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `follow_id` int NOT NULL AUTO_INCREMENT,
  `followee_id` int NULL DEFAULT NULL,
  `follower_id` int NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`follow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of follow
-- ----------------------------
INSERT INTO `follow` VALUES (46, 12, 11, '2022-05-02 23:04:09');

-- ----------------------------
-- Table structure for love
-- ----------------------------
DROP TABLE IF EXISTS `love`;
CREATE TABLE `love`  (
  `love_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `blog_id` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  `create_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`love_id`) USING BTREE,
  INDEX `fk_love_user`(`user_id`) USING BTREE,
  INDEX `fk_love_blog`(`blog_id`) USING BTREE,
  CONSTRAINT `fk_love_blog` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`blog_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_love_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 186 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of love
-- ----------------------------
INSERT INTO `love` VALUES (161, 11, 70, 0, '2022-03-18 16:41:03');
INSERT INTO `love` VALUES (163, 11, 74, 0, '2022-03-18 16:47:51');
INSERT INTO `love` VALUES (182, 12, 77, 0, '2022-05-05 23:04:45');
INSERT INTO `love` VALUES (184, 11, 77, 0, '2022-05-05 23:08:54');
INSERT INTO `love` VALUES (185, 11, 83, 0, '2022-05-06 02:05:34');
INSERT INTO `love` VALUES (186, 11, 68, 0, '2022-05-06 18:00:41');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `message_id` int NOT NULL AUTO_INCREMENT,
  `from_id` int NULL DEFAULT NULL,
  `to_id` int NULL DEFAULT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 126 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (101, 12, 11, '聊聊呗', '2022-03-18 16:51:02');
INSERT INTO `message` VALUES (102, 11, 12, '你是谁', '2022-03-18 16:51:15');
INSERT INTO `message` VALUES (103, 12, 11, '猜猜我是谁', '2022-03-18 16:51:26');
INSERT INTO `message` VALUES (104, 12, 0, '这是群聊室，可以对大家说话', '2022-03-18 16:52:04');
INSERT INTO `message` VALUES (105, 11, 0, '那我也说一句', '2022-03-18 16:52:09');
INSERT INTO `message` VALUES (106, 11, 12, '你好', '2022-03-20 01:10:42');
INSERT INTO `message` VALUES (107, 12, 11, 'niaho ', '2022-03-20 01:12:01');
INSERT INTO `message` VALUES (108, 11, 12, 'hello', '2022-03-20 01:12:06');
INSERT INTO `message` VALUES (109, 11, 12, '年后', '2022-05-01 17:33:31');
INSERT INTO `message` VALUES (110, 12, 11, '哈哈哈', '2022-05-01 17:34:15');
INSERT INTO `message` VALUES (111, 11, 12, '你好', '2022-05-02 00:21:17');
INSERT INTO `message` VALUES (112, 11, 12, 'hj', '2022-05-02 00:22:24');
INSERT INTO `message` VALUES (113, 11, 12, '就', '2022-05-02 00:22:28');
INSERT INTO `message` VALUES (114, 11, 12, 'f ', '2022-05-02 00:24:21');
INSERT INTO `message` VALUES (115, 11, 12, 'niaho ', '2022-05-02 00:24:35');
INSERT INTO `message` VALUES (116, 11, 12, 'nihao', '2022-05-02 00:25:15');
INSERT INTO `message` VALUES (117, 11, 12, '哈哈哈', '2022-05-02 00:25:18');
INSERT INTO `message` VALUES (118, 11, 0, '你好', '2022-05-02 00:26:42');
INSERT INTO `message` VALUES (119, 11, 12, '你好', '2022-05-02 00:34:50');
INSERT INTO `message` VALUES (120, 11, 12, '哈哈哈', '2022-05-02 00:38:03');
INSERT INTO `message` VALUES (121, 11, 12, '天气真好', '2022-05-02 00:38:08');
INSERT INTO `message` VALUES (122, 11, 12, '你好', '2022-05-02 00:40:19');
INSERT INTO `message` VALUES (123, 11, 12, '天气', '2022-05-02 00:58:22');
INSERT INTO `message` VALUES (124, 11, 12, '谢谢', '2022-05-02 01:46:07');
INSERT INTO `message` VALUES (125, 11, 12, '11', '2022-05-04 01:04:26');
INSERT INTO `message` VALUES (126, 11, 0, '哈哈哈', '2022-05-04 01:04:33');

-- ----------------------------
-- Table structure for picture
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture`  (
  `picture_id` int NOT NULL AUTO_INCREMENT,
  `blog_id` int NULL DEFAULT NULL,
  `picture_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`picture_id`) USING BTREE,
  INDEX `fk_picture_blog`(`blog_id`) USING BTREE,
  CONSTRAINT `fk_picture_blog` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`blog_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 87 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of picture
-- ----------------------------
INSERT INTO `picture` VALUES (63, 68, '1647592796803.jpg', '2022-03-18 16:39:57');
INSERT INTO `picture` VALUES (64, 68, '1647592796888.jpg', '2022-03-18 16:39:57');
INSERT INTO `picture` VALUES (65, 68, '1647592797022.jpg', '2022-03-18 16:39:57');
INSERT INTO `picture` VALUES (66, 68, '1647592797131.png', '2022-03-18 16:39:57');
INSERT INTO `picture` VALUES (67, 68, '1647592797233.jpg', '2022-03-18 16:39:57');
INSERT INTO `picture` VALUES (68, 68, '1647592797354.jpg', '2022-03-18 16:39:57');
INSERT INTO `picture` VALUES (69, 68, '1647592797466.jpg', '2022-03-18 16:39:57');
INSERT INTO `picture` VALUES (70, 68, '1647592797618.jpg', '2022-03-18 16:39:58');
INSERT INTO `picture` VALUES (71, 68, '1647592797829.jpg', '2022-03-18 16:39:58');
INSERT INTO `picture` VALUES (72, 69, '1647592846113.jpg', '2022-03-18 16:40:46');
INSERT INTO `picture` VALUES (73, 69, '1647592846252.jpg', '2022-03-18 16:40:46');
INSERT INTO `picture` VALUES (74, 69, '1647592846420.jpg', '2022-03-18 16:40:46');
INSERT INTO `picture` VALUES (75, 69, '1647592846651.jpg', '2022-03-18 16:40:47');
INSERT INTO `picture` VALUES (76, 69, '1647592846830.jpg', '2022-03-18 16:40:47');
INSERT INTO `picture` VALUES (77, 76, '1647593398159.jpg', '2022-03-18 16:49:58');
INSERT INTO `picture` VALUES (78, 77, '1647593423537.jpg', '2022-03-18 16:50:24');
INSERT INTO `picture` VALUES (79, 79, '1647882790046.jpg', '2022-03-22 01:13:10');
INSERT INTO `picture` VALUES (80, 79, '1647882790180.jpg', '2022-03-22 01:13:10');
INSERT INTO `picture` VALUES (81, 79, '1647882790418.jpg', '2022-03-22 01:13:10');
INSERT INTO `picture` VALUES (82, 79, '1647882790527.png', '2022-03-22 01:13:11');
INSERT INTO `picture` VALUES (83, 79, '1647882790628.jpg', '2022-03-22 01:13:11');
INSERT INTO `picture` VALUES (84, 79, '1647882790729.jpg', '2022-03-22 01:13:11');
INSERT INTO `picture` VALUES (85, 79, '1647882790874.jpg', '2022-03-22 01:13:11');
INSERT INTO `picture` VALUES (86, 79, '1647882790961.jpg', '2022-03-22 01:13:11');
INSERT INTO `picture` VALUES (87, 79, '1647882791062.jpg', '2022-03-22 01:13:11');

-- ----------------------------
-- Table structure for relation
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation`  (
  `relation_id` int NOT NULL AUTO_INCREMENT,
  `from_id` int NULL DEFAULT NULL,
  `to_id` int NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`relation_id`) USING BTREE,
  INDEX `fk_relation_follower`(`from_id`) USING BTREE,
  INDEX `fk_relation_to`(`to_id`) USING BTREE,
  CONSTRAINT `fk_relation_from` FOREIGN KEY (`from_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_relation_to` FOREIGN KEY (`to_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of relation
-- ----------------------------
INSERT INTO `relation` VALUES (47, 12, 11, '2022-03-18 16:51:02');
INSERT INTO `relation` VALUES (48, 11, 12, '2022-03-18 16:51:15');
INSERT INTO `relation` VALUES (49, 12, 11, '2022-03-18 16:51:26');
INSERT INTO `relation` VALUES (50, 11, 12, '2022-03-20 01:10:43');
INSERT INTO `relation` VALUES (51, 12, 11, '2022-03-20 01:12:01');
INSERT INTO `relation` VALUES (52, 11, 12, '2022-03-20 01:12:06');
INSERT INTO `relation` VALUES (53, 11, 12, '2022-05-01 17:33:31');
INSERT INTO `relation` VALUES (54, 12, 11, '2022-05-01 17:34:15');
INSERT INTO `relation` VALUES (55, 11, 12, '2022-05-02 00:21:17');
INSERT INTO `relation` VALUES (56, 11, 12, '2022-05-02 00:22:25');
INSERT INTO `relation` VALUES (57, 11, 12, '2022-05-02 00:22:28');
INSERT INTO `relation` VALUES (58, 11, 12, '2022-05-02 00:24:21');
INSERT INTO `relation` VALUES (59, 11, 12, '2022-05-02 00:24:35');
INSERT INTO `relation` VALUES (60, 11, 12, '2022-05-02 00:25:15');
INSERT INTO `relation` VALUES (61, 11, 12, '2022-05-02 00:25:18');
INSERT INTO `relation` VALUES (62, 11, 12, '2022-05-02 00:34:50');
INSERT INTO `relation` VALUES (63, 11, 12, '2022-05-02 00:38:03');
INSERT INTO `relation` VALUES (64, 11, 12, '2022-05-02 00:38:08');
INSERT INTO `relation` VALUES (65, 11, 12, '2022-05-02 00:40:19');
INSERT INTO `relation` VALUES (66, 11, 12, '2022-05-02 00:58:22');
INSERT INTO `relation` VALUES (67, 11, 12, '2022-05-02 01:46:07');
INSERT INTO `relation` VALUES (68, 11, 12, '2022-05-04 01:04:26');

-- ----------------------------
-- Table structure for relay
-- ----------------------------
DROP TABLE IF EXISTS `relay`;
CREATE TABLE `relay`  (
  `relay_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `blog_id` int NULL DEFAULT NULL,
  `relay_comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`relay_id`) USING BTREE,
  INDEX `fk_relay_blog`(`blog_id`) USING BTREE,
  INDEX `fk_relay_user`(`user_id`) USING BTREE,
  CONSTRAINT `fk_relay_blog` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`blog_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_relay_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of relay
-- ----------------------------
INSERT INTO `relay` VALUES (18, 11, 74, NULL, '2022-03-18 16:47:53');
INSERT INTO `relay` VALUES (19, 12, 83, NULL, '2022-05-01 18:28:37');
INSERT INTO `relay` VALUES (20, 12, 68, NULL, '2022-05-05 13:30:57');
INSERT INTO `relay` VALUES (21, 11, 68, NULL, '2022-05-06 18:00:52');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar_id` int NULL DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `birth_date` date NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `fk_avatar`(`avatar_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (11, 'admin', '123456', 'admin', 108, '男', '简简单单的简介简简单单的简介', '2022-03-18', '2022-03-18 16:38:34');
INSERT INTO `user` VALUES (12, 'test', '123456', 'test1', 102, '女', 'test1的简介情况扒拉扒拉扒拉扒拉的扒拉扒拉扒拉扒拉的', '2022-03-18', '2022-03-18 16:49:02');
INSERT INTO `user` VALUES (13, 'tttt', '123456', 'nickname', 0, '男', 'sfa', '2022-03-08', '2022-03-19 22:44:35');

-- ----------------------------
-- Procedure structure for my_procedure
-- ----------------------------
DROP PROCEDURE IF EXISTS `my_procedure`;
delimiter ;;
CREATE PROCEDURE `my_procedure`()
begin
 DECLARE n int DEFAULT 1;
 WHILE n < 5 DO
 insert into b(id,age) values(1,1);
 END WHILE;
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
