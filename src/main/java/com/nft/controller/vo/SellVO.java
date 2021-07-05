package com.nft.controller.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class SellVO {

    /**
     * '文件ID'
     */
    @ApiModelProperty("tokenId")
    private String tokenId;
    /**
     * '价格单位',
     */
    @ApiModelProperty("unit")
    private String unit;
    /**
     * '价格',
     */
    @ApiModelProperty("price")
    private BigDecimal price;

    /**
     * 用户地址
     */
    @ApiModelProperty("用户地址")
    private String userAddress;
}
