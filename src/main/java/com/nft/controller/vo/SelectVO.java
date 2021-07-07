package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SelectVO {

    /**
     * 公告
     */
    @ApiModelProperty(value = "用户标识")
    private String userAddress;
    /**
     * 第几页
     */
    @ApiModelProperty("第几页")
    private int page;
    /**
     * 每个大小
     */
    @ApiModelProperty("每页大小，默认20")
    private int pageSize = 20;

    /**
     * update moloqu 2021-07-07
     */
    @ApiModelProperty("媒体数据来源，0创建， 1 商品，2 关注")
    private Integer source;
}
