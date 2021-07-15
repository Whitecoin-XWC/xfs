package com.nft.dao.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Description: nft
 * Created by moloq on 2021/7/14 9:30
 */
@Data
@Getter
@Setter
public class ReceivePO {
    @ApiModelProperty(value = "文件id", required = true)
    private String fileTokenId;

    @ApiModelProperty(value = "钱包地址", required = true)
    private String userAddress;
}
