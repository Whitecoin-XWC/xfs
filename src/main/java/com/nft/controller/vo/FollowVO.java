package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class FollowVO {

    /**
     * tokenId
     */
    @ApiModelProperty("tokenId，被关注的文件")
    private String tokenId;

    /**
     * 用户钱包地址
     */
    @ApiModelProperty("用户钱包地址")
    private String userAddress;
}
