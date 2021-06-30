package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("nft_userinfo")
public class UserinfoPO {

    /**
     * '用户ID，钱包地址',
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * '昵称',
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * '简介',
     */
    @TableField("introduction")
    private String introduction;
    /**
     * '创建时间',
     */
    @TableField("create_time")
    private Date createTime;
}
