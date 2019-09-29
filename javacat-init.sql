/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : javacat-public

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2019-09-27 15:16:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for b_employee
-- ----------------------------
DROP TABLE IF EXISTS `b_employee`;
CREATE TABLE `b_employee` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `description` text COMMENT '个人简介',
  `enabled` varchar(255) DEFAULT NULL COMMENT '是否可用',
  `name` varchar(100) DEFAULT NULL COMMENT '名字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of b_employee
-- ----------------------------
INSERT INTO `b_employee` VALUES ('8a80813d6d714f99016d71516d750001', '0', '2019-09-27 14:03:45', 'admin', '0', 'admin', '2019-09-27 14:03:45', null, '11', '2019-09-27 00:00:00', '的沙发斯蒂芬', null, '张三');

-- ----------------------------
-- Table structure for b_item
-- ----------------------------
DROP TABLE IF EXISTS `b_item`;
CREATE TABLE `b_item` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `value` varchar(1000) DEFAULT NULL COMMENT '值',
  `obj_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKACA720D018C76FCB` (`obj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of b_item
-- ----------------------------

-- ----------------------------
-- Table structure for sys_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_api`;
CREATE TABLE `sys_api` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  `bz` text,
  `req_example` text,
  `res_example` text,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_api
-- ----------------------------

-- ----------------------------
-- Table structure for sys_api_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_api_detail`;
CREATE TABLE `sys_api_detail` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `req_example` text,
  `res_example` text,
  `url` varchar(255) DEFAULT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  `bz` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_api_detail
-- ----------------------------

-- ----------------------------
-- Table structure for sys_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `area_id` varchar(255) DEFAULT NULL,
  `area_name` varchar(255) DEFAULT NULL,
  `if_handle` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `parent_id` varchar(255) DEFAULT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_area
-- ----------------------------

-- ----------------------------
-- Table structure for sys_auto
-- ----------------------------
DROP TABLE IF EXISTS `sys_auto`;
CREATE TABLE `sys_auto` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `age` int(11) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `ifMarried` varchar(255) DEFAULT NULL,
  `introduce` text,
  `name` varchar(255) NOT NULL,
  `upState` int(11) DEFAULT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  `if_married` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_auto
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `config_key` varchar(50) DEFAULT NULL,
  `config_value` text,
  `description` varchar(200) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES ('8a80813d6b686622016b686a7f780000', '0', '2019-06-18 10:28:58', 'admin', 'admin', '2019-06-18 10:28:58', 'domain_server_ip', '127.0.0.1', '', '', '0', null);
INSERT INTO `sys_config` VALUES ('8a80813d6b686622016b686aa2500001', '0', '2019-06-18 10:29:07', 'admin', 'admin', '2019-06-18 10:29:07', 'domain_postfix', '@mutou888.com', '', '', '0', null);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `bz` varchar(255) DEFAULT NULL COMMENT '备注',
  `dict_key` varchar(100) DEFAULT NULL COMMENT '键',
  `dict_value` varchar(100) DEFAULT NULL COMMENT '值',
  `company_id` varchar(100) DEFAULT NULL COMMENT '公司id',
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  `file_hash` varchar(255) DEFAULT NULL,
  `file_key` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_size` int(11) DEFAULT NULL,
  `local_file_path` varchar(255) DEFAULT NULL,
  `local_file_path_prefix` varchar(255) DEFAULT NULL,
  `mime_type` varchar(255) DEFAULT NULL,
  `qiniu_file_bucket` varchar(255) DEFAULT NULL,
  `qiniu_file_path` varchar(255) DEFAULT NULL,
  `seq` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_file
-- ----------------------------

-- ----------------------------
-- Table structure for sys_file_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_relation`;
CREATE TABLE `sys_file_relation` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  `file_id` varchar(255) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `obj_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_file_relation
-- ----------------------------

-- ----------------------------
-- Table structure for sys_import_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_import_record`;
CREATE TABLE `sys_import_record` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `all_count` int(11) DEFAULT NULL,
  `fail_count` int(11) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) DEFAULT NULL,
  `import_status` varchar(100) DEFAULT NULL COMMENT '导入状态',
  `import_type` varchar(255) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '数据名称',
  `result_file_url` varchar(255) DEFAULT NULL,
  `success_count` int(11) DEFAULT NULL,
  `result` varchar(255) DEFAULT NULL,
  `company_id` varchar(100) DEFAULT NULL COMMENT '公司id',
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_import_record
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  `action` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `obj_id` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `detail` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('8a80813d6d714b6b016d714b91a40000', '0', '2019-09-27 13:57:21', 'admin', '0', 'admin', '2019-09-27 13:57:21', null, '登录', '登录', '297e91895a0eb90b015a0eba435f0000', '系统', null);
INSERT INTO `sys_log` VALUES ('8a80813d6d714f99016d714fc0e40000', '0', '2019-09-27 14:01:55', 'admin', '0', 'admin', '2019-09-27 14:01:55', null, '登录', '登录', '297e91895a0eb90b015a0eba435f0000', '系统', null);
INSERT INTO `sys_log` VALUES ('8a80813d6d717455016d71755ca20000', '0', '2019-09-27 14:43:00', 'admin', '0', 'admin', '2019-09-27 14:43:00', null, '登录', '登录', '297e91895a0eb90b015a0eba435f0000', '系统', null);
INSERT INTO `sys_log` VALUES ('8a80813d6d717455016d71764cd50001', '0', '2019-09-27 14:44:01', 'admin', '0', 'admin', '2019-09-27 14:44:01', null, '登录', '登录', '297e91895a0eb90b015a0eba435f0000', '系统', null);
INSERT INTO `sys_log` VALUES ('8a80813d6d718d4c016d718dbadc0000', '0', '2019-09-27 15:09:37', 'admin', '0', 'admin', '2019-09-27 15:09:37', null, '登录', '登录', '297e91895a0eb90b015a0eba435f0000', '系统', null);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `parent_id` varchar(255) NOT NULL,
  `seq` int(11) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('297e02655b749df6015b74a15e900000', '2', '2017-04-16 10:39:44', 'admin', 'admin', '2017-06-03 09:08:48', 'fa fa-tachometer', '控制台', '0', '999', '/busi/home', '0', null);
INSERT INTO `sys_menu` VALUES ('297e91895a130801015a130a9c590000', '2', '2017-02-06 18:49:07', 'admin', '', '2017-02-06 21:21:08', 'fa fa-desktop', '系统管理', '0', '1', '', '0', null);
INSERT INTO `sys_menu` VALUES ('297e91895a138387015a1386475d0000', '3', '2017-02-06 21:04:12', 'admin', 'admin', '2019-06-18 10:15:29', 'fa fa-user', '用户管理', '297e91895a130801015a130a9c590000', '20', '/sys/user/home', '0', null);
INSERT INTO `sys_menu` VALUES ('297e91895a138387015a138a3df70001', '3', '2017-02-06 21:08:31', 'admin', 'admin', '2019-06-18 10:15:40', 'fa fa-list-ul', '菜单管理', '297e91895a130801015a130a9c590000', '40', '/sys/menu/home', '0', null);
INSERT INTO `sys_menu` VALUES ('297e91895a138387015a138a8d930002', '5', '2017-02-06 21:08:52', 'admin', 'admin', '2019-06-18 10:15:35', 'fa fa-users', '角色管理', '297e91895a130801015a130a9c590000', '30', '/sys/role/home', '0', null);
INSERT INTO `sys_menu` VALUES ('8a80813d6d714b6b016d714cc6400001', '0', '2019-09-27 13:58:40', 'admin', 'admin', '2019-09-27 13:58:40', 'fa fa-user', '增删改查演示', '0', '888', '/busi/employee/home', '0', null);

-- ----------------------------
-- Table structure for sys_msg
-- ----------------------------
DROP TABLE IF EXISTS `sys_msg`;
CREATE TABLE `sys_msg` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `content` text NOT NULL,
  `handle_action` varchar(255) NOT NULL,
  `obj_id` varchar(255) NOT NULL,
  `read_state` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_msg
-- ----------------------------
INSERT INTO `sys_msg` VALUES ('8a80813d6d714f99016d716456a80004', '0', '2019-09-27 14:24:24', 'admin', 'admin', '2019-09-27 14:24:24', '您的账号', '选择权限', '8a80813d6b6485d7016b6485e67b0000', 'WD', '', '普通用户', '0', null);
INSERT INTO `sys_msg` VALUES ('8a80813d6d714f99016d716472c7000a', '0', '2019-09-27 14:24:31', 'admin', 'admin', '2019-09-27 14:24:31', '您的账号减少权限：保存,添加,查看,查看详细信息,配置菜单,分页查询,编辑,编辑,选择角色,删除,分页查询,添加,配置权限,添加,删除,分页查询,', '选择权限', '402880e6642811b501642811ca610001', 'WD', '', '平台管理员', '0', null);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `role_key` varchar(255) NOT NULL,
  `role_name` varchar(255) NOT NULL,
  `data_level` varchar(255) DEFAULT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  `device_types` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('402880e6642811b501642811ca610001', '1', '2018-07-01 23:16:52', 'system', 'admin', '2018-06-23 21:23:37', 'ROLE_PLATFORM', '平台管理员', 'ALL', '0', null, null);
INSERT INTO `sys_role` VALUES ('402880e664282031016428204a470000', '1', '2018-06-22 23:32:42', 'system', 'admin', '2018-06-22 23:41:57', 'ROLE_ROOT', '系统管理员', 'ALL', '0', null, null);
INSERT INTO `sys_role` VALUES ('8a80813d6b6485d7016b6485e67b0000', '8', '2019-06-17 16:20:25', 'system', 'admin', '2019-09-27 14:24:24', 'COMMON', '普通用户', 'ALL', '0', null, null);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `menu_id` varchar(255) DEFAULT NULL,
  `role_id` varchar(255) DEFAULT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714b6b016d714ce2720002', '0', '2019-09-27 13:58:47', 'admin', 'admin', '2019-09-27 13:58:47', '297e02655b749df6015b74a15e900000', '402880e664282031016428204a470000', '0', null);
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714b6b016d714ce2720003', '0', '2019-09-27 13:58:47', 'admin', 'admin', '2019-09-27 13:58:47', '8a80813d6d714b6b016d714cc6400001', '402880e664282031016428204a470000', '0', null);
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714b6b016d714ce2720004', '0', '2019-09-27 13:58:47', 'admin', 'admin', '2019-09-27 13:58:47', '297e91895a130801015a130a9c590000', '402880e664282031016428204a470000', '0', null);
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714b6b016d714ce2720005', '0', '2019-09-27 13:58:47', 'admin', 'admin', '2019-09-27 13:58:47', '297e91895a138387015a138a3df70001', '402880e664282031016428204a470000', '0', null);
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714b6b016d714ce2730006', '0', '2019-09-27 13:58:47', 'admin', 'admin', '2019-09-27 13:58:47', '297e91895a138387015a138a8d930002', '402880e664282031016428204a470000', '0', null);
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714b6b016d714ce2730007', '0', '2019-09-27 13:58:47', 'admin', 'admin', '2019-09-27 13:58:47', '297e91895a138387015a1386475d0000', '402880e664282031016428204a470000', '0', null);
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714b6b016d714ce2730008', '0', '2019-09-27 13:58:47', 'admin', 'admin', '2019-09-27 13:58:47', '', '402880e664282031016428204a470000', '0', null);
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714f99016d7164725b0005', '0', '2019-09-27 14:24:31', 'admin', 'admin', '2019-09-27 14:24:31', '8a80813d6d714b6b016d714cc6400001', '402880e6642811b501642811ca610001', '0', null);
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714f99016d7164725c0006', '0', '2019-09-27 14:24:31', 'admin', 'admin', '2019-09-27 14:24:31', '297e91895a130801015a130a9c590000', '402880e6642811b501642811ca610001', '0', null);
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714f99016d7164725c0007', '0', '2019-09-27 14:24:31', 'admin', 'admin', '2019-09-27 14:24:31', '297e91895a138387015a138a8d930002', '402880e6642811b501642811ca610001', '0', null);
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714f99016d7164725c0008', '0', '2019-09-27 14:24:31', 'admin', 'admin', '2019-09-27 14:24:31', '297e91895a138387015a1386475d0000', '402880e6642811b501642811ca610001', '0', null);
INSERT INTO `sys_role_menu` VALUES ('8a80813d6d714f99016d7164725c0009', '0', '2019-09-27 14:24:31', 'admin', 'admin', '2019-09-27 14:24:31', '', '402880e6642811b501642811ca610001', '0', null);

-- ----------------------------
-- Table structure for sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `role_id` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_resource
-- ----------------------------
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b30002', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '999', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b30003', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/log', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b30004', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/log/add', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b40005', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/log/edit', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b40006', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/log/view', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b40007', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/log/page', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b40008', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/log/export', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b50009', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/log/viewDetail', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b5000a', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/log/del', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b5000b', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/menu', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b5000c', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/menu/add', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b5000d', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/menu/move', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b5000e', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/menu/edit', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b6000f', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/menu/del', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b60010', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/role', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b60011', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/role/add', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b60012', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/role/save', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b60013', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/role/resource', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b70014', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/role/edit', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b70015', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/role/page', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b70016', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/role/menu', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b70017', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/role/del', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b70018', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/user', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b80019', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/user/add', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b8001a', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/user/edit', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b8001b', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/user/page', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b8001c', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/user/export', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b8001d', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/user/role/save', '0', null);
INSERT INTO `sys_role_resource` VALUES ('8a80813d6cfa3949016cfa3a64b8001e', '0', '2019-09-04 11:03:47', 'admin', 'admin', '2019-09-04 11:03:47', '402880e664282031016428204a470000', '/sys/user/del', '0', null);

-- ----------------------------
-- Table structure for sys_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_token`;
CREATE TABLE `sys_token` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `token_key` varchar(255) DEFAULT NULL,
  `token_value` varchar(255) DEFAULT NULL,
  `issuer` varchar(255) DEFAULT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_token
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `accountNonExpired` tinyint(1) NOT NULL,
  `accountNonLocked` tinyint(1) NOT NULL,
  `credentialsNonExpired` tinyint(1) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `read_msg_time` datetime DEFAULT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  `openid` varchar(255) DEFAULT NULL,
  `role_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK74A81DFD3AC11281` (`role_id`),
  CONSTRAINT `FK74A81DFD3AC11281` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('297e91895a0eb90b015a0eba435f0000', '14', '2017-02-05 22:42:52', 'admin', 'admin', '2019-09-20 10:57:13', '1', '1', '1', '1', '698DE6D882C735D3FFDF08BF8CC90316', '', 'admin', '', '系统管理员', null, '0', null, null, '402880e664282031016428204a470000');
INSERT INTO `sys_user` VALUES ('402880e6642ce70f01642ce7ef700000', '1', '2018-06-23 21:49:15', 'admin', 'admin', '2018-06-23 22:19:44', '1', '1', '1', '1', '698DE6D882C735D3FFDF08BF8CC90316', null, 'platform', null, '平台管理员', null, '0', null, null, '402880e6642811b501642811ca610001');

-- ----------------------------
-- Table structure for sys_user_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_area`;
CREATE TABLE `sys_user_area` (
  `id` varchar(50) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `creator` varchar(50) NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `modify_time` datetime NOT NULL,
  `area_id` varchar(50) NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `del_flag` tinyint(1) NOT NULL,
  `shop_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_area
-- ----------------------------
