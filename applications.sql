/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44)
 Source Host           : localhost:3306
 Source Schema         : hrms

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44)
 File Encoding         : 65001

 Date: 08/06/2025 14:48:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for applications
-- ----------------------------
DROP TABLE IF EXISTS `applications`;
CREATE TABLE `applications`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_type` enum('EQUIPMENT','LEAVE','OTHER','OVERTIME','REIMBURSEMENT','RESIGNATION','TRANSFER') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '????',
  `applicant_id` bigint(20) NOT NULL COMMENT '???ID',
  `current_approver_id` bigint(20) NULL DEFAULT NULL COMMENT '?????ID',
  `status` enum('APPROVED','CANCELLED','IN_REVIEW','PENDING','REJECTED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `approve_time` datetime NULL DEFAULT NULL COMMENT '????',
  `approve_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '????',
  `priority` enum('LOW','NORMAL','HIGH','URGENT') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'NORMAL' COMMENT '???',
  `attachment_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '????',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_applications_applicant_id`(`applicant_id`) USING BTREE,
  INDEX `idx_applications_current_approver_id`(`current_approver_id`) USING BTREE,
  INDEX `idx_applications_status`(`status`) USING BTREE,
  INDEX `idx_applications_type`(`application_type`) USING BTREE,
  INDEX `idx_applications_apply_time`(`apply_time`) USING BTREE,
  CONSTRAINT `applications_ibfk_1` FOREIGN KEY (`applicant_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `applications_ibfk_2` FOREIGN KEY (`current_approver_id`) REFERENCES `employees` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '?????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of applications
-- ----------------------------
INSERT INTO `applications` VALUES (1, 'LEAVE', '123', '123', 8, NULL, 'APPROVED', '2025-06-07 14:40:31', '2025-06-07 14:55:09', '同意', 'NORMAL', NULL, '2025-06-07 14:55:09');
INSERT INTO `applications` VALUES (2, 'OVERTIME', '123123', '123123123123', 8, 8, 'APPROVED', '2025-06-07 14:43:25', '2025-06-07 14:56:09', 'ddddd', 'HIGH', NULL, '2025-06-07 14:56:09');
INSERT INTO `applications` VALUES (3, 'EQUIPMENT', '123', '123', 8, 8, 'REJECTED', '2025-06-07 14:44:19', '2025-06-07 15:12:27', '拒绝', 'NORMAL', NULL, '2025-06-07 15:12:27');
INSERT INTO `applications` VALUES (4, 'OTHER', '123', '123123', 8, 8, 'APPROVED', '2025-06-07 14:44:27', '2025-06-07 15:45:11', '12', 'NORMAL', NULL, '2025-06-07 15:45:11');
INSERT INTO `applications` VALUES (5, 'EQUIPMENT', '123', '123', 8, 8, 'APPROVED', '2025-06-07 14:45:22', '2025-06-07 15:48:09', '通过aaa', 'NORMAL', NULL, '2025-06-07 15:48:09');
INSERT INTO `applications` VALUES (6, 'LEAVE', '3122222', '12312', 8, 8, 'REJECTED', '2025-06-07 15:48:23', '2025-06-07 15:48:27', '123123123', 'NORMAL', NULL, '2025-06-07 15:48:27');
INSERT INTO `applications` VALUES (7, 'REIMBURSEMENT', '顶顶顶顶', '等等的点点滴滴', 8, 8, 'APPROVED', '2025-06-08 02:57:39', '2025-06-08 03:09:30', '通过', 'URGENT', NULL, '2025-06-08 03:09:30');
INSERT INTO `applications` VALUES (8, 'LEAVE', '咳嗽', '咳嗽请假', 8, 8, 'APPROVED', '2025-06-08 06:35:43', '2025-06-08 06:35:51', '同意', 'HIGH', NULL, '2025-06-08 06:35:51');
INSERT INTO `applications` VALUES (9, 'LEAVE', '测试1', '测试1', 8, 8, 'REJECTED', '2025-06-08 06:36:16', '2025-06-08 06:36:30', '不可以', 'URGENT', NULL, '2025-06-08 06:36:30');

-- ----------------------------
-- Table structure for departments
-- ----------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '???' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` VALUES (1, '研发', '2025-06-01 02:02:53');
INSERT INTO `departments` VALUES (2, '产品', '2025-06-01 02:02:53');
INSERT INTO `departments` VALUES (3, '我的', '2025-06-01 02:02:53');
INSERT INTO `departments` VALUES (4, '123', '2025-06-01 02:02:53');
INSERT INTO `departments` VALUES (5, '测试', '2025-06-01 02:02:53');
INSERT INTO `departments` VALUES (6, '运营', '2025-06-01 02:02:53');

-- ----------------------------
-- Table structure for employees
-- ----------------------------
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '??',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '??',
  `position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '??',
  `employee_type` enum('CONTRACTOR','FULL_TIME','INTERN','PART_TIME') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `department_id` bigint(20) NULL DEFAULT NULL COMMENT '??ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '????ID',
  `manager_id` bigint(20) NULL DEFAULT NULL COMMENT '????ID',
  `hire_date` date NULL DEFAULT NULL COMMENT '????',
  `status` enum('ACTIVE','INACTIVE','SUSPENDED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '????',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '???',
  `employee_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '??',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE,
  UNIQUE INDEX `employee_number`(`employee_number`) USING BTREE,
  INDEX `idx_employees_department_id`(`department_id`) USING BTREE,
  INDEX `idx_employees_user_id`(`user_id`) USING BTREE,
  INDEX `idx_employees_manager_id`(`manager_id`) USING BTREE,
  INDEX `idx_employees_status`(`status`) USING BTREE,
  INDEX `idx_employees_employee_number`(`employee_number`) USING BTREE,
  CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `employees_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `employees_ibfk_3` FOREIGN KEY (`manager_id`) REFERENCES `employees` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '???' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employees
-- ----------------------------
INSERT INTO `employees` VALUES (7, '张三', '123123W@qq.com', '123123', 'FULL_TIME', 1, 5, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 06:40:23', '2025-06-07 12:18:04');
INSERT INTO `employees` VALUES (8, '离宿舍', '1016608214@qq.com', '123', 'FULL_TIME', 2, 1, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 06:40:30', '2025-06-07 12:18:08');
INSERT INTO `employees` VALUES (9, 'ddd的', '123123W@qq.com', '12312', 'FULL_TIME', 1, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 06:40:37', '2025-06-07 15:21:51');
INSERT INTO `employees` VALUES (10, 'ddd大队', '123123W@qq.com', '12312', 'FULL_TIME', 1, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 06:40:43', '2025-06-07 15:21:52');
INSERT INTO `employees` VALUES (11, 'dasda咚咚咚', '123123W@qq.com', '123', 'FULL_TIME', 3, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 06:40:52', '2025-06-07 15:21:54');
INSERT INTO `employees` VALUES (12, '123', '123123W@qq.com', '123123', 'FULL_TIME', 4, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 06:50:16', '2025-06-07 15:21:55');
INSERT INTO `employees` VALUES (13, '12222', '123123W@qq.com', '123123', 'FULL_TIME', NULL, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 06:50:59', '2025-06-07 15:21:58');
INSERT INTO `employees` VALUES (14, '123', '1016608214@qq.com', '123', 'FULL_TIME', 2, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 07:13:08', '2025-06-07 15:21:59');
INSERT INTO `employees` VALUES (15, 'aaaaa', '123123W@qq.com', '123123', 'FULL_TIME', NULL, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 07:25:06', '2025-06-07 07:25:06');
INSERT INTO `employees` VALUES (16, '123', '123123W@qq.com', '1231', 'FULL_TIME', NULL, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 07:25:30', '2025-06-07 07:25:30');
INSERT INTO `employees` VALUES (17, 'bbbb', 'ba@qq.com', '12312', 'FULL_TIME', NULL, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 07:28:05', '2025-06-07 07:28:05');
INSERT INTO `employees` VALUES (18, 'd', '123123W@qq.com', '1231', 'FULL_TIME', NULL, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 07:29:24', '2025-06-07 07:29:24');
INSERT INTO `employees` VALUES (19, 'd', '123123W@qq.com', '1231', 'FULL_TIME', NULL, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 07:29:40', '2025-06-07 07:29:40');
INSERT INTO `employees` VALUES (20, 'd', '123123W@qq.com', '1231', 'FULL_TIME', NULL, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 07:30:46', '2025-06-07 07:30:46');
INSERT INTO `employees` VALUES (21, '123', '123123W@qq.com', '123', 'FULL_TIME', NULL, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 07:31:02', '2025-06-07 07:31:02');
INSERT INTO `employees` VALUES (22, '123', '123123W@qq.com', '123', 'FULL_TIME', 2, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 07:34:23', '2025-06-07 07:34:23');
INSERT INTO `employees` VALUES (23, '123', '13783138605@163.com', '123', 'FULL_TIME', 4, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 07:34:55', '2025-06-07 07:34:55');
INSERT INTO `employees` VALUES (25, '1', '13783138605@163.com', '123', 'FULL_TIME', 4, NULL, NULL, NULL, 'ACTIVE', NULL, NULL, '2025-06-07 07:35:47', '2025-06-07 07:35:47');

-- ----------------------------
-- Table structure for operation_logs
-- ----------------------------
DROP TABLE IF EXISTS `operation_logs`;
CREATE TABLE `operation_logs`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '????ID',
  `operation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '????',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '????',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP??',
  `user_agent` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '????',
  `status` enum('SUCCESS','FAILED','ERROR') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'SUCCESS' COMMENT '????',
  `execution_time` bigint(20) NULL DEFAULT NULL COMMENT '????(??)',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_operation_logs_user_id`(`user_id`) USING BTREE,
  INDEX `idx_operation_logs_operation`(`operation`) USING BTREE,
  INDEX `idx_operation_logs_status`(`status`) USING BTREE,
  INDEX `idx_operation_logs_created_time`(`created_time`) USING BTREE,
  CONSTRAINT `operation_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '?????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operation_logs
-- ----------------------------
INSERT INTO `operation_logs` VALUES (3, NULL, 'page', 'GET', '[Ljava.lang.Object;@61099063', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 34, '2025-06-08 06:20:38');
INSERT INTO `operation_logs` VALUES (4, NULL, 'page', 'GET', '[Ljava.lang.Object;@184c3550', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 25, '2025-06-08 06:20:40');
INSERT INTO `operation_logs` VALUES (5, NULL, 'getUnboundUsers', 'GET', '[Ljava.lang.Object;@34ece978', '[User{id=3, username=\'pm001\', email=\'pm001@hrms.com\', enabled=true}, User{id=4, username=\'tester001\', email=\'tester001@hrms.com\', enabled=true}, User{id=7, username=\'asd\', email=\'asd@11.com\', enabled=true}]', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 11, '2025-06-08 06:20:42');
INSERT INTO `operation_logs` VALUES (6, NULL, 'create', 'POST', '[Ljava.lang.Object;@b1df7c4', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 7, '2025-06-08 06:20:48');
INSERT INTO `operation_logs` VALUES (7, NULL, 'page', 'GET', '[Ljava.lang.Object;@27a8a4b2', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 17, '2025-06-08 06:20:48');
INSERT INTO `operation_logs` VALUES (8, NULL, 'page', 'GET', '[Ljava.lang.Object;@5d5aec34', 'Page 2 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 5, '2025-06-08 06:20:50');
INSERT INTO `operation_logs` VALUES (9, NULL, 'page', 'GET', '[Ljava.lang.Object;@791682d7', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 10, '2025-06-08 06:21:58');
INSERT INTO `operation_logs` VALUES (10, 1, 'page', 'GET', '[Ljava.lang.Object;@fcb1e6b', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 43, '2025-06-08 06:23:51');
INSERT INTO `operation_logs` VALUES (11, 1, 'getUnboundUsers', 'GET', '[Ljava.lang.Object;@5d724aa9', '[User{id=3, username=\'pm001\', email=\'pm001@hrms.com\', enabled=true}, User{id=4, username=\'tester001\', email=\'tester001@hrms.com\', enabled=true}, User{id=7, username=\'asd\', email=\'asd@11.com\', enabled=true}]', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 20, '2025-06-08 06:23:53');
INSERT INTO `operation_logs` VALUES (12, 1, 'create', 'POST', '[Ljava.lang.Object;@6e0fac36', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 9, '2025-06-08 06:23:58');
INSERT INTO `operation_logs` VALUES (13, 1, 'page', 'GET', '[Ljava.lang.Object;@5dc76bc6', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 19, '2025-06-08 06:23:58');
INSERT INTO `operation_logs` VALUES (14, 1, 'page', 'GET', '[Ljava.lang.Object;@c7f4df7', 'Page 2 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 10, '2025-06-08 06:24:01');
INSERT INTO `operation_logs` VALUES (15, 1, 'page', 'GET', '[Ljava.lang.Object;@6f99cee7', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 20, '2025-06-08 06:25:37');
INSERT INTO `operation_logs` VALUES (16, 1, 'login', 'POST', '[Ljava.lang.Object;@581690d9', '{code=20000, data={token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc0OTM2NDA4NiwiZXhwIjoxNzQ5NDUwNDg2fQ.7uJwayYF_2be1YQ2fshuFZ7MQaH8qEJg1ccVDECjapGjcc2TZ1y1ZVsIb27bUq9NynCrdkKvPqciJlLPbeBvaA}}', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 99, '2025-06-08 06:28:07');
INSERT INTO `operation_logs` VALUES (17, 1, 'page', 'GET', '[Ljava.lang.Object;@586f6085', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 15, '2025-06-08 06:28:07');
INSERT INTO `operation_logs` VALUES (18, 1, 'page', 'GET', '[Ljava.lang.Object;@4bd0476e', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 14, '2025-06-08 06:28:11');
INSERT INTO `operation_logs` VALUES (19, 1, 'getUnboundUsers', 'GET', '[Ljava.lang.Object;@44d2a4ab', '[User{id=3, username=\'pm001\', email=\'pm001@hrms.com\', enabled=true}, User{id=4, username=\'tester001\', email=\'tester001@hrms.com\', enabled=true}, User{id=7, username=\'asd\', email=\'asd@11.com\', enabled=true}]', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 9, '2025-06-08 06:28:12');
INSERT INTO `operation_logs` VALUES (20, 1, 'create', 'POST', '[Ljava.lang.Object;@4509cddc', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 8, '2025-06-08 06:28:29');
INSERT INTO `operation_logs` VALUES (21, 1, 'page', 'GET', '[Ljava.lang.Object;@61286193', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 13, '2025-06-08 06:28:29');
INSERT INTO `operation_logs` VALUES (22, 1, 'page', 'GET', '[Ljava.lang.Object;@1864897f', 'Page 2 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 7, '2025-06-08 06:28:31');
INSERT INTO `operation_logs` VALUES (23, 1, 'get', 'GET', '[Ljava.lang.Object;@1802c53', 'EmployeeDTO(id=30, name=测试111, email=ceshi@qq.com, position=只为, departmentName=我的, departmentId=null, username=null, userId=null)', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 3, '2025-06-08 06:28:34');
INSERT INTO `operation_logs` VALUES (24, 1, 'page', 'GET', '[Ljava.lang.Object;@45073598', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 10, '2025-06-08 06:28:38');
INSERT INTO `operation_logs` VALUES (25, 1, 'page', 'GET', '[Ljava.lang.Object;@2c3dbaeb', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 12, '2025-06-08 06:29:14');
INSERT INTO `operation_logs` VALUES (26, 1, 'login', 'POST', '[Ljava.lang.Object;@785a3138', '{code=20000, data={token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc0OTM2NDE2NSwiZXhwIjoxNzQ5NDUwNTY1fQ.gKDMMkPHD9Zu7JFmyziC_7yp-3tXpGfyC_Rld6N93pRC2bv3vTnitH27OlUQyCzppuppkAV4EC8wcXpJBpAwPQ}}', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 88, '2025-06-08 06:29:26');
INSERT INTO `operation_logs` VALUES (27, 1, 'page', 'GET', '[Ljava.lang.Object;@1be0537f', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 12, '2025-06-08 06:29:26');
INSERT INTO `operation_logs` VALUES (28, 1, 'getUnboundUsers', 'GET', '[Ljava.lang.Object;@37ed40e4', '[User{id=3, username=\'pm001\', email=\'pm001@hrms.com\', enabled=true}, User{id=7, username=\'asd\', email=\'asd@11.com\', enabled=true}]', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 7, '2025-06-08 06:29:27');
INSERT INTO `operation_logs` VALUES (29, 1, 'create', 'POST', '[Ljava.lang.Object;@15a53816', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 6, '2025-06-08 06:29:40');
INSERT INTO `operation_logs` VALUES (30, 1, 'page', 'GET', '[Ljava.lang.Object;@1c598286', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 14, '2025-06-08 06:29:40');
INSERT INTO `operation_logs` VALUES (31, 1, 'page', 'GET', '[Ljava.lang.Object;@4e6b9155', 'Page 2 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 10, '2025-06-08 06:29:41');
INSERT INTO `operation_logs` VALUES (32, 1, 'page', 'GET', '[Ljava.lang.Object;@19137eb4', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 11, '2025-06-08 06:30:54');
INSERT INTO `operation_logs` VALUES (33, 1, 'page', 'GET', '[Ljava.lang.Object;@6bf2bc5b', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 11, '2025-06-08 06:32:07');
INSERT INTO `operation_logs` VALUES (34, 1, 'page', 'GET', '[Ljava.lang.Object;@3490a75c', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 11, '2025-06-08 06:32:08');
INSERT INTO `operation_logs` VALUES (35, 1, 'login', 'POST', '[Ljava.lang.Object;@4cd827c', '{code=20000, data={token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc0OTM2NDM0NiwiZXhwIjoxNzQ5NDUwNzQ2fQ.lGfxT9Wg_hVY1_nPsMsuX62LZxlcuOAeOHk4qDPlqkgov5C_KhJWkexAxaB7MKOucALzlCgssHmgNthLZyyHfg}}', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 89, '2025-06-08 06:32:26');
INSERT INTO `operation_logs` VALUES (36, 1, 'page', 'GET', '[Ljava.lang.Object;@4e954933', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 14, '2025-06-08 06:32:26');
INSERT INTO `operation_logs` VALUES (37, 1, 'getUnboundUsers', 'GET', '[Ljava.lang.Object;@35b836d2', '[User{id=2, username=\'hr001\', email=\'hr001@hrms.com\', enabled=true}, User{id=4, username=\'tester001\', email=\'tester001@hrms.com\', enabled=true}, User{id=7, username=\'asd\', email=\'asd@11.com\', enabled=true}]', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 6, '2025-06-08 06:32:27');
INSERT INTO `operation_logs` VALUES (38, 1, 'create', 'POST', '[Ljava.lang.Object;@12d490f0', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 5, '2025-06-08 06:32:36');
INSERT INTO `operation_logs` VALUES (39, 1, 'page', 'GET', '[Ljava.lang.Object;@3e42a8f8', 'Page 1 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 11, '2025-06-08 06:32:36');
INSERT INTO `operation_logs` VALUES (40, 1, 'page', 'GET', '[Ljava.lang.Object;@6bed139f', 'Page 2 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 8, '2025-06-08 06:32:38');
INSERT INTO `operation_logs` VALUES (41, 1, 'getUnboundUsers', 'GET', '[Ljava.lang.Object;@7a81ee64', '[User{id=2, username=\'hr001\', email=\'hr001@hrms.com\', enabled=true}, User{id=4, username=\'tester001\', email=\'tester001@hrms.com\', enabled=true}, User{id=7, username=\'asd\', email=\'asd@11.com\', enabled=true}]', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 6, '2025-06-08 06:32:40');
INSERT INTO `operation_logs` VALUES (42, 1, 'update', 'PUT', '[Ljava.lang.Object;@48fccef0', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 11, '2025-06-08 06:32:47');
INSERT INTO `operation_logs` VALUES (43, 1, 'page', 'GET', '[Ljava.lang.Object;@7e352d81', 'Page 2 of 2 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 5, '2025-06-08 06:32:47');
INSERT INTO `operation_logs` VALUES (44, 1, 'page', 'GET', '[Ljava.lang.Object;@1dd61a05', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 9, '2025-06-08 06:32:52');
INSERT INTO `operation_logs` VALUES (45, 1, 'page', 'GET', '[Ljava.lang.Object;@79ffc51c', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 10, '2025-06-08 06:33:14');
INSERT INTO `operation_logs` VALUES (46, 1, 'page', 'GET', '[Ljava.lang.Object;@22c0e332', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 7, '2025-06-08 06:33:43');
INSERT INTO `operation_logs` VALUES (47, 1, 'page', 'GET', '[Ljava.lang.Object;@5d4099fc', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 9, '2025-06-08 06:33:50');
INSERT INTO `operation_logs` VALUES (48, 1, 'page', 'GET', '[Ljava.lang.Object;@1011e039', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 11, '2025-06-08 06:34:04');
INSERT INTO `operation_logs` VALUES (49, 1, 'page', 'GET', '[Ljava.lang.Object;@6b1e452d', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 7, '2025-06-08 06:34:30');
INSERT INTO `operation_logs` VALUES (50, 1, 'getApplications', 'GET', '[Ljava.lang.Object;@44c6440a', 'Result(code=20000, message=操作成功, data=Page 1 of 1 containing com.example.hrms.entity.Application instances)', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 5, '2025-06-08 06:35:33');
INSERT INTO `operation_logs` VALUES (51, 1, 'getApplicationStatistics', 'GET', '[Ljava.lang.Object;@3eab6e88', 'Result(code=20000, message=操作成功, data={total=7, approved=5, rejected=2, pending=0})', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 8, '2025-06-08 06:35:33');
INSERT INTO `operation_logs` VALUES (52, 1, 'submitApplication', 'POST', '[Ljava.lang.Object;@4e80cfca', 'Result(code=20000, message=操作成功, data=Application(id=8, applicationType=LEAVE, title=咳嗽, content=咳嗽请假, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=null, status=PENDING, applyTime=2025-06-08T14:35:43.335840, approveTime=null, approveReason=null, priority=HIGH, attachmentPath=null, updatedTime=2025-06-08T14:35:43.335840))', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 7, '2025-06-08 06:35:43');
INSERT INTO `operation_logs` VALUES (53, 1, 'getApplications', 'GET', '[Ljava.lang.Object;@1fb61594', 'Result(code=20000, message=操作成功, data=Page 1 of 1 containing com.example.hrms.entity.Application instances)', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 1, '2025-06-08 06:35:43');
INSERT INTO `operation_logs` VALUES (54, 1, 'getApplicationStatistics', 'GET', '[Ljava.lang.Object;@6e1348d2', 'Result(code=20000, message=操作成功, data={total=8, approved=5, rejected=2, pending=1})', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 4, '2025-06-08 06:35:43');
INSERT INTO `operation_logs` VALUES (55, 1, 'approveApplication', 'PUT', '[Ljava.lang.Object;@7995c29b', 'Result(code=20000, message=操作成功, data=Application(id=8, applicationType=LEAVE, title=咳嗽, content=咳嗽请假, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-08T14:35:43, approveTime=2025-06-08T14:35:51.367048, approveReason=同意, priority=HIGH, attachmentPath=null, updatedTime=2025-06-08T14:35:51.369041500))', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 9, '2025-06-08 06:35:51');
INSERT INTO `operation_logs` VALUES (56, 1, 'getApplications', 'GET', '[Ljava.lang.Object;@1b53f974', 'Result(code=20000, message=操作成功, data=Page 1 of 1 containing com.example.hrms.entity.Application instances)', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 1, '2025-06-08 06:35:51');
INSERT INTO `operation_logs` VALUES (57, 1, 'getApplicationStatistics', 'GET', '[Ljava.lang.Object;@46f679fe', 'Result(code=20000, message=操作成功, data={total=8, approved=6, rejected=2, pending=0})', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 4, '2025-06-08 06:35:51');
INSERT INTO `operation_logs` VALUES (58, 1, 'getMyApplications', 'GET', '[Ljava.lang.Object;@4bcb3692', 'Result(code=20000, message=操作成功, data=[Application(id=1, applicationType=LEAVE, title=123, content=123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=null, status=APPROVED, applyTime=2025-06-07T22:40:31, approveTime=2025-06-07T22:55:09, approveReason=同意, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T22:55:09), Application(id=2, applicationType=OVERTIME, title=123123, content=123123123123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-07T22:43:25, approveTime=2025-06-07T22:56:09, approveReason=ddddd, priority=HIGH, attachmentPath=null, updatedTime=2025-06-07T22:56:09), Application(id=3, applicationType=EQUIPMENT, title=123, content=123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=REJECTED, applyTime=2025-06-07T22:44:19, approveTime=2025-06-07T23:12:27, approveReason=拒绝, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:12:27), Application(id=4, applicationType=OTHER, title=123, content=123123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-07T22:44:27, approveTime=2025-06-07T23:45:11, approveReason=12, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:45:11), Application(id=5, applicationType=EQUIPMENT, title=123, content=123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-07T22:45:22, approveTime=2025-06-07T23:48:09, approveReason=通过aaa, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:48:09), Application(id=6, applicationType=LEAVE, title=3122222, content=12312, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=REJECTED, applyTime=2025-06-07T23:48:23, approveTime=2025-06-07T23:48:27, approveReason=123123123, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:48:27), Application(id=7, applicationType=REIMBURSEMENT, title=顶顶顶顶, content=等等的点点滴滴, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-08T10:57:39, approveTime=2025-06-08T11:09:30, approveReason=通过, priority=URGENT, attachmentPath=null, updatedTime=2025-06-08T11:09:30), Application(id=8, applicationType=LEAVE, title=咳嗽, content=咳嗽请假, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-08T14:35:43, approveTime=2025-06-08T14:35:51, approveReason=同意, priority=HIGH, attachmentPath=null, updatedTime=2025-06-08T14:35:51)])', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 5, '2025-06-08 06:36:01');
INSERT INTO `operation_logs` VALUES (59, 1, 'getPendingApprovals', 'GET', '[Ljava.lang.Object;@3c57cefb', 'Result(code=20000, message=操作成功, data=[])', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 3, '2025-06-08 06:36:04');
INSERT INTO `operation_logs` VALUES (60, 1, 'getApplications', 'GET', '[Ljava.lang.Object;@2228179e', 'Result(code=20000, message=操作成功, data=Page 1 of 1 containing com.example.hrms.entity.Application instances)', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 1, '2025-06-08 06:36:06');
INSERT INTO `operation_logs` VALUES (61, 1, 'getApplicationStatistics', 'GET', '[Ljava.lang.Object;@646d486e', 'Result(code=20000, message=操作成功, data={total=8, approved=6, rejected=2, pending=0})', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 3, '2025-06-08 06:36:06');
INSERT INTO `operation_logs` VALUES (62, 1, 'submitApplication', 'POST', '[Ljava.lang.Object;@7dbe3df2', 'Result(code=20000, message=操作成功, data=Application(id=9, applicationType=LEAVE, title=测试1, content=测试1, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=null, status=PENDING, applyTime=2025-06-08T14:36:16.162880100, approveTime=null, approveReason=null, priority=URGENT, attachmentPath=null, updatedTime=2025-06-08T14:36:16.162880100))', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 5, '2025-06-08 06:36:16');
INSERT INTO `operation_logs` VALUES (63, 1, 'getApplications', 'GET', '[Ljava.lang.Object;@77cc317f', 'Result(code=20000, message=操作成功, data=Page 1 of 1 containing com.example.hrms.entity.Application instances)', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 2, '2025-06-08 06:36:16');
INSERT INTO `operation_logs` VALUES (64, 1, 'getApplicationStatistics', 'GET', '[Ljava.lang.Object;@169c456', 'Result(code=20000, message=操作成功, data={total=9, approved=6, rejected=2, pending=1})', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 6, '2025-06-08 06:36:16');
INSERT INTO `operation_logs` VALUES (65, 1, 'getMyApplications', 'GET', '[Ljava.lang.Object;@287c053f', 'Result(code=20000, message=操作成功, data=[Application(id=1, applicationType=LEAVE, title=123, content=123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=null, status=APPROVED, applyTime=2025-06-07T22:40:31, approveTime=2025-06-07T22:55:09, approveReason=同意, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T22:55:09), Application(id=2, applicationType=OVERTIME, title=123123, content=123123123123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-07T22:43:25, approveTime=2025-06-07T22:56:09, approveReason=ddddd, priority=HIGH, attachmentPath=null, updatedTime=2025-06-07T22:56:09), Application(id=3, applicationType=EQUIPMENT, title=123, content=123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=REJECTED, applyTime=2025-06-07T22:44:19, approveTime=2025-06-07T23:12:27, approveReason=拒绝, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:12:27), Application(id=4, applicationType=OTHER, title=123, content=123123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-07T22:44:27, approveTime=2025-06-07T23:45:11, approveReason=12, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:45:11), Application(id=5, applicationType=EQUIPMENT, title=123, content=123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-07T22:45:22, approveTime=2025-06-07T23:48:09, approveReason=通过aaa, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:48:09), Application(id=6, applicationType=LEAVE, title=3122222, content=12312, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=REJECTED, applyTime=2025-06-07T23:48:23, approveTime=2025-06-07T23:48:27, approveReason=123123123, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:48:27), Application(id=7, applicationType=REIMBURSEMENT, title=顶顶顶顶, content=等等的点点滴滴, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-08T10:57:39, approveTime=2025-06-08T11:09:30, approveReason=通过, priority=URGENT, attachmentPath=null, updatedTime=2025-06-08T11:09:30), Application(id=8, applicationType=LEAVE, title=咳嗽, content=咳嗽请假, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-08T14:35:43, approveTime=2025-06-08T14:35:51, approveReason=同意, priority=HIGH, attachmentPath=null, updatedTime=2025-06-08T14:35:51), Application(id=9, applicationType=LEAVE, title=测试1, content=测试1, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=null, status=PENDING, applyTime=2025-06-08T14:36:16, approveTime=null, approveReason=null, priority=URGENT, attachmentPath=null, updatedTime=2025-06-08T14:36:16)])', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 4, '2025-06-08 06:36:18');
INSERT INTO `operation_logs` VALUES (66, 1, 'getPendingApprovals', 'GET', '[Ljava.lang.Object;@11fda66d', 'Result(code=20000, message=操作成功, data=[Application(id=9, applicationType=LEAVE, title=测试1, content=测试1, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=null, status=PENDING, applyTime=2025-06-08T14:36:16, approveTime=null, approveReason=null, priority=URGENT, attachmentPath=null, updatedTime=2025-06-08T14:36:16)])', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 4, '2025-06-08 06:36:23');
INSERT INTO `operation_logs` VALUES (67, 1, 'approveApplication', 'PUT', '[Ljava.lang.Object;@26ffe30b', 'Result(code=20000, message=操作成功, data=Application(id=9, applicationType=LEAVE, title=测试1, content=测试1, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=REJECTED, applyTime=2025-06-08T14:36:16, approveTime=2025-06-08T14:36:30.444750500, approveReason=不可以, priority=URGENT, attachmentPath=null, updatedTime=2025-06-08T14:36:30.446748400))', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 8, '2025-06-08 06:36:30');
INSERT INTO `operation_logs` VALUES (68, 1, 'getPendingApprovals', 'GET', '[Ljava.lang.Object;@2e92b8ad', 'Result(code=20000, message=操作成功, data=[])', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 4, '2025-06-08 06:36:30');
INSERT INTO `operation_logs` VALUES (69, 1, 'getMyApplications', 'GET', '[Ljava.lang.Object;@727fdd7', 'Result(code=20000, message=操作成功, data=[Application(id=1, applicationType=LEAVE, title=123, content=123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=null, status=APPROVED, applyTime=2025-06-07T22:40:31, approveTime=2025-06-07T22:55:09, approveReason=同意, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T22:55:09), Application(id=2, applicationType=OVERTIME, title=123123, content=123123123123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-07T22:43:25, approveTime=2025-06-07T22:56:09, approveReason=ddddd, priority=HIGH, attachmentPath=null, updatedTime=2025-06-07T22:56:09), Application(id=3, applicationType=EQUIPMENT, title=123, content=123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=REJECTED, applyTime=2025-06-07T22:44:19, approveTime=2025-06-07T23:12:27, approveReason=拒绝, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:12:27), Application(id=4, applicationType=OTHER, title=123, content=123123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-07T22:44:27, approveTime=2025-06-07T23:45:11, approveReason=12, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:45:11), Application(id=5, applicationType=EQUIPMENT, title=123, content=123, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-07T22:45:22, approveTime=2025-06-07T23:48:09, approveReason=通过aaa, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:48:09), Application(id=6, applicationType=LEAVE, title=3122222, content=12312, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=REJECTED, applyTime=2025-06-07T23:48:23, approveTime=2025-06-07T23:48:27, approveReason=123123123, priority=NORMAL, attachmentPath=null, updatedTime=2025-06-07T23:48:27), Application(id=7, applicationType=REIMBURSEMENT, title=顶顶顶顶, content=等等的点点滴滴, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-08T10:57:39, approveTime=2025-06-08T11:09:30, approveReason=通过, priority=URGENT, attachmentPath=null, updatedTime=2025-06-08T11:09:30), Application(id=8, applicationType=LEAVE, title=咳嗽, content=咳嗽请假, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=APPROVED, applyTime=2025-06-08T14:35:43, approveTime=2025-06-08T14:35:51, approveReason=同意, priority=HIGH, attachmentPath=null, updatedTime=2025-06-08T14:35:51), Application(id=9, applicationType=LEAVE, title=测试1, content=测试1, applicant=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, currentApprover=Employee{id=8, name=\'离宿舍\', email=\'1016608214@qq.com\', position=\'123\', employeeNumber=\'null\'}, status=REJECTED, applyTime=2025-06-08T14:36:16, approveTime=2025-06-08T14:36:30, approveReason=不可以, priority=URGENT, attachmentPath=null, updatedTime=2025-06-08T14:36:30)])', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 3, '2025-06-08 06:36:32');
INSERT INTO `operation_logs` VALUES (70, 1, 'page', 'GET', '[Ljava.lang.Object;@3f8dce6f', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 11, '2025-06-08 06:36:40');
INSERT INTO `operation_logs` VALUES (71, 1, 'page', 'GET', '[Ljava.lang.Object;@2a799a31', 'Page 1 of 1 containing com.example.hrms.dto.EmployeeDTO instances', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 Edg/137.0.0.0', 'SUCCESS', 5, '2025-06-08 06:38:18');

-- ----------------------------
-- Table structure for outsourcing_tests
-- ----------------------------
DROP TABLE IF EXISTS `outsourcing_tests`;
CREATE TABLE `outsourcing_tests`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '??',
  `tester_id` bigint(20) NULL DEFAULT NULL COMMENT '????ID',
  `creator_id` bigint(20) NOT NULL COMMENT '???ID',
  `start_time` datetime NULL DEFAULT NULL COMMENT '????',
  `end_time` datetime NULL DEFAULT NULL COMMENT '????',
  `status` enum('CANCELLED','COMPLETED','IN_PROGRESS','PENDING','REVIEWED','SUBMITTED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '????',
  `priority` enum('LOW','MEDIUM','HIGH','URGENT') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'MEDIUM' COMMENT '???',
  `estimated_hours` int(11) NULL DEFAULT NULL COMMENT '????',
  `actual_hours` int(11) NULL DEFAULT NULL COMMENT '????',
  `score` int(11) NULL DEFAULT NULL COMMENT '????(0-100)',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_outsourcing_tests_tester_id`(`tester_id`) USING BTREE,
  INDEX `idx_outsourcing_tests_creator_id`(`creator_id`) USING BTREE,
  INDEX `idx_outsourcing_tests_status`(`status`) USING BTREE,
  INDEX `idx_outsourcing_tests_priority`(`priority`) USING BTREE,
  INDEX `idx_outsourcing_tests_created_time`(`created_time`) USING BTREE,
  CONSTRAINT `outsourcing_tests_ibfk_1` FOREIGN KEY (`tester_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `outsourcing_tests_ibfk_2` FOREIGN KEY (`creator_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '?????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of outsourcing_tests
-- ----------------------------
INSERT INTO `outsourcing_tests` VALUES (4, '测试bug', '测试bug描述', 11, 11, '2025-06-05 16:00:00', '2025-06-26 16:00:00', 'PENDING', NULL, 'MEDIUM', 8, NULL, NULL, '2025-06-07 08:55:35', '2025-06-07 08:55:35');
INSERT INTO `outsourcing_tests` VALUES (5, '123', '123', 9, 9, '2025-06-20 16:00:00', '2025-06-24 16:00:00', 'PENDING', NULL, 'MEDIUM', 8, NULL, NULL, '2025-06-07 08:56:58', '2025-06-07 08:56:58');
INSERT INTO `outsourcing_tests` VALUES (18, '123', '1231', 8, 8, NULL, NULL, 'REVIEWED', 'eeeeeeeeee\n审核意见: eeeeeeeeee', 'MEDIUM', 8, 8, 98, '2025-06-07 12:03:06', '2025-06-07 13:04:51');
INSERT INTO `outsourcing_tests` VALUES (19, '123123', '12312312', 9, 9, NULL, NULL, 'IN_PROGRESS', NULL, 'MEDIUM', 8, NULL, NULL, '2025-06-07 12:03:13', '2025-06-07 12:03:13');
INSERT INTO `outsourcing_tests` VALUES (20, '321', '231312', 8, 8, '2025-06-19 16:00:00', '2025-06-26 16:00:00', 'REVIEWED', '请求\n审核意见: dqeq', 'MEDIUM', 8, 8, 2, '2025-06-07 12:03:23', '2025-06-07 13:11:13');
INSERT INTO `outsourcing_tests` VALUES (23, '123', '123', 8, 8, NULL, NULL, 'REVIEWED', '不不不\n审核意见: 不不不', 'MEDIUM', 8, 8, 90, '2025-06-07 12:09:51', '2025-06-07 13:07:57');
INSERT INTO `outsourcing_tests` VALUES (24, '123', '123123', 8, 8, NULL, NULL, 'REVIEWED', '测试结果\n审核意见: 审核意见', 'MEDIUM', 8, 8, 75, '2025-06-07 12:09:56', '2025-06-07 13:10:11');
INSERT INTO `outsourcing_tests` VALUES (25, '123', '123123', 8, 8, NULL, NULL, 'REVIEWED', '123\n审核意见: rrr', 'MEDIUM', 8, 8, 100, '2025-06-07 12:10:01', '2025-06-07 13:11:00');
INSERT INTO `outsourcing_tests` VALUES (26, '123', '12312', 8, 8, NULL, NULL, 'REVIEWED', '1\n审核意见: qqq', 'MEDIUM', 8, 8, 12, '2025-07-07 12:10:12', '2025-06-07 22:04:27');
INSERT INTO `outsourcing_tests` VALUES (27, '测试任务', '测试任务', 7, 7, '2025-06-07 16:00:00', '2025-06-10 16:00:00', 'IN_PROGRESS', NULL, 'HIGH', 10, NULL, NULL, '2025-06-08 06:33:33', '2025-06-08 06:33:33');
INSERT INTO `outsourcing_tests` VALUES (28, '测试1', '测试1', 8, 8, '2025-06-02 16:00:00', '2025-06-17 16:00:00', 'REVIEWED', '测试完成\n审核意见: 可以弄得不错', 'MEDIUM', 8, 8, 90, '2025-06-08 06:34:20', '2025-06-08 06:35:18');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `resource` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '??',
  `action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '??',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '??',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '???' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------
INSERT INTO `permissions` VALUES (1, 'USER_READ', '用户', '用户读取', '用户读取', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (2, 'USER_WRITE', '用户', '用户写入', '用户写入', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (3, 'USER_DELETE', '用户', '用户删除', '用户删除', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (4, 'USER_ASSIGN_ROLE', '用户', '用户分配角色', '用户分配角色', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (5, 'EMPLOYEE_READ', '员工', '员工读取', '员工读取', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (6, 'EMPLOYEE_WRITE', '员工', '员工写入', '员工写入', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (7, 'EMPLOYEE_DELETE', '员工', '员工删除', '员工删除', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (8, 'EMPLOYEE_IMPORT', '员工', '员工导入', '员工导入', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (9, 'EMPLOYEE_EXPORT', '员工', '员工导出', '员工导出', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (10, 'DEPT_READ', '部门', '部门读取', '部门读取', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (11, 'DEPT_WRITE', '部门', '部门写入', '部门写入', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (12, 'DEPT_DELETE', '部门', '部门删除', '部门删除', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (13, 'TEST_READ', '测试', '测试读取', '测试读取', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (14, 'TEST_WRITE', '测试', '测试写入', '测试写入', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (15, 'TEST_DELETE', '测试', '测试删除', '测试删除', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (16, 'TEST_ASSIGN', '测试', '测试分配', '测试分配', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (17, 'TEST_REVIEW', '测试', '测试审核', '测试审核', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (18, 'TEST_EXECUTE', '测试', '测试执行', '测试执行', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (19, 'APPLICATION_READ', '应用', '应用读取', '应用读取', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (20, 'APPLICATION_WRITE', '应用', '应用写入', '应用写入', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (21, 'APPLICATION_APPROVE', '应用', '应用审批', '应用审批', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (22, 'APPLICATION_DELETE', '应用', '应用删除', '应用删除', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (23, 'APPLICATION_FORWARD', '应用', '应用转发', '应用转发', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (24, 'SALARY_READ', '薪资', '薪资读取', '薪资读取', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (25, 'SALARY_WRITE', '薪资', '薪资写入', '薪资写入', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (26, 'SALARY_DELETE', '薪资', '薪资删除', '薪资删除', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (27, 'SALARY_EXPORT', '薪资', '薪资导出', '薪资导出', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (28, 'ROLE_READ', '角色', '角色读取', '角色读取', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (29, 'ROLE_WRITE', '角色', '角色写入', '角色写入', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (30, 'ROLE_DELETE', '角色', '角色删除', '角色删除', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (31, 'PERMISSION_READ', '权限', '权限读取', '权限读取', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (32, 'PERMISSION_WRITE', '权限', '权限写入', '权限写入', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (33, 'LOG_READ', '日志', '日志读取', '日志读取', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (34, 'LOG_EXPORT', '日志', '日志导出', '日志导出', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (35, 'LOG_DELETE', '日志', '日志删除', '日志删除', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (36, 'SYSTEM_MONITOR', '系统监控', '系统监控', '系统监控', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (37, 'SYSTEM_CONFIG', '系统配置', '系统配置', '系统配置', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (38, 'REPORT_VIEW', '报告查看', '报告查看', '报告查看', '2025-06-01 01:46:25');
INSERT INTO `permissions` VALUES (39, 'REPORT_EXPORT', '报告导出', '报告导出', '报告导出', '2025-06-01 01:46:25');

-- ----------------------------
-- Table structure for role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE `role_permissions`  (
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE,
  INDEX `permission_id`(`permission_id`) USING BTREE,
  CONSTRAINT `role_permissions_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `role_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '???????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permissions
-- ----------------------------
INSERT INTO `role_permissions` VALUES (1, 1);
INSERT INTO `role_permissions` VALUES (2, 1);
INSERT INTO `role_permissions` VALUES (3, 1);
INSERT INTO `role_permissions` VALUES (1, 2);
INSERT INTO `role_permissions` VALUES (2, 2);
INSERT INTO `role_permissions` VALUES (3, 2);
INSERT INTO `role_permissions` VALUES (1, 3);
INSERT INTO `role_permissions` VALUES (2, 3);
INSERT INTO `role_permissions` VALUES (3, 3);
INSERT INTO `role_permissions` VALUES (1, 4);
INSERT INTO `role_permissions` VALUES (2, 4);
INSERT INTO `role_permissions` VALUES (3, 4);
INSERT INTO `role_permissions` VALUES (1, 5);
INSERT INTO `role_permissions` VALUES (2, 5);
INSERT INTO `role_permissions` VALUES (3, 5);
INSERT INTO `role_permissions` VALUES (5, 5);
INSERT INTO `role_permissions` VALUES (6, 5);
INSERT INTO `role_permissions` VALUES (1, 6);
INSERT INTO `role_permissions` VALUES (2, 6);
INSERT INTO `role_permissions` VALUES (3, 6);
INSERT INTO `role_permissions` VALUES (6, 6);
INSERT INTO `role_permissions` VALUES (1, 7);
INSERT INTO `role_permissions` VALUES (2, 7);
INSERT INTO `role_permissions` VALUES (3, 7);
INSERT INTO `role_permissions` VALUES (1, 8);
INSERT INTO `role_permissions` VALUES (2, 8);
INSERT INTO `role_permissions` VALUES (3, 8);
INSERT INTO `role_permissions` VALUES (1, 9);
INSERT INTO `role_permissions` VALUES (2, 9);
INSERT INTO `role_permissions` VALUES (3, 9);
INSERT INTO `role_permissions` VALUES (1, 10);
INSERT INTO `role_permissions` VALUES (2, 10);
INSERT INTO `role_permissions` VALUES (3, 10);
INSERT INTO `role_permissions` VALUES (6, 10);
INSERT INTO `role_permissions` VALUES (1, 11);
INSERT INTO `role_permissions` VALUES (2, 11);
INSERT INTO `role_permissions` VALUES (3, 11);
INSERT INTO `role_permissions` VALUES (1, 12);
INSERT INTO `role_permissions` VALUES (2, 12);
INSERT INTO `role_permissions` VALUES (3, 12);
INSERT INTO `role_permissions` VALUES (1, 13);
INSERT INTO `role_permissions` VALUES (2, 13);
INSERT INTO `role_permissions` VALUES (3, 13);
INSERT INTO `role_permissions` VALUES (4, 13);
INSERT INTO `role_permissions` VALUES (1, 14);
INSERT INTO `role_permissions` VALUES (2, 14);
INSERT INTO `role_permissions` VALUES (3, 14);
INSERT INTO `role_permissions` VALUES (1, 15);
INSERT INTO `role_permissions` VALUES (2, 15);
INSERT INTO `role_permissions` VALUES (3, 15);
INSERT INTO `role_permissions` VALUES (1, 16);
INSERT INTO `role_permissions` VALUES (2, 16);
INSERT INTO `role_permissions` VALUES (3, 16);
INSERT INTO `role_permissions` VALUES (1, 17);
INSERT INTO `role_permissions` VALUES (2, 17);
INSERT INTO `role_permissions` VALUES (3, 17);
INSERT INTO `role_permissions` VALUES (1, 18);
INSERT INTO `role_permissions` VALUES (2, 18);
INSERT INTO `role_permissions` VALUES (3, 18);
INSERT INTO `role_permissions` VALUES (4, 18);
INSERT INTO `role_permissions` VALUES (1, 19);
INSERT INTO `role_permissions` VALUES (2, 19);
INSERT INTO `role_permissions` VALUES (3, 19);
INSERT INTO `role_permissions` VALUES (4, 19);
INSERT INTO `role_permissions` VALUES (5, 19);
INSERT INTO `role_permissions` VALUES (6, 19);
INSERT INTO `role_permissions` VALUES (1, 20);
INSERT INTO `role_permissions` VALUES (2, 20);
INSERT INTO `role_permissions` VALUES (3, 20);
INSERT INTO `role_permissions` VALUES (4, 20);
INSERT INTO `role_permissions` VALUES (5, 20);
INSERT INTO `role_permissions` VALUES (1, 21);
INSERT INTO `role_permissions` VALUES (2, 21);
INSERT INTO `role_permissions` VALUES (3, 21);
INSERT INTO `role_permissions` VALUES (6, 21);
INSERT INTO `role_permissions` VALUES (1, 22);
INSERT INTO `role_permissions` VALUES (2, 22);
INSERT INTO `role_permissions` VALUES (3, 22);
INSERT INTO `role_permissions` VALUES (1, 23);
INSERT INTO `role_permissions` VALUES (2, 23);
INSERT INTO `role_permissions` VALUES (3, 23);
INSERT INTO `role_permissions` VALUES (6, 23);
INSERT INTO `role_permissions` VALUES (1, 24);
INSERT INTO `role_permissions` VALUES (2, 24);
INSERT INTO `role_permissions` VALUES (3, 24);
INSERT INTO `role_permissions` VALUES (1, 25);
INSERT INTO `role_permissions` VALUES (2, 25);
INSERT INTO `role_permissions` VALUES (3, 25);
INSERT INTO `role_permissions` VALUES (1, 26);
INSERT INTO `role_permissions` VALUES (2, 26);
INSERT INTO `role_permissions` VALUES (3, 26);
INSERT INTO `role_permissions` VALUES (1, 27);
INSERT INTO `role_permissions` VALUES (2, 27);
INSERT INTO `role_permissions` VALUES (3, 27);
INSERT INTO `role_permissions` VALUES (1, 28);
INSERT INTO `role_permissions` VALUES (2, 28);
INSERT INTO `role_permissions` VALUES (3, 28);
INSERT INTO `role_permissions` VALUES (1, 29);
INSERT INTO `role_permissions` VALUES (2, 29);
INSERT INTO `role_permissions` VALUES (3, 29);
INSERT INTO `role_permissions` VALUES (1, 30);
INSERT INTO `role_permissions` VALUES (2, 30);
INSERT INTO `role_permissions` VALUES (3, 30);
INSERT INTO `role_permissions` VALUES (1, 31);
INSERT INTO `role_permissions` VALUES (2, 31);
INSERT INTO `role_permissions` VALUES (3, 31);
INSERT INTO `role_permissions` VALUES (1, 32);
INSERT INTO `role_permissions` VALUES (2, 32);
INSERT INTO `role_permissions` VALUES (3, 32);
INSERT INTO `role_permissions` VALUES (1, 33);
INSERT INTO `role_permissions` VALUES (2, 33);
INSERT INTO `role_permissions` VALUES (3, 33);
INSERT INTO `role_permissions` VALUES (1, 34);
INSERT INTO `role_permissions` VALUES (2, 34);
INSERT INTO `role_permissions` VALUES (3, 34);
INSERT INTO `role_permissions` VALUES (1, 35);
INSERT INTO `role_permissions` VALUES (2, 35);
INSERT INTO `role_permissions` VALUES (3, 35);
INSERT INTO `role_permissions` VALUES (1, 36);
INSERT INTO `role_permissions` VALUES (2, 36);
INSERT INTO `role_permissions` VALUES (3, 36);
INSERT INTO `role_permissions` VALUES (9, 36);
INSERT INTO `role_permissions` VALUES (1, 37);
INSERT INTO `role_permissions` VALUES (2, 37);
INSERT INTO `role_permissions` VALUES (3, 37);
INSERT INTO `role_permissions` VALUES (1, 38);
INSERT INTO `role_permissions` VALUES (2, 38);
INSERT INTO `role_permissions` VALUES (3, 38);
INSERT INTO `role_permissions` VALUES (6, 38);
INSERT INTO `role_permissions` VALUES (1, 39);
INSERT INTO `role_permissions` VALUES (2, 39);
INSERT INTO `role_permissions` VALUES (3, 39);

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `resource` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '??',
  `action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '??',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '??',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '????',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '???' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1, 'ADMIN', '', '', '超管', '2025-06-01 02:02:37', 1);
INSERT INTO `roles` VALUES (2, 'HR', '', '', '人事', '2025-06-01 02:02:37', 1);
INSERT INTO `roles` VALUES (3, 'PROJECT_MANAGER', '', '', '管理员', '2025-06-01 02:02:37', 1);
INSERT INTO `roles` VALUES (4, 'TESTER', '', '', 'TESTER', '2025-06-01 02:02:37', 1);
INSERT INTO `roles` VALUES (5, 'EMPLOYEE', '', '', 'EMPLOYEE', '2025-06-01 02:02:37', 1);
INSERT INTO `roles` VALUES (6, 'DEPT_MANAGER', '', '', 'DEPT_MANAGER', '2025-06-01 02:02:37', 1);
INSERT INTO `roles` VALUES (9, '111', NULL, NULL, '111', '2025-06-08 04:52:19', 1);

-- ----------------------------
-- Table structure for salaries
-- ----------------------------
DROP TABLE IF EXISTS `salaries`;
CREATE TABLE `salaries`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `employee_id` bigint(20) NOT NULL COMMENT '??ID',
  `pay_date` date NOT NULL COMMENT '????',
  `amount` decimal(10, 2) NOT NULL COMMENT '??',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '??',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_salaries_employee_id`(`employee_id`) USING BTREE,
  INDEX `idx_salaries_pay_date`(`pay_date`) USING BTREE,
  CONSTRAINT `salaries_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '???' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of salaries
-- ----------------------------

-- ----------------------------
-- Table structure for salary
-- ----------------------------
DROP TABLE IF EXISTS `salary`;
CREATE TABLE `salary`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(38, 2) NULL DEFAULT NULL,
  `pay_date` date NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `employee_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK8shr4to2ct7gci2vauwolmrlk`(`employee_id`) USING BTREE,
  CONSTRAINT `FK8shr4to2ct7gci2vauwolmrlk` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of salary
-- ----------------------------
INSERT INTO `salary` VALUES (2, 111111.00, '2025-06-19', '111111', 8);
INSERT INTO `salary` VALUES (5, 9500.00, '2025-06-02', '干的不错', 8);

-- ----------------------------
-- Table structure for user_roles
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`  (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `user_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `user_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '???????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_roles
-- ----------------------------
INSERT INTO `user_roles` VALUES (1, 1);
INSERT INTO `user_roles` VALUES (5, 5);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '???',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '??',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '??',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '????',
  `account_non_expired` tinyint(1) NOT NULL DEFAULT 1 COMMENT '???????',
  `account_non_locked` tinyint(1) NOT NULL DEFAULT 1 COMMENT '???????',
  `credentials_non_expired` tinyint(1) NOT NULL DEFAULT 1 COMMENT '???????',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '??????',
  `last_login_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  INDEX `idx_users_username`(`username`) USING BTREE,
  INDEX `idx_users_email`(`email`) USING BTREE,
  INDEX `idx_users_enabled`(`enabled`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '???' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', '$2a$10$7iWWwD1GuT8byKDfj7grdOkqhhw.KQG.pKIrryb.3w32XAqyG.ggC', 'admin@hrms.com', 1, 1, 1, 1, NULL, NULL, '2025-06-01 02:03:09', '2025-05-31 17:02:27');
INSERT INTO `users` VALUES (2, 'hr001', '$2a$10$2CPJ73A4n5oP2eH5iURVbeLn2r/09qlRjo.ihDJdGEjQSBq7TCYzS', 'hr001@hrms.com', 1, 1, 1, 1, NULL, NULL, '2025-06-01 02:03:09', '2025-06-08 06:32:40');
INSERT INTO `users` VALUES (3, 'pm001', '$2a$10$AsR1S1XP8q99O1hCkGJzWuymsU0N7QkGLwgN5QcvE07zjudPBq0.G', 'pm001@hrms.com', 1, 1, 1, 1, NULL, NULL, '2025-06-01 02:03:09', '2025-06-08 06:29:27');
INSERT INTO `users` VALUES (4, 'tester001', '$2a$10$HPOtfOenWVIElCGNLh8KV.FNKo1uA/ihhwa6fAL.LowhjpLBUGiBy', 'tester001@hrms.com', 1, 1, 1, 1, NULL, NULL, '2025-06-01 02:03:09', '2025-06-08 06:32:40');
INSERT INTO `users` VALUES (5, 'emp001', '$2a$10$c3/YmxLXd8ElxygipC0esec/5NzmwFSiH2cRk/pINd9e30HJ1M.bC', 'emp001@hrms.com', 1, 1, 1, 1, NULL, NULL, '2025-06-01 02:03:09', '2025-06-08 04:09:05');
INSERT INTO `users` VALUES (7, 'asd', 'qweqwe', 'asd@11.com', 1, 1, 1, 1, NULL, NULL, '2025-06-08 04:18:56', '2025-06-08 06:32:40');

SET FOREIGN_KEY_CHECKS = 1;
