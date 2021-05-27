package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上传文件后返回实体
 */
@Data
@ApiModel
public class UploadResultVO {

    /**
     * tokenId，唯一标识
     */
    @ApiModelProperty("tokenId，唯一标识")
    private String tokenId;

    /**
     * 文件访问路径
     */
    @ApiModelProperty("文件访问路径")
    private String url;
}
