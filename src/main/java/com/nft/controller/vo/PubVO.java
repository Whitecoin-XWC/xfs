package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PubVO {

    /**
     * tokenId
     */
    @ApiModelProperty("tokenId，从上传接口返回")
    private String tokenId;

    /**
     * 用户钱包地址
     */
    @ApiModelProperty("用户钱包地址")
    private String userAddress;

    /**
     * 交易ID
     */
    @ApiModelProperty("交易ID")
    private String tractionId;
}
