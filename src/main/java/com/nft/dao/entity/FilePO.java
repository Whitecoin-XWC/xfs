package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("nft_file")
public class FilePO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 标题
     */
    @TableField("file_title")
    private String fileTitle;
    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;
    /**
     * 付费时间，null 表示没付费
     */
    @TableField("pay_time")
    private Date payTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 发布时间
     */
    @TableField("pub_time")
    private Date pubTime;
    /**
     * 用户标识
     */
    @TableField("user_tag")
    private String userTag;
    /**
     * 媒体类型 1 图片，2视频，3音频
     */
    @TableField("media_type")
    private Integer mediaType;
    /**
     * 状态，1. 已上传，2. 已发布，3. 已付费
     */
    @TableField("file_status")
    private Integer fileStatus;
    /**
     * 描述
     */
    @TableField("file_des")
    private String fileDes;
}
