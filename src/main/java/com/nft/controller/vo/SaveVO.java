package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SaveVO {

    /**
     * 名称
     */

    @ApiModelProperty("标题")
    private String title;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String des;

    /**
     * 文件的tokenId
     */
    @ApiModelProperty("文件的tokenId，上传接口返回")
    private String tokenId;
    /**
     * 用户标识
     */
    @ApiModelProperty("用户标识")
    private String userTag;
}
