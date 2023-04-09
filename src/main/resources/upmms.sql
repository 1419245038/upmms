/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1_3306
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : 127.0.0.1:3306
 Source Schema         : upmms

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 09/04/2023 23:44:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for adm_apply_form
-- ----------------------------
DROP TABLE IF EXISTS `adm_apply_form`;
CREATE TABLE `adm_apply_form`  (
  `form_id` int NOT NULL AUTO_INCREMENT COMMENT '表单编号',
  `user_id` int NOT NULL COMMENT '用户编号',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户姓名',
  `part_id` int NOT NULL COMMENT '申请加入党组织编号',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '入党申请材料url',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'processing-正在审批 approved-审批已通过 refused-审批被驳回',
  PRIMARY KEY (`form_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '入党申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_apply_form
-- ----------------------------

-- ----------------------------
-- Table structure for adm_notice
-- ----------------------------
DROP TABLE IF EXISTS `adm_notice`;
CREATE TABLE `adm_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '消息编号',
  `receiver_id` int NULL DEFAULT NULL COMMENT '消息接收人编号',
  `part_id` int NULL DEFAULT NULL COMMENT '消息接收党组织编号',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_notice
-- ----------------------------

-- ----------------------------
-- Table structure for adm_part
-- ----------------------------
DROP TABLE IF EXISTS `adm_part`;
CREATE TABLE `adm_part`  (
  `part_id` int NOT NULL AUTO_INCREMENT,
  `part_title` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '党组织名称',
  `part_addr` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '党组织地址',
  `part_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '党组织描述',
  PRIMARY KEY (`part_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '党组织表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of adm_part
-- ----------------------------
INSERT INTO `adm_part` VALUES (10, '沈阳工学院党支部', '辽宁省抚顺市沈抚示范区滨河路东段1号', '');

-- ----------------------------
-- Table structure for adm_part_user
-- ----------------------------
DROP TABLE IF EXISTS `adm_part_user`;
CREATE TABLE `adm_part_user`  (
  `pu_id` int NOT NULL AUTO_INCREMENT,
  `part_id` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`pu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户党组织关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_part_user
-- ----------------------------
INSERT INTO `adm_part_user` VALUES (16, 10, 25);

-- ----------------------------
-- Table structure for adm_position
-- ----------------------------
DROP TABLE IF EXISTS `adm_position`;
CREATE TABLE `adm_position`  (
  `position_id` int NOT NULL AUTO_INCREMENT COMMENT '职务编号',
  `position_title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '职务名称',
  `position_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职务描述',
  PRIMARY KEY (`position_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '职务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of adm_position
-- ----------------------------
INSERT INTO `adm_position` VALUES (1, '党员', NULL);
INSERT INTO `adm_position` VALUES (2, '预备党员', NULL);
INSERT INTO `adm_position` VALUES (3, '入党积极分子', NULL);
INSERT INTO `adm_position` VALUES (4, '书记', NULL);

-- ----------------------------
-- Table structure for adm_position_user
-- ----------------------------
DROP TABLE IF EXISTS `adm_position_user`;
CREATE TABLE `adm_position_user`  (
  `pu_id` int NOT NULL AUTO_INCREMENT,
  `position_id` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`pu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户职务关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_position_user
-- ----------------------------
INSERT INTO `adm_position_user` VALUES (15, 4, 25);
INSERT INTO `adm_position_user` VALUES (17, 1, 24);

-- ----------------------------
-- Table structure for adm_process_flow
-- ----------------------------
DROP TABLE IF EXISTS `adm_process_flow`;
CREATE TABLE `adm_process_flow`  (
  `process_id` int NOT NULL AUTO_INCREMENT COMMENT '处理任务编号',
  `form_id` int NOT NULL COMMENT '入党申请表单编号',
  `operator_id` int NULL DEFAULT NULL COMMENT '经办人编号',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经办人姓名',
  `part_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经办党组织编号',
  `action` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'apply-申请 audit-审批',
  `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'approved-同意 refused-驳回',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批意见',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审批时间',
  `order_no` int NOT NULL COMMENT '任务序号',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ready-准备 process-正在处理 complete-处理完成 cancel-取消',
  `is_last` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否最后节点 0-否 1-是',
  PRIMARY KEY (`process_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '审批流程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_process_flow
-- ----------------------------

-- ----------------------------
-- Table structure for adm_record
-- ----------------------------
DROP TABLE IF EXISTS `adm_record`;
CREATE TABLE `adm_record`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '党费缴纳记录编号',
  `user_id` int NOT NULL COMMENT '缴费用户编号',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '缴费用户名',
  `payment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '缴费日期',
  `money` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '缴费金额',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '缴费凭证图片',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '党费缴纳记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_record
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '配置项编号',
  `home_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '首页名称',
  `home_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '首页url',
  `logo_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站点标题',
  `logo_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站点logo',
  `logo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '点击站点logo跳转到的url',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '站点配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (3, '首页', 'welcome.html', '党员管理系统', '/common/download?name=', '');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '父ID',
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `icon` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '菜单图标',
  `href` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '链接',
  `target` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '_self' COMMENT '链接打开方式',
  `sort` int NULL DEFAULT 0 COMMENT '菜单排序',
  `status` int UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态(0:禁用,1:启用)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `title`(`title` ASC) USING BTREE,
  INDEX `href`(`href` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 293 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '系统菜单表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (255, 0, '系统管理', 'fa fa-align-justify', '', '_self', 1, 1);
INSERT INTO `sys_menu` VALUES (256, 255, '菜单管理', 'fa fa-align-left', 'page/menu/menu.html', '_self', 1, 1);
INSERT INTO `sys_menu` VALUES (276, 255, '党组织管理', 'fa fa-group', 'page/part/part.html', '_self', 5, 1);
INSERT INTO `sys_menu` VALUES (277, 255, '用户管理', 'fa fa-address-book-o', 'page/user/user.html', '_self', 2, 1);
INSERT INTO `sys_menu` VALUES (278, 255, '角色管理', 'fa fa-address-card', 'page/role/role.html', '_self', 3, 1);
INSERT INTO `sys_menu` VALUES (279, 0, '流程管理', 'fa fa-align-justify', '', '_self', 2, 1);
INSERT INTO `sys_menu` VALUES (280, 279, '入党申请', 'fa fa-envelope-o', 'page/apply/apply.html', '_self', 2, 1);
INSERT INTO `sys_menu` VALUES (281, 279, '入党审批', 'fa fa-check', 'page/approve/approve.html', '_self', 3, 1);
INSERT INTO `sys_menu` VALUES (282, 279, '消息通知', 'fa fa-commenting', 'page/notice/notice.html', '_self', 1, 1);
INSERT INTO `sys_menu` VALUES (283, 255, '站点设置', 'fa fa-sitemap', 'page/site/site.html', '_self', 6, 1);
INSERT INTO `sys_menu` VALUES (287, 0, '党费管理', 'fa fa-align-justify', '', '_self', 3, 1);
INSERT INTO `sys_menu` VALUES (288, 287, '党费缴纳记录', 'fa fa-columns', 'page/record/record.html', '_self', 2, 1);
INSERT INTO `sys_menu` VALUES (289, 287, '党费缴纳登记', 'fa fa-clipboard', 'page/register/register.html', '_self', 1, 1);
INSERT INTO `sys_menu` VALUES (290, 287, '党费缴纳提醒', 'fa fa-comment-o', 'page/remind/remind.html', '_self', 3, 1);
INSERT INTO `sys_menu` VALUES (291, 255, '职务管理', 'fa fa-hospital-o', 'page/position/position.html', '_self', 4, 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `role_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_description` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (3, '系统管理员', '拥有系统中最高权限');
INSERT INTO `sys_role` VALUES (12, '党组织管理员', '');
INSERT INTO `sys_role` VALUES (13, '普通用户', '');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `rm_id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `menu_id` int NOT NULL,
  PRIMARY KEY (`rm_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 148 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (128, 12, 282);
INSERT INTO `sys_role_menu` VALUES (129, 12, 281);
INSERT INTO `sys_role_menu` VALUES (130, 12, 287);
INSERT INTO `sys_role_menu` VALUES (131, 12, 289);
INSERT INTO `sys_role_menu` VALUES (132, 12, 288);
INSERT INTO `sys_role_menu` VALUES (133, 12, 290);
INSERT INTO `sys_role_menu` VALUES (134, 12, 279);
INSERT INTO `sys_role_menu` VALUES (135, 13, 282);
INSERT INTO `sys_role_menu` VALUES (136, 13, 280);
INSERT INTO `sys_role_menu` VALUES (137, 13, 289);
INSERT INTO `sys_role_menu` VALUES (138, 13, 288);
INSERT INTO `sys_role_menu` VALUES (139, 13, 279);
INSERT INTO `sys_role_menu` VALUES (140, 13, 287);
INSERT INTO `sys_role_menu` VALUES (141, 3, 255);
INSERT INTO `sys_role_menu` VALUES (142, 3, 256);
INSERT INTO `sys_role_menu` VALUES (143, 3, 277);
INSERT INTO `sys_role_menu` VALUES (144, 3, 278);
INSERT INTO `sys_role_menu` VALUES (145, 3, 291);
INSERT INTO `sys_role_menu` VALUES (146, 3, 276);
INSERT INTO `sys_role_menu` VALUES (147, 3, 283);

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user`  (
  `ru_id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`ru_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色用户关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES (20, 3, 19);
INSERT INTO `sys_role_user` VALUES (31, 12, 25);
INSERT INTO `sys_role_user` VALUES (32, 13, 24);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `salt` int NOT NULL COMMENT '盐值',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电子邮箱',
  `id_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `sex` int NOT NULL COMMENT '性别 1-男 2-女',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '住址',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `id_number`(`id_number` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (19, '超级管理员', '707401acfd7cb55cb1eed177c53d026b', 62, 'admin@qq.com', '350781196403076661', '13133333333', 1, '辽宁沈阳');
INSERT INTO `sys_user` VALUES (24, 'user01', 'c39849a94026a0f0275439e032804de2', 49, 'test@qq.com', '35078119640307990X', '13111111111', 1, '辽宁省鞍山市海城市');
INSERT INTO `sys_user` VALUES (25, 'user02', '1f36019ad5f9322f86463791239e7e96', 89, 'test@qq.com', '350781196403070403', '13333333333', 1, '辽宁沈阳');

SET FOREIGN_KEY_CHECKS = 1;
