package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class FileLogVO {

    @ApiModelProperty("tokenId")
    private String tokenId;

    @ApiModelProperty("第几页")
    private int page;

    @ApiModelProperty("每页大小")
    private int pageSize;

}
