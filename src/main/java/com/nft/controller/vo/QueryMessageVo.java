package com.nft.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Description: nft
 * Created by moloq on 2021/9/2 14:27
 */
@Getter
@Setter
@Data
public class QueryMessageVo {
    @ApiModelProperty("tokenId，从上传接口返回")
    private String tokenId;
    @ApiModelProperty("第几页")
    private int page;
    @ApiModelProperty("每页大小，默认20")
    private int pageSize = 20;
}
