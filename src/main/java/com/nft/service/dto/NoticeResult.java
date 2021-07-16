package com.nft.service.dto;

import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.FilePO;
import com.nft.dao.entity.UserFilePO;
import com.nft.dao.entity.UserinfoPO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description: nft
 * Created by moloq on 2021/7/15 16:57
 */
@Data
@ApiModel
public class NoticeResult {

    @ApiModelProperty("您的竞拍")
    private List<MyAuction> myAuction;

    @ApiModelProperty("版权费")
    private List<CopyrightFee> copyrightFee;


    @Getter
    @Setter
    public static class MyAuction {
        @ApiModelProperty("NFT名称")
        private String NFTName;
        @ApiModelProperty("拍卖结果")
        private String auctionResult;
        @ApiModelProperty("拍卖出价")
        private BigDecimal auctionPrice;
        @ApiModelProperty("拍卖出价")
        private BigDecimal auctionPriceUsdt;
        @ApiModelProperty("拍卖倒计时")
        private long remainingTime;
    }

    @Getter
    @Setter
    public static class CopyrightFee {
        @ApiModelProperty("NFT名称")
        private String NFTName;
        @ApiModelProperty("提供者")
        private String provider;
        @ApiModelProperty("版权费")
        private BigDecimal copyrightFee;
        @ApiModelProperty("版权费美元")
        private BigDecimal copyrightFeeUsdt;
    }
}
