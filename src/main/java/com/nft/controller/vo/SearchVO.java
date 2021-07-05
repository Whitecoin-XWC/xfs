package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SearchVO {

    @ApiModelProperty("关键词")
    private String keyWord;
}
