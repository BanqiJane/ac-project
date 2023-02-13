/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : security_flux_demo

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 12/01/2023 10:59:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `permission_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `parent_id` int NULL DEFAULT NULL,
  `enable` bit(1) NOT NULL DEFAULT b'1',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, 'user:list', '2023-01-12 08:42:42', '2023-01-12 08:42:43', NULL, b'1', NULL);
INSERT INTO `permission` VALUES (2, 'user:save', '2023-01-12 08:42:42', '2023-01-12 08:42:50', 1, b'1', NULL);
INSERT INTO `permission` VALUES (3, 'user:delete', '2023-01-12 08:42:42', '2023-01-12 08:42:50', 1, b'1', NULL);
INSERT INTO `permission` VALUES (4, 'role:list', '2023-01-12 08:42:42', '2023-01-12 08:42:50', NULL, b'1', NULL);
INSERT INTO `permission` VALUES (5, 'role:save', '2023-01-12 08:42:42', '2023-01-12 08:42:50', 4, b'1', NULL);
INSERT INTO `permission` VALUES (6, 'role:delete', '2023-01-12 08:42:42', '2023-01-12 08:42:50', 4, b'1', NULL);
INSERT INTO `permission` VALUES (7, 'permission:list', '2023-01-12 08:42:42', '2023-01-12 08:42:50', NULL, b'1', NULL);
INSERT INTO `permission` VALUES (8, 'permission:save', '2023-01-12 08:42:42', '2023-01-12 08:42:50', 7, b'1', NULL);
INSERT INTO `permission` VALUES (9, 'permission:delete', '2023-01-12 08:42:42', '2023-01-12 08:42:50', 7, b'1', NULL);
INSERT INTO `permission` VALUES (10, 'permission:bind', '2023-01-12 08:42:42', '2023-01-12 08:42:50', 7, b'1', NULL);
INSERT INTO `permission` VALUES (11, 'role:bind', '2023-01-12 08:42:42', '2023-01-12 08:42:50', 4, b'1', NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `enable` bit(1) NOT NULL DEFAULT b'1',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '管理员', 'MANAGER', '2023-01-11 10:04:39', '2023-01-11 10:04:41', b'1', NULL);
INSERT INTO `role` VALUES (2, '二级管理员', 'SECOND_MANAGER', '2023-01-11 10:04:42', '2023-01-11 10:04:44', b'1', NULL);
INSERT INTO `role` VALUES (3, '用户', 'USER', '2023-01-11 10:04:45', '2023-01-11 10:04:47', b'1', NULL);

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `role_id` int NULL DEFAULT NULL,
  `permission_id` int NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, 1);
INSERT INTO `role_permission` VALUES (1, 2);
INSERT INTO `role_permission` VALUES (1, 3);
INSERT INTO `role_permission` VALUES (1, 4);
INSERT INTO `role_permission` VALUES (1, 5);
INSERT INTO `role_permission` VALUES (1, 6);
INSERT INTO `role_permission` VALUES (1, 7);
INSERT INTO `role_permission` VALUES (1, 8);
INSERT INTO `role_permission` VALUES (1, 9);
INSERT INTO `role_permission` VALUES (1, 10);
INSERT INTO `role_permission` VALUES (1, 11);
INSERT INTO `role_permission` VALUES (2, 1);
INSERT INTO `role_permission` VALUES (2, 4);
INSERT INTO `role_permission` VALUES (2, 7);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `enable` bit(1) NOT NULL DEFAULT b'1',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'manager', '1', '2023-01-11 10:05:18', NULL, b'1', NULL);
INSERT INTO `user` VALUES (2, 'second_manager', '2', NULL, NULL, b'1', NULL);
INSERT INTO `user` VALUES (3, 'user1', 'u1', NULL, NULL, b'1', NULL);
INSERT INTO `user` VALUES (4, '默认用户', NULL, '2023-01-11 15:14:41', NULL, b'1', NULL);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_id` int NULL DEFAULT NULL,
  `role_id` int NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1);
INSERT INTO `user_role` VALUES (1, 2);
INSERT INTO `user_role` VALUES (1, 3);
INSERT INTO `user_role` VALUES (2, 2);
INSERT INTO `user_role` VALUES (2, 3);
INSERT INTO `user_role` VALUES (3, 3);

SET FOREIGN_KEY_CHECKS = 1;
