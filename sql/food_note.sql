/*
 Navicat Premium Dump SQL

 Source Server         : local-db-mysql
 Source Server Type    : MySQL
 Source Server Version : 50741 (5.7.41)
 Source Host           : localhost:3306
 Source Schema         : food_share

 Target Server Type    : MySQL
 Target Server Version : 50741 (5.7.41)
 File Encoding         : 65001

 Date: 25/11/2024 15:19:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '美食类别ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类别名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '美食类别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------

-- ----------------------------
-- Table structure for content_net
-- ----------------------------
DROP TABLE IF EXISTS `content_net`;
CREATE TABLE `content_net`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '内容分享主键ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `gourmet_id` int(11) NULL DEFAULT NULL COMMENT '美食做法ID',
  `access_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '访问码',
  `valid_day` int(1) NULL DEFAULT NULL COMMENT '有效天数',
  `access_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '访问密码',
  `password_auth` tinyint(1) NULL DEFAULT NULL COMMENT '是否是密码认证',
  `create_time` datetime NULL DEFAULT NULL COMMENT '分享时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '内容分享信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of content_net
-- ----------------------------

-- ----------------------------
-- Table structure for cookbook
-- ----------------------------
DROP TABLE IF EXISTS `cookbook`;
CREATE TABLE `cookbook`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '食谱ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '所属分类ID',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '内容',
  `is_publish` tinyint(1) NULL DEFAULT NULL COMMENT '是否是公开食谱',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '食谱信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cookbook
-- ----------------------------

-- ----------------------------
-- Table structure for cookbook_nutriment
-- ----------------------------
DROP TABLE IF EXISTS `cookbook_nutriment`;
CREATE TABLE `cookbook_nutriment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '食谱营养素关联信息主键ID',
  `cookbook_id` int(11) NULL DEFAULT NULL COMMENT '食谱ID',
  `nutriment_id` int(11) NULL DEFAULT NULL COMMENT '营养素ID',
  `100g_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '每100g食物中含量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '食谱营养素关联信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cookbook_nutriment
-- ----------------------------

-- ----------------------------
-- Table structure for diet_history
-- ----------------------------
DROP TABLE IF EXISTS `diet_history`;
CREATE TABLE `diet_history`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户饮食主键ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `cookbook_id` int(11) NULL DEFAULT NULL COMMENT '食谱ID',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '记录的值',
  `create_time` datetime NULL DEFAULT NULL COMMENT '记录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户饮食信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of diet_history
-- ----------------------------

-- ----------------------------
-- Table structure for evaluations
-- ----------------------------
DROP TABLE IF EXISTS `evaluations`;
CREATE TABLE `evaluations`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父级评论ID',
  `commenter_id` int(11) NULL DEFAULT NULL COMMENT '评论者ID',
  `replier_id` int(11) NULL DEFAULT NULL COMMENT '回复者ID',
  `content_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容类型',
  `content_id` int(11) NULL DEFAULT NULL COMMENT '内容ID',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '评论内容',
  `upvote_list` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '点赞列表，以\",\"分割',
  `create_time` datetime NULL DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 138 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of evaluations
-- ----------------------------
INSERT INTO `evaluations` VALUES (4, NULL, 8, NULL, 'FOOD', 10, 'I like it', '1,7,8', '2024-05-23 11:33:01');
INSERT INTO `evaluations` VALUES (7, 4, 3, NULL, 'FOOD', 10, 'wow', '1,7,8,3', '2024-06-08 17:06:19');
INSERT INTO `evaluations` VALUES (8, 1, 3, 3, 'FOOD', 10, 'really?', '1,7,8', '2024-06-08 17:06:19');
INSERT INTO `evaluations` VALUES (13, 1, 3, 7, 'FOOD', 10, 'magical!', '1,8', '2024-06-08 17:06:19');
INSERT INTO `evaluations` VALUES (14, NULL, 8, 3, 'FOOD', 10, 'perfect', '1,8', '2024-06-08 17:06:19');
INSERT INTO `evaluations` VALUES (23, NULL, 3, NULL, 'FOOD', 10, 'i like it', '3', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (27, 23, 3, 3, 'FOOD', 10, 'this is so cool', '3', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (29, 14, 3, NULL, 'FOOD', 10, 'yeah looks so nice', '3', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (40, NULL, 3, NULL, 'FOOD', 19, 'looks good', '3', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (44, 40, 3, NULL, 'FOOD', 19, 'love it', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (45, 40, 3, 3, 'FOOD', 19, 'cool', '3', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (47, 40, 3, 3, 'FOOD', 19, 'amazing', '3', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (49, NULL, 3, NULL, 'FOOD', 10, 'so great', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (50, 49, 3, NULL, 'FOOD', 10, 'lovely', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (51, 4, 3, NULL, 'FOOD', 10, 'wowwwwww', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (52, 4, 3, 8, 'FOOD', 10, 'nice', '3', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (53, 49, 3, 3, 'FOOD', 10, 'soooo good', '8', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (54, NULL, 3, NULL, 'FOOD', 18, 'enjoy', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (55, 54, 3, NULL, 'FOOD', 18, 'questionable', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (58, 57, 3, NULL, 'FOOD', 19, 'i like what i see', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (59, 57, 3, 3, 'FOOD', 19, 'unbelievable', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (61, NULL, 3, NULL, 'FOOD', 19, 'looks healthy', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (83, NULL, 3, NULL, 'FOOD', 13, 'smooth', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (84, NULL, 3, NULL, 'FOOD', 13, 'omg', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (85, NULL, 3, NULL, 'FOOD', 14, 'healthy', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (87, NULL, 3, NULL, 'FOOD', 16, 'hhhhhh', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (88, NULL, 3, NULL, 'FOOD', 16, 'i agree', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (89, NULL, 3, NULL, 'FOOD', 19, 'it looks amazing', '3', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (90, 89, 3, NULL, 'FOOD', 19, 'wow looks so good', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (116, NULL, 9, NULL, 'FOOD', 3, 'i enjoy this', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (117, NULL, 9, NULL, 'FOOD', 5, 'sounds good', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (118, 117, 9, NULL, 'FOOD', 5, 'looks like fun', NULL, '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (119, NULL, 9, NULL, 'FOOD', 12, 'peeeerrrfect', '9', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (120, 119, 9, NULL, 'FOOD', 12, 'i would like to try', '9', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (121, 119, 9, 9, 'FOOD', 12, 'amazingly good', '9', '2024-10-24 15:04:19');
INSERT INTO `evaluations` VALUES (122, NULL, 8, NULL, 'FOOD', 7, 'looks so cool', '10', '2024-11-13 23:02:43');
INSERT INTO `evaluations` VALUES (123, 122, 10, NULL, 'FOOD', 7, 'i love it', '8', '2024-11-13 23:03:29');
INSERT INTO `evaluations` VALUES (124, 122, 8, 10, 'FOOD', 7, 'sooks really good', '8', '2024-11-13 23:03:51');
INSERT INTO `evaluations` VALUES (127, 125, 3, 8, 'FOOD', 4, 'so pretty', '8', '2024-11-14 15:46:07');
INSERT INTO `evaluations` VALUES (131, NULL, 8, NULL, 'FOOD', 6, 'wow sooo nice', NULL, '2024-11-16 19:02:12');
INSERT INTO `evaluations` VALUES (132, 131, 3, NULL, 'FOOD', 6, 'neat', NULL, '2024-11-16 19:02:46');
INSERT INTO `evaluations` VALUES (134, 133, 8, NULL, 'FOOD', 6, 'i like it', NULL, '2024-11-16 19:03:53');

-- ----------------------------
-- Table structure for gourmet
-- ----------------------------
DROP TABLE IF EXISTS `gourmet`;
CREATE TABLE `gourmet`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '美食表主键ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '分类ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '内容',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '封面',
  `is_audit` tinyint(1) NULL DEFAULT NULL COMMENT '是否已经审核',
  `is_publish` tinyint(1) NULL DEFAULT NULL COMMENT '是否公开',
  `create_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '美食做法表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gourmet
-- ----------------------------

-- ----------------------------
-- Table structure for interaction
-- ----------------------------
DROP TABLE IF EXISTS `interaction`;
CREATE TABLE `interaction`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '互动表主键ID',
  `type` int(2) NULL DEFAULT NULL COMMENT '互动类型（1：点赞；2：收藏；3：浏览；4：评分）',
  `content_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容类型，表示的就是互动属于哪一个模块的东西',
  `content_id` int(11) NULL DEFAULT NULL COMMENT '内容ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `score` int(2) NULL DEFAULT NULL COMMENT '评分',
  `create_time` datetime NULL DEFAULT NULL COMMENT '互动时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '互动信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of interaction
-- ----------------------------

-- ----------------------------
-- Table structure for nutriment
-- ----------------------------
DROP TABLE IF EXISTS `nutriment`;
CREATE TABLE `nutriment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '营养素主键ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '单位',
  `is_publish` tinyint(1) NULL DEFAULT NULL COMMENT '是否公开',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '营养素信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nutriment
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `user_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户账号',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `user_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `user_email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `user_role` int(11) NULL DEFAULT NULL COMMENT '用户角色',
  `is_login` tinyint(1) NULL DEFAULT NULL COMMENT '可登录状态(0：可用，1：不可用)',
  `is_word` tinyint(1) NULL DEFAULT NULL COMMENT '禁言状态(0：可用，1：不可用)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '用户注册时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', '14e1b600b1fd579f47433b88e8d85291', 'http://192.168.1.8:21090//api/food-share-sys/v1.0/file/getFile?fileName=7366435Snipaste_2024-11-22_00-46-13.png', '1343243@qq.com', 1, 0, 0, '2024-10-25 12:53:05');
INSERT INTO `user` VALUES (3, 'Zhang', 'Zhang', '1f32aa4c9a1d2ea010adcf2348166a04', 'http://192.168.1.8:21090//api/food-share-sys/v1.0/file/getFile?fileName=0427ca6Snipaste_2024-11-22_00-47-21.png', '1134123@qq.com', 2, 0, 0, '2024-10-09 12:53:05');
INSERT INTO `user` VALUES (8, 'Cindy', 'Cindy', '14e1b600b1fd579f47433b88e8d85291', 'http://192.168.1.8:21090//api/food-share-sys/v1.0/file/getFile?fileName=b4974f3Snipaste_2024-11-22_00-46-48.png', '123@qq.coom', 2, 0, 0, '2024-10-19 12:53:05');
INSERT INTO `user` VALUES (9, 'Linda', 'Linda', '14e1b600b1fd579f47433b88e8d85291', 'http://192.168.1.8:21090//api/food-share-sys/v1.0/file/getFile?fileName=c5efdefSnipaste_2024-11-22_00-47-02.png', '1243@qq.com', 2, 0, 0, '2024-10-15 12:53:05');
INSERT INTO `user` VALUES (10, 'Carl', 'Carl', '14e1b600b1fd579f47433b88e8d85291', 'http://192.168.1.8:21090//api/food-share-sys/v1.0/file/getFile?fileName=7137a0bSnipaste_2024-11-22_00-46-36.png', '4432432@qq.com', 2, 0, 0, '2024-10-25 16:42:32');
INSERT INTO `user` VALUES (11, 'Bob', 'Bob', '14e1b600b1fd579f47433b88e8d85291', 'http://192.168.1.8:21090//api/food-share-sys/v1.0/file/getFile?fileName=fd32821pic_4.jpg', '32121@qq.com', 2, 0, 0, '2024-10-25 16:42:53');
INSERT INTO `user` VALUES (12, 'Marven', 'Marven', 'bf99609f47ba4b3cd5da3b1c89fbf0ab', 'http://192.168.1.8:21090//api/food-share-sys/v1.0/file/getFile?fileName=50aa9b9pic_3.jpg', '14343@qq.com', 2, 1, 0, '2024-10-25 16:43:57');
INSERT INTO `user` VALUES (13, 'David', 'David', '14e1b600b1fd579f47433b88e8d85291', 'http://192.168.1.8:21090//api/food-share-sys/v1.0/file/getFile?fileName=6d40fd4Snipaste_2024-11-22_00-46-29.png', '12223211@qq.com', 1, 0, 1, '2024-10-25 16:44:46');
INSERT INTO `user` VALUES (14, 'Anna', 'Anna', '14e1b600b1fd579f47433b88e8d85291', 'http://192.168.1.8:21090//api/food-share-sys/v1.0/file/getFile?fileName=c84a4b0pic_2.jpg', NULL, 1, 0, 0, '2024-11-17 16:26:15');
INSERT INTO `user` VALUES (15, 'Bella', 'Bella', 'bb8f3e82dd24423ea6358540d3651621', 'http://192.168.1.8:21090//api/food-share-sys/v1.0/file/getFile?fileName=2cb9e0cpic_7.jpg', '432432', 1, 1, 1, '2024-11-25 13:23:55');

SET FOREIGN_KEY_CHECKS = 1;
