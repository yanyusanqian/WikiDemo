/*
 Navicat Premium Data Transfer

 Source Server         : wikidev
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : wikidev

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 02/11/2023 14:38:57
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`
(
    `id`     bigint(20) NOT NULL COMMENT 'id',
    `parent` bigint(20) NOT NULL DEFAULT 0 COMMENT '父id',
    `name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `sort`   int(11) NULL DEFAULT NULL COMMENT '顺序',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category`
VALUES (100, 0, '前端开发', 100);
INSERT INTO `category`
VALUES (101, 100, 'Vue', 101);
INSERT INTO `category`
VALUES (102, 100, 'HTML&CSS', 102);
INSERT INTO `category`
VALUES (200, 0, 'Java', 200);
INSERT INTO `category`
VALUES (201, 200, '基础应用', 201);
INSERT INTO `category`
VALUES (202, 200, '框架应用', 202);
INSERT INTO `category`
VALUES (300, 0, 'Python', 300);
INSERT INTO `category`
VALUES (301, 300, '基础应用', 301);
INSERT INTO `category`
VALUES (302, 300, '进阶方向应用', 302);
INSERT INTO `category`
VALUES (400, 0, '数据库', 400);
INSERT INTO `category`
VALUES (401, 400, 'MySQL', 401);
INSERT INTO `category`
VALUES (500, 0, '其它', 500);
INSERT INTO `category`
VALUES (501, 500, '服务器', 501);
INSERT INTO `category`
VALUES (502, 500, '开发工具', 502);
INSERT INTO `category`
VALUES (503, 500, '热门服务端语言', 503);
INSERT INTO `category`
VALUES (372656688534261760, 372656612059516928, 'test2', 2);
INSERT INTO `category`
VALUES (372670031085047809, 400, 'Oracle', 402);

-- ----------------------------
-- Table structure for content
-- ----------------------------
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content`
(
    `id`      bigint(20) NOT NULL COMMENT '文档id',
    `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of content
-- ----------------------------
INSERT INTO `content`
VALUES (1, '<p>ttetetffdfsg</p>');
INSERT INTO `content`
VALUES (374832919799271424, '<p>3333333333333</p>');
INSERT INTO `content`
VALUES (375161739140009984, '<p>啊打发哥特确认wfsf</p>');

-- ----------------------------
-- Table structure for doc
-- ----------------------------
DROP TABLE IF EXISTS `doc`;
CREATE TABLE `doc`
(
    `id`         bigint(20) NOT NULL COMMENT 'id',
    `ebook_id`   bigint(20) NOT NULL COMMENT '电子书id',
    `parent`     bigint(20) NOT NULL COMMENT '父id',
    `name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
    `sort`       int(11) NULL DEFAULT NULL COMMENT '顺序',
    `view_count` int(11) NULL DEFAULT 0 COMMENT '阅读数',
    `vote_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doc
-- ----------------------------
INSERT INTO `doc`
VALUES (1, 1, 0, '文档1', 1, 0, 0);
INSERT INTO `doc`
VALUES (2, 1, 1, '文档1.1', 1, 0, 0);
INSERT INTO `doc`
VALUES (3, 1, 0, '文档2', 2, 0, 0);
INSERT INTO `doc`
VALUES (4, 1, 3, '文档2.1', 1, 0, 0);
INSERT INTO `doc`
VALUES (5, 1, 3, '文档2.2', 2, 0, 0);
INSERT INTO `doc`
VALUES (6, 1, 5, '文档2.2.1', 1, 0, 0);
INSERT INTO `doc`
VALUES (374075529357299712, 1, 0, '1', 1, NULL, NULL);
INSERT INTO `doc`
VALUES (374832919799271424, 1, 374075529357299712, 'test', 1, NULL, NULL);

-- ----------------------------
-- Table structure for ebook
-- ----------------------------
DROP TABLE IF EXISTS `ebook`;
CREATE TABLE `ebook`
(
    `id`           bigint(20) NOT NULL COMMENT 'id',
    `name`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `category1_id` bigint(20) NULL DEFAULT NULL COMMENT '分类1',
    `category2_id` bigint(20) NULL DEFAULT NULL COMMENT '分类2',
    `description`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `cover`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
    `doc_count`    int(11) NULL DEFAULT NULL COMMENT '文档数',
    `view_count`   int(11) NULL DEFAULT NULL COMMENT '阅读数',
    `vote_count`   int(11) NULL DEFAULT NULL COMMENT '点赞数',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ebook
-- ----------------------------
INSERT INTO `ebook`
VALUES (1, 'Spring Boot 入门教程', 200, 202, '零基础入门 Java 开发，企业级应用开发最佳首选框架', NULL, NULL, NULL, NULL);
INSERT INTO `ebook`
VALUES (2, 'Vue 入门教程', 100, 101, '零基础入门 Vue 开发，企业级应用开发最佳首选框架', NULL, NULL, NULL, NULL);
INSERT INTO `ebook`
VALUES (3, 'Python 入门教程', 300, 301, '零基础入门 Python 开发，企业级应用开发最佳首选框架', NULL, NULL, NULL, NULL);
INSERT INTO `ebook`
VALUES (4, 'Mysql 入门教程', 400, 401, '零基础入门 Mysql 开发，企业级应用开发最佳首选框架', NULL, NULL, NULL, NULL);
INSERT INTO `ebook`
VALUES (5, 'Oracle 入门教程', NULL, NULL, '零基础入门 Oracle 开发，企业级应用开发最佳首选框架', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`         bigint(20) NOT NULL COMMENT 'ID',
    `login_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录名',
    `name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
    `password`   char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NOT NULL COMMENT '密码',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `login_name_unique`(`login_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------

SET
FOREIGN_KEY_CHECKS = 1;

INSERT INTO `user`
VALUES (1, 'test', '测试 ', 'test');
INSERT INTO `user`
VALUES (375561915369394176, 'test1', 'test1', '7354a1d413535a6c0dc5c209e198d799');


-- ----------------------------
-- Table structure for ebook_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `ebook_snapshot`;
CREATE TABLE `ebook_snapshot`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `ebook_id`      bigint(20) NOT NULL DEFAULT 0 COMMENT '电子书id',
    `date`          date NOT NULL COMMENT '快照日期',
    `view_count`    int(11) NOT NULL DEFAULT 0 COMMENT '阅读数',
    `vote_count`    int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
    `view_increase` int(11) NOT NULL DEFAULT 0 COMMENT '阅读增长',
    `vote_increase` int(11) NOT NULL COMMENT '点赞增长',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE key `ebook_id_date_unique`(`ebook_id`,`date`)

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 方案一 (ID不连续)
-- 删除之前的数据
-- 为所有的电子书生成一条今天的记录
-- 更新总阅读数、总点赞数
-- 更新今日阅读数、今日点赞数
--
-- 方案二 (ID连续)
-- 为所有的电子书生成一条今天的记录（如果还没有）
-- 更新总阅读数、总点赞数
-- 更新今日阅读数、今日点赞数

insert into ebook_snapshot(ebook_id, `date`, view_count, vote_count, view_increase, vote_increase)
select t1.id, curdate(), 0, 0, 0, 0
from ebook t1
where not exists(select 1
                 from ebook_snapshot t2
                 where t1.id = t2.ebook_id
                   and t2.`date` = curdate());

update ebook_snapshot t1, ebook t2
set t1.view_count = t2.view_count, t1.vote_count = t2.vote_count
where t1.`date` = curdate()
and t1.ebook_id = t2.id;

-- 获取昨天数据
select t1.ebook_id, view_count, vote_count from ebook_snapshot t1
where t1.`date` = date_sub(curdate(), interval 1 day)