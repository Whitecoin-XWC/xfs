package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("nft_user_file")
public class UserFilePO {

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
     * '类型，0创建， 1 商品，2 关注',
     */
    @TableField("type")
    private Integer type;

    /**
     * 用户昵称
     */
    @TableField("user_name")
    private String userName;
}
