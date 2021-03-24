/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : me-oa

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 24/03/2021 15:59:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for adm_department
-- ----------------------------
DROP TABLE IF EXISTS `adm_department`;
CREATE TABLE `adm_department`  (
  `department_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `department_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  PRIMARY KEY (`department_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_department
-- ----------------------------
INSERT INTO `adm_department` VALUES (1, '总裁办');
INSERT INTO `adm_department` VALUES (2, '研发部');
INSERT INTO `adm_department` VALUES (3, '市场部');

-- ----------------------------
-- Table structure for adm_employee
-- ----------------------------
DROP TABLE IF EXISTS `adm_employee`;
CREATE TABLE `adm_employee`  (
  `employee_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '员工id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工姓名',
  `department_id` bigint NOT NULL COMMENT '部门id',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工职称',
  `level` int NOT NULL COMMENT '员工级别',
  PRIMARY KEY (`employee_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_employee
-- ----------------------------
INSERT INTO `adm_employee` VALUES (1, '张晓涛', 1, '总经理', 8);
INSERT INTO `adm_employee` VALUES (2, '齐紫陌', 2, '部门经理', 7);
INSERT INTO `adm_employee` VALUES (3, '王美美', 2, '高级研发工程师', 6);
INSERT INTO `adm_employee` VALUES (4, '宋彩妮', 2, '研发工程师', 5);
INSERT INTO `adm_employee` VALUES (5, '欧阳峰', 2, '初级研发工程师', 4);
INSERT INTO `adm_employee` VALUES (6, '张世豪', 3, '部门经理', 7);
INSERT INTO `adm_employee` VALUES (7, '王子豪', 3, '大客户经理', 6);
INSERT INTO `adm_employee` VALUES (8, '段峰', 3, '客户经理', 5);
INSERT INTO `adm_employee` VALUES (9, '章雪峰', 3, '客户经理', 4);
INSERT INTO `adm_employee` VALUES (10, '李莉', 3, '见习客户经理', 3);

-- ----------------------------
-- Table structure for adm_leave_form
-- ----------------------------
DROP TABLE IF EXISTS `adm_leave_form`;
CREATE TABLE `adm_leave_form`  (
  `form_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `employee_id` bigint NOT NULL COMMENT '员工编号',
  `form_type` int NOT NULL COMMENT '请假类型 1-事假 2-病假 3-工伤假 4-婚假 5-产假 6-丧假',
  `start_time` datetime(0) NOT NULL COMMENT '开始时间',
  `end_time` datetime(0) NOT NULL COMMENT '结束时间',
  `reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请假事由',
  `create_time` datetime(0) NOT NULL COMMENT '当前数据创建时间',
  `state` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'processing-正在审批 approved-审批已通过 refused-审批被驳回',
  PRIMARY KEY (`form_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_leave_form
-- ----------------------------
INSERT INTO `adm_leave_form` VALUES (31, 4, 3, '2020-03-29 00:00:00', '2020-04-04 00:00:00', '事故,小腿骨折,明天做手术', '2020-03-28 11:49:09', 'processing');
INSERT INTO `adm_leave_form` VALUES (32, 3, 1, '2020-03-29 00:00:00', '2020-04-04 00:00:00', '没啥原因,单纯想休息几天', '2020-03-28 11:50:35', 'refused');
INSERT INTO `adm_leave_form` VALUES (33, 6, 1, '2020-03-30 00:00:00', '2020-03-31 00:00:00', '带孩子去看牙', '2020-03-28 11:51:49', 'processing');

-- ----------------------------
-- Table structure for adm_process_flow
-- ----------------------------
DROP TABLE IF EXISTS `adm_process_flow`;
CREATE TABLE `adm_process_flow`  (
  `process_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '处理任务编号',
  `form_id` bigint NOT NULL COMMENT '表单编号，用于说明依附于哪张表单',
  `operator_id` bigint NOT NULL COMMENT '经办人编号',
  `action` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'apply-申请 audit-审批',
  `result` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'approved-同意 refused-驳回',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批意见',
  `create_time` datetime(0) NOT NULL COMMENT '记录的创建时间',
  `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '审批时间',
  `order_no` int NOT NULL COMMENT '任务序号',
  `state` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ready-准备 process-正在处理 complete-处理完成 cancel-取消',
  `is_last` int NOT NULL COMMENT '是否最后节点 0-否 1-是',
  PRIMARY KEY (`process_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adm_process_flow
-- ----------------------------
INSERT INTO `adm_process_flow` VALUES (74, 31, 4, 'apply', NULL, NULL, '2020-03-28 11:49:09', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (75, 31, 2, 'audit', NULL, NULL, '2020-03-28 11:49:09', NULL, 2, 'process', 0);
INSERT INTO `adm_process_flow` VALUES (76, 31, 1, 'audit', NULL, NULL, '2020-03-28 11:49:09', NULL, 3, 'ready', 1);
INSERT INTO `adm_process_flow` VALUES (77, 32, 3, 'apply', NULL, NULL, '2020-03-28 11:50:35', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (78, 32, 2, 'audit', 'refused', NULL, '2020-03-28 11:50:35', '2021-03-24 15:34:20', 2, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (79, 32, 1, 'audit', NULL, NULL, '2020-03-28 11:50:36', NULL, 3, 'cancel', 1);
INSERT INTO `adm_process_flow` VALUES (80, 33, 6, 'apply', NULL, NULL, '2020-03-28 11:51:49', NULL, 1, 'complete', 0);
INSERT INTO `adm_process_flow` VALUES (81, 33, 1, 'audit', NULL, NULL, '2020-03-28 11:51:49', NULL, 2, 'process', 1);

-- ----------------------------
-- Table structure for sys_node
-- ----------------------------
DROP TABLE IF EXISTS `sys_node`;
CREATE TABLE `sys_node`  (
  `node_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '节点编号',
  `node_type` int NOT NULL COMMENT '节点类型 1-模块 2-功能',
  `node_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点名称',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能地址',
  `node_code` int NOT NULL COMMENT '节点编码，用于排序',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '上级节点编号',
  PRIMARY KEY (`node_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_node
-- ----------------------------
INSERT INTO `sys_node` VALUES (1, 1, '行政审批', NULL, 1000000, NULL);
INSERT INTO `sys_node` VALUES (2, 2, '通知公告', '/forward/notice', 1000001, 1);
INSERT INTO `sys_node` VALUES (3, 2, '请假申请', '/forward/form', 1000002, 1);
INSERT INTO `sys_node` VALUES (4, 2, '请假审批', '/forward/audit', 1000003, 1);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `receiver_id` bigint NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (5, 4, '您的请假申请[2020-04-01-00时-2020-04-10-00时]已提交,请等待上级审批.', '2020-03-28 18:41:21');
INSERT INTO `sys_notice` VALUES (6, 2, '研发工程师-宋彩妮提起请假申请[2020-04-01-00时-2020-04-10-00时],请尽快审批', '2020-03-28 18:41:21');
INSERT INTO `sys_notice` VALUES (7, 4, '您的请假申请[2020-04-01-00时-2020-04-10-00时]部门经理齐紫陌已批准,审批意见:同意 ,请继续等待上级审批', '2020-03-28 18:43:18');
INSERT INTO `sys_notice` VALUES (8, 1, '研发工程师-宋彩妮提起请假申请[2020-04-01-00时-2020-04-10-00时],请尽快审批', '2020-03-28 18:43:18');
INSERT INTO `sys_notice` VALUES (9, 2, '研发工程师-宋彩妮提起请假申请[2020-04-01-00时-2020-04-10-00时]您已批准,审批意见:同意,申请转至上级领导继续审批', '2020-03-28 18:43:18');
INSERT INTO `sys_notice` VALUES (10, 4, '您的请假申请[2020-04-01-00时-2020-04-10-00时]总经理张晓涛已批准,审批意见:同意,审批流程已结束', '2020-03-28 18:44:43');
INSERT INTO `sys_notice` VALUES (11, 1, '研发工程师-宋彩妮提起请假申请[2020-04-01-00时-2020-04-10-00时]您已批准,审批意见:同意,审批流程已结束', '2020-03-28 18:44:43');
INSERT INTO `sys_notice` VALUES (12, 3, '您的请假申请[2020-04-01-00时-2020-04-30-00时]已提交,请等待上级审批.', '2020-03-28 21:16:39');
INSERT INTO `sys_notice` VALUES (13, 2, '高级研发工程师-王美美提起请假申请[2020-04-01-00时-2020-04-30-00时],请尽快审批', '2020-03-28 21:16:39');
INSERT INTO `sys_notice` VALUES (14, 3, '您的请假申请[2020-04-01-00时-2020-04-30-00时]部门经理齐紫陌已批准,审批意见:同意 ,请继续等待上级审批', '2020-03-28 21:18:01');
INSERT INTO `sys_notice` VALUES (15, 1, '高级研发工程师-王美美提起请假申请[2020-04-01-00时-2020-04-30-00时],请尽快审批', '2020-03-28 21:18:01');
INSERT INTO `sys_notice` VALUES (16, 2, '高级研发工程师-王美美提起请假申请[2020-04-01-00时-2020-04-30-00时]您已批准,审批意见:同意,申请转至上级领导继续审批', '2020-03-28 21:18:01');
INSERT INTO `sys_notice` VALUES (17, 3, '您的请假申请[2020-04-01-00时-2020-04-30-00时]总经理张晓涛已批准,审批意见:同意,审批流程已结束', '2020-03-28 21:19:05');
INSERT INTO `sys_notice` VALUES (18, 1, '高级研发工程师-王美美提起请假申请[2020-04-01-00时-2020-04-30-00时]您已批准,审批意见:同意,审批流程已结束', '2020-03-28 21:19:05');
INSERT INTO `sys_notice` VALUES (19, 2, '测试消息', '2021-03-22 17:14:24');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `role_description` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色描述',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '业务岗角色');
INSERT INTO `sys_role` VALUES (2, '管理岗角色');

-- ----------------------------
-- Table structure for sys_role_node
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_node`;
CREATE TABLE `sys_role_node`  (
  `rn_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `node_id` bigint NOT NULL COMMENT '节点id',
  PRIMARY KEY (`rn_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_node
-- ----------------------------
INSERT INTO `sys_role_node` VALUES (1, 1, 1);
INSERT INTO `sys_role_node` VALUES (2, 1, 2);
INSERT INTO `sys_role_node` VALUES (3, 1, 3);
INSERT INTO `sys_role_node` VALUES (4, 2, 1);
INSERT INTO `sys_role_node` VALUES (5, 2, 2);
INSERT INTO `sys_role_node` VALUES (6, 2, 3);
INSERT INTO `sys_role_node` VALUES (7, 2, 4);

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user`  (
  `ru_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `user_id` int NOT NULL COMMENT '用户id',
  PRIMARY KEY (`ru_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES (1, 2, 1);
INSERT INTO `sys_role_user` VALUES (2, 2, 2);
INSERT INTO `sys_role_user` VALUES (3, 1, 3);
INSERT INTO `sys_role_user` VALUES (4, 1, 4);
INSERT INTO `sys_role_user` VALUES (5, 1, 5);
INSERT INTO `sys_role_user` VALUES (6, 2, 6);
INSERT INTO `sys_role_user` VALUES (7, 1, 7);
INSERT INTO `sys_role_user` VALUES (8, 1, 8);
INSERT INTO `sys_role_user` VALUES (9, 1, 9);
INSERT INTO `sys_role_user` VALUES (10, 1, 10);
INSERT INTO `sys_role_user` VALUES (11, 1, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `employee_id` bigint NOT NULL COMMENT '用户所属员工编号',
  `salt` int NOT NULL COMMENT '盐值',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'm8', 'f57e762e3fb7e1e3ec8ec4db6a1248e1', 1, 188);
INSERT INTO `sys_user` VALUES (2, 't7', 'dcfa022748271dccf5532c834e98ad08', 2, 189);
INSERT INTO `sys_user` VALUES (3, 't6', '76ce11f8b004e8bdc8b0976b177c620d', 3, 190);
INSERT INTO `sys_user` VALUES (4, 't5', '11f04f04054772bc1a8fdc41e70c7977', 4, 191);
INSERT INTO `sys_user` VALUES (5, 't4', '8d7713848189a8d5c224f94f65d18b06', 5, 192);
INSERT INTO `sys_user` VALUES (6, 's7', '044214e86e07d96c97de79a2222188cd', 6, 193);
INSERT INTO `sys_user` VALUES (7, 's6', 'ecbd2f592ee65838328236d06ce35252', 7, 194);
INSERT INTO `sys_user` VALUES (8, 's5', '846ecc83bba8fe420adc38b39f897201', 8, 195);
INSERT INTO `sys_user` VALUES (9, 's4', 'c1e523cd2daa02f6cf4b98b2f26585fd', 9, 196);
INSERT INTO `sys_user` VALUES (10, 's3', '89e89f369e07634fbb2286efffb9492b', 10, 197);

SET FOREIGN_KEY_CHECKS = 1;
