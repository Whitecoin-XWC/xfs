package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("nft_sell_info")
public class SellInfoPO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * '文件ID'
     */
    @TableField("token_id")
    private String tokenId;
    /**
     * '价格单位',
     */
    @TableField("unit")
    private String unit;
    /**
     * '价格',
     */
    @TableField("price")
    private BigDecimal price;
    /**
     * '状态 0 有效，1 失效，3删除',
     */
    @TableField("status")
    private Integer status;

}
