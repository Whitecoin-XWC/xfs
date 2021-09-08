package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: nft
 * Created by moloq on 2021/9/8 15:45
 */
@Data
@Getter
@Setter
@TableName("nft_order")
public class OrderPO {
    private Integer id;
    private String tokenId;
    private BigDecimal price;
    private String address;
    private String zipCode;
    private String phone;
    private String name;
    private String orderStatus;
    private String expressNo;
    private String expressLink;
    private Date createTime;
    private Date updateTime;
}
