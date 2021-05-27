package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xfs_file")
public class FilePO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
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
    private String payTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;
    /**
     * 用户标识
     */
    @TableField("user_tag")
    private String userTag;
}
