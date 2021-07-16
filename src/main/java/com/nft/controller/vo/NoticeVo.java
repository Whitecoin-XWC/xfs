package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description: nft
 * Created by moloq on 2021/7/15 17:14
 */
@Data
@ApiModel
public class NoticeVo {
    @ApiModelProperty("钱包地址")
    private String userAddress;
    @ApiModelProperty("当前页")
    private Integer pageNo;
    @ApiModelProperty("一页数量")
    private Integer pageSize;
}
