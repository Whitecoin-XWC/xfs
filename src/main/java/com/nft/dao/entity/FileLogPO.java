package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 用户地址
     */
    @TableField("user_id")
    private String userId;
    /**
     * 日志内容
     */
    @TableField("log_info")
    private String logInfo;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    /**
     * 类型：0 普通变化，1 竞拍，2 出售
     */
    @TableField("type")
    private Integer type;

    /**
     * 其他数据，以json字符串的形式存储
     */
    @TableField("other")
    private String other;
}
