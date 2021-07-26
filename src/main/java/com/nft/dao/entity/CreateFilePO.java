package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Description: nft
 * Created by moloq on 2021/7/26 15:08
 */
@Data
@TableName("nft_create_file")
public class CreateFilePO {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * '用户ID',
     */
    @TableField("user_id")
    private String userId;
    /**
     * '文件ID',
     */
    @TableField("file_id")
    private String fileId;
    /**
     * '创建时间',
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 用户昵称
     */
    @TableField("user_name")
    private String userName;
}
