package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Description: nft
 * Created by moloq on 2021/9/8 15:37
 */
@Data
@Getter
@Setter
@TableName("nft_shop")
public class ShopPO {
    private String id;
    private boolean shopStatus;
    private Date createTime;
    private Date updateTime;
}
