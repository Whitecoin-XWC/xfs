package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("nft_file_log")
public class FileLogPO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 文件ID
     */
    @TableField("file_id")
    private String fileId;
    /**
     * 日志内容
     */
    @TableField("log_info")
    private String logInfo;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
}
