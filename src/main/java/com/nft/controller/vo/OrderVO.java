package com.nft.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Description: nft
 * Created by moloq on 2021/9/9 16:43
 */
@Data
@Getter
@Setter
public class OrderVO {
    @ApiModelProperty("user address")
    private String userAddress;
    @ApiModelProperty("第几页")
    private int page;
    @ApiModelProperty("每页大小，默认20")
    private int pageSize = 20;
    @ApiModelProperty("order status")
    private String orderStatus;
}
