/*
 Navicat Premium Data Transfer

 Source Server         : tokenswap-manager
 Source Server Type    : MySQL
 Source Server Version : 50643
 Source Host           : 154.209.69.121:3306
 Source Schema         : nft

 Target Server Type    : MySQL
 Target Server Version : 50643
 File Encoding         : 65001

 Date: 16/06/2021 16:39:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for nft_file
-- ----------------------------
DROP TABLE IF EXISTS `nft_file`;
CREATE TABLE `nft_file`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键，文件的md5',
  `file_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `pay_time` timestamp(0) NULL DEFAULT NULL COMMENT '付费时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `pub_time` timestamp(0) NULL DEFAULT NULL COMMENT '发布时间',
  `user_tag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户标识',
  `media_type` int(2) NULL DEFAULT NULL COMMENT '文件类型 0文件，1 图片，2视频，3音频',
  `file_status` int(2) NULL DEFAULT NULL COMMENT '状态，1. 已上传，2. 已发布，3. 已付费',
  `file_des` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `copyright_fee` decimal(20, 16) NULL DEFAULT NULL COMMENT '版权费（百分比）',
  `price` decimal(20, 16) NULL DEFAULT NULL COMMENT '售价',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for nft_file_log
-- ----------------------------
DROP TABLE IF EXISTS `nft_file_log`;
CREATE TABLE `nft_file_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `file_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件id',
  `log_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志内容',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型：0 普通变化，1 竞拍，2 出售',
  `other` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其他数据，以json字符串的形式存储',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for nft_userinfo
-- ----------------------------
DROP TABLE IF EXISTS `nft_userinfo`;
CREATE TABLE `nft_userinfo`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID，钱包地址',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `introduction` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简介',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
