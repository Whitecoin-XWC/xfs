package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BuyVO {

    /**
     * 买方用户地址
     */
    @ApiModelProperty("buyUserAddress")
    private String buyUserAddress;

    /**
     * '文件ID'
     */
    @ApiModelProperty("tokenId")
    private String tokenId;


}
