package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Description: nft
 * Created by moloq on 2021/9/2 13:58
 */
@Data
@Getter
@Setter
@TableName("nft_message")
public class MessageEntity {
    private Long id;
    private String tokenId;
    private String userAddress;
    private String messageContext;
    private Date createTime;
    @TableField(exist = false)
    private String nickName;
    @TableField(exist = false)
    private boolean owner = Boolean.FALSE;
}
