package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class FileVO {

    /**
     * tokenId
     */
    @ApiModelProperty("tokenId，从上传接口返回")
    private String tokenId;

    /**
     * 用户标识
     */
    @ApiModelProperty("用户标识")
    private String userTag;
}
