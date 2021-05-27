CREATE TABLE `xfs_file` (
  `id` varchar(50) NOT NULL COMMENT '主键，uuid',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '付费时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `pub_time` timestamp NULL DEFAULT NULL COMMENT '发布时间',
  `user_tag` varchar(255) DEFAULT NULL COMMENT '用户标识',
  `media_type` int(2) DEFAULT NULL COMMENT '文件类型 1 图片，2视频，3音频',
  `file_status` int(2) DEFAULT NULL COMMENT '状态，1. 已上传，2. 已发布，3. 已付费',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;