

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wechat_user
-- ----------------------------
DROP TABLE IF EXISTS `wechat_user`;
CREATE TABLE `wechat_user`  (
  `wu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `wu_user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `wu_user_type` int(11) NULL DEFAULT NULL COMMENT '用户类型(医生)',
  `wu_openid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'openid',
  `wu_nickname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `wu_sex` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `wu_city` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '城市',
  `wu_country` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '国家',
  `wu_province` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '省份',
  `wu_language` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '语言',
  `wu_headimgurl` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `wu_unionid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'unionid',
  `wu_remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `wu_headimg` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像本地路径',
  `wu_groupid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户分组id',
  `wu_tagid_list` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签ID列表',
  `wu_subscribe` int(11) NULL DEFAULT NULL COMMENT '是否订阅该公众号标识（0：未关注；1：关注）',
  `wu_subscribe_time` bigint(20) NULL DEFAULT NULL COMMENT '关注时间',
  `wu_create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `wu_update_time` bigint(20) NULL DEFAULT NULL COMMENT '修改时间',
  `wu_source` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户来源(1:翼多会议)',
  PRIMARY KEY (`wu_id`) USING BTREE,
  INDEX `us_id`(`wu_user_id`) USING BTREE,
  INDEX `open_id`(`wu_openid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '微信用户表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
