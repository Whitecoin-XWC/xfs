package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class FileUserChangeVO {

    /**
     * tokenId
     */
    @ApiModelProperty("tokenId，从上传接口返回")
    private String tokenId;

    /**
     * 用户标识
     */
    @ApiModelProperty("用户地址（要转移给谁就写谁的地址）")
    private String userAddress;

    /**
     * 交易ID
     */
    @ApiModelProperty("交易ID")
    private String tractionId;

    @ApiModelProperty("交易金额")
    private BigDecimal price;

    @ApiModelProperty("交易币种")
    private String name;
}
