package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: nft
 * Created by moloq on 2021/6/21 9:16
 */
@Data
@Getter
@Setter
@TableName("nft_auction")
public class AuctionEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @TableField("auction_creater")
    @ApiModelProperty(value = "拍卖创建者",required = true)
    private String auctionCreater;

    /**
     * 拍卖的产品id
     */
    @TableField("file_token_id")
    @ApiModelProperty(value = "拍卖的产品id",required = true)
    private String fileTokenId;

    @TableField("trade_id")
    private String tradeId;

    /**
     * 拍卖id
     */
    @TableField("auction_id")
    @ApiModelProperty(value = "拍卖id")
    private String auctionId;

    /**
     * 拍卖的代币
     */
    @TableField("auction_coin")
    @ApiModelProperty(value = "代币类型",required = true)
    private String auctionCoin;

    /**
     * 拍卖保留价
     */
    @TableField("auction_retain_price")
    @ApiModelProperty(value = "拍卖保留价",required = true)
    private BigDecimal auctionRetainPrice;

    /**
     * 最低加价幅度
     */
    @TableField("auction_min_markup")
    @ApiModelProperty(value = "最低加价幅度",required = true)
    private BigDecimal auctionMinMarkup;

    /**
     * 拍卖保留价美元
     */
    @TableField(exist = false)
    private BigDecimal auctionRetainPriceUsdt;

    /**
     * 最低加价幅度美元
     */
    @TableField(exist = false)
    private BigDecimal auctionMinMarkupUsdt;

    /**
     * 拍卖最高价
     */
    @TableField("auction_max_price")
    private BigDecimal auctionMaxPrice;

    @TableField("auction_max_er")
    private String auctionMaxEr;

    /**
     * 拍卖状态
     */
    @TableField("auction_status")
    @ApiModelProperty(value = "拍卖状态，0-创建拍卖，未开始竞拍；1-竞拍中；2-竞拍结束，待领取；3-领取完成",required = true)
    private Integer auctionStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    /**
     * 拍卖开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("auction_start_time")
    private Date auctionStartTime;

    /**
     * 记录修改时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;
}
