/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : vip_class

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2020-04-24 11:52:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for databasechangelog
-- ----------------------------
DROP TABLE IF EXISTS `databasechangelog`;
CREATE TABLE `databasechangelog` (
  `ID` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `AUTHOR` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `FILENAME` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MD5SUM` varchar(35) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `COMMENTS` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TAG` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LIQUIBASE` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CONTEXTS` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LABELS` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for databasechangeloglock
-- ----------------------------
DROP TABLE IF EXISTS `databasechangeloglock`;
CREATE TABLE `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for prad_bookmark_record
-- ----------------------------
DROP TABLE IF EXISTS `prad_bookmark_record`;
CREATE TABLE `prad_bookmark_record` (
  `br_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `br_user_id` bigint(20) DEFAULT NULL COMMENT '用户主键',
  `br_news_id` bigint(20) DEFAULT NULL COMMENT '文章主键',
  `br_time` bigint(20) DEFAULT NULL COMMENT '收藏时间',
  `br_thumb_path` bigint(20) DEFAULT NULL COMMENT '文章缩略图',
  `br_title` bigint(20) DEFAULT NULL COMMENT '文章标题',
  `br_article_path` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`br_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏记录表';

-- ----------------------------
-- Table structure for prad_comment
-- ----------------------------
DROP TABLE IF EXISTS `prad_comment`;
CREATE TABLE `prad_comment` (
  `pc_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pc_user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `pc_user_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `pc_news_id` bigint(20) DEFAULT NULL COMMENT '文章主键',
  `pc_content` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评论内容',
  `pc_hits` int(11) DEFAULT NULL COMMENT '本条评论点赞量',
  `pc_time` bigint(20) DEFAULT NULL COMMENT '本条评论时间',
  PRIMARY KEY (`pc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ----------------------------
-- Table structure for prad_like_record
-- ----------------------------
DROP TABLE IF EXISTS `prad_like_record`;
CREATE TABLE `prad_like_record` (
  `lr_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lr_user_id` bigint(20) DEFAULT NULL COMMENT '用户主键',
  `lr_news_id` bigint(20) DEFAULT NULL COMMENT '资讯主键',
  `lr_time` bigint(20) DEFAULT NULL COMMENT '点赞时间',
  `lr_comment_id` bigint(20) DEFAULT NULL COMMENT '评论记录id',
  `lr_type` int(11) DEFAULT NULL COMMENT '类型（0：资讯点赞，1：评论点赞）',
  PRIMARY KEY (`lr_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='类型（0：资讯点赞，1：评论点赞）';

-- ----------------------------
-- Table structure for prad_news
-- ----------------------------
DROP TABLE IF EXISTS `prad_news`;
CREATE TABLE `prad_news` (
  `ne_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ne_nc_id` bigint(20) DEFAULT NULL COMMENT '分类id',
  `ne_title` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `ne_thumbnail_path` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '缩略图路径',
  `ne_carousel_path` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '轮播图路径',
  `ne_content` longtext COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `ne_video_path` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '视频路径',
  `ne_hits` int(11) DEFAULT NULL COMMENT '点击量',
  `ne_like_num` int(11) DEFAULT NULL COMMENT '点赞量',
  `ne_book_num` int(11) DEFAULT NULL COMMENT '收藏量',
  `ne_is_banner` int(11) DEFAULT NULL COMMENT '是否是头图(0:否，1：是)',
  `ne_banner_pic_path` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头部路径',
  `ne_expert_id` bigint(20) DEFAULT NULL COMMENT '关联专家id',
  `ne_date` bigint(20) DEFAULT NULL COMMENT '显示用发布日期',
  `ne_from` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '来源',
  `ne_publish_user_id` bigint(20) DEFAULT NULL COMMENT '发布人id',
  `ne_publish_user_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发布人姓名',
  `ne_create_time` bigint(20) DEFAULT NULL COMMENT '发布时间',
  `ne_update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `ne_update_user_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人姓名',
  `ne_update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `ne_is_valid` int(11) DEFAULT NULL COMMENT '是否有效(0：无效；1：有效)',
  `ne_is_carousel` int(11) DEFAULT NULL COMMENT '是否显示轮播图,(0,不显示,1:显示)',
  `ne_end_date` bigint(20) DEFAULT NULL COMMENT '直播结束时间',
  `ne_nc_ids` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类id（可选多个）',
  `ne_tag_ids` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容标签（以逗号分隔）',
  PRIMARY KEY (`ne_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容标签（以逗号分隔）';

-- ----------------------------
-- Table structure for prad_news_category
-- ----------------------------
DROP TABLE IF EXISTS `prad_news_category`;
CREATE TABLE `prad_news_category` (
  `nc_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nc_pid` bigint(20) DEFAULT NULL COMMENT '父id',
  `nc_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `nc_memo` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '介绍文字',
  `nc_pic_path` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '缩略图',
  `nc_add1` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用1',
  `nc_add2` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用2',
  `nc_add3` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备用3',
  PRIMARY KEY (`nc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资讯分类表';

-- ----------------------------
-- Table structure for prad_user
-- ----------------------------
DROP TABLE IF EXISTS `prad_user`;
CREATE TABLE `prad_user` (
  `pu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pu_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `pu_email` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `pu_pwd` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `pu_province` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省份',
  `pu_city` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `pu_region` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '区县',
  `pu_hospital` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '医院',
  `pu_dept` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '科室',
  `pu_professional` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职称',
  `pu_phone` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `pu_create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `pu_update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `pu_province_id` bigint(20) DEFAULT NULL COMMENT '省份id',
  `pu_city_id` bigint(20) DEFAULT NULL COMMENT '城市id',
  `pu_region_id` bigint(20) DEFAULT NULL COMMENT '区县id',
  `pu_from` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户来源',
  `pu_medlive_user_id` bigint(20) DEFAULT NULL COMMENT '医脉通用户id',
  `pu_medlive_user_thumb` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '医脉通用户头像',
  `pu_total_score` int(11) DEFAULT NULL COMMENT '总积分',
  PRIMARY KEY (`pu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='总积分';

-- ----------------------------
-- Table structure for prad_view_record
-- ----------------------------
DROP TABLE IF EXISTS `prad_view_record`;
CREATE TABLE `prad_view_record` (
  `vr_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vr_user_id` bigint(20) DEFAULT NULL COMMENT '用户主键',
  `vr_news_id` bigint(20) DEFAULT NULL COMMENT '课程主键',
  `vr_time` bigint(20) DEFAULT NULL COMMENT '浏览时间',
  PRIMARY KEY (`vr_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浏览记录表';

-- ----------------------------
-- Table structure for prad_visit_log
-- ----------------------------
DROP TABLE IF EXISTS `prad_visit_log`;
CREATE TABLE `prad_visit_log` (
  `vl_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vl_user_id` bigint(20) DEFAULT NULL COMMENT '用户主键',
  `vl_start_time` bigint(20) DEFAULT NULL COMMENT '访问页面开始时间',
  `vl_end_time` bigint(20) DEFAULT NULL COMMENT '访问页面结束时间',
  `vl_duration` bigint(20) DEFAULT NULL COMMENT '访问页面时长(秒)',
  `vl_visit_module` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '观看模块',
  `vl_news_module` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资讯所属模块',
  `vl_news_id` bigint(20) DEFAULT NULL COMMENT '资讯主键',
  `vl_include_video` int(11) DEFAULT NULL COMMENT '是否包含视频(0：不包含，1：包含)',
  `vl_ip` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ip地址',
  `vl_like` int(11) DEFAULT NULL COMMENT '是否点赞(0：未点赞；1: 点赞；2：取消点赞)',
  `vl_like_time` bigint(20) DEFAULT NULL COMMENT '点赞时间/取消点赞时间',
  `vl_favorite` int(11) DEFAULT NULL COMMENT '是否收藏(0：未收藏；1: 收藏；2：取消收藏)',
  `vl_favorite_time` bigint(20) DEFAULT NULL COMMENT '收藏时间/取消收藏时间',
  `vl_vr_id` bigint(20) DEFAULT NULL COMMENT '视频观看记录主键',
  PRIMARY KEY (`vl_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='访问记录日志';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `su_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `su_login_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `su_password` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `su_create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `su_update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`su_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- ----------------------------
-- Table structure for wechat_user
-- ----------------------------
DROP TABLE IF EXISTS `wechat_user`;
CREATE TABLE `wechat_user` (
  `wu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `wu_user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `wu_user_type` int(11) DEFAULT NULL COMMENT '用户类型(医生)',
  `wu_openid` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'openid',
  `wu_nickname` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `wu_sex` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `wu_city` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `wu_country` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家',
  `wu_province` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省份',
  `wu_language` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '语言',
  `wu_headimgurl` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `wu_unionid` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'unionid',
  `wu_remark` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `wu_headimg` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像本地路径',
  `wu_groupid` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户分组id',
  `wu_tagid_list` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签ID列表',
  `wu_subscribe` int(11) DEFAULT NULL COMMENT '是否订阅该公众号标识（0：未关注；1：关注）',
  `wu_subscribe_time` bigint(20) DEFAULT NULL COMMENT '关注时间',
  `wu_create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `wu_update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `wu_source` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户来源(1:翼多会议)',
  PRIMARY KEY (`wu_id`) USING BTREE,
  KEY `us_id` (`wu_user_id`) USING BTREE,
  KEY `open_id` (`wu_openid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='微信用户表';



-- ----------------------------
-- Table structure for sms_code
-- ----------------------------
DROP TABLE IF EXISTS `sms_code`;
CREATE TABLE `sms_code` (
  `sc_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sc_phone` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `sc_email` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `sc_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '激活码',
  `sc_type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  `sc_invalid_date` bigint(20) DEFAULT NULL COMMENT '失效时间',
  `sc_create_date` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `sc_is_used` int(11) DEFAULT NULL COMMENT '是否使用(0:否1:是)',
  `sc_user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `sc_ip_address` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP地址',
  `sc_send_prompt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送提示',
  `sc_send_flag` int(11) DEFAULT NULL COMMENT '发送标志（0：失败；1：成功）',
  PRIMARY KEY (`sc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短信验证码';