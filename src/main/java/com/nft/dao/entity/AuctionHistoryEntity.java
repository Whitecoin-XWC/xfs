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
 * Created by moloq on 2021/6/21 9:29
 */
@Data
@Getter
@Setter
@TableName("nft_auction_history")
public class AuctionHistoryEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @TableField("file_id")
    @ApiModelProperty(value = "拍卖的图片id", required = true)
    private String fileId;

    @TableField("trade_id")
    private String tradeId;

    /**
     * 拍卖记录
     */
    @TableField("auction_id")
    @ApiModelProperty(value = "拍卖的记录id", required = true)
    private Long auctionId;

    /**
     * 拍卖人
     */
    @TableField("auctioneer")
    @ApiModelProperty(value = "出价人", required = true)
    private String auctioneer;

    /**
     * 拍卖时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("auction_time")
    private Date auctionTime;

    /**
     * 竞拍最高价
     */
    @TableField("auction_max_price")
    private BigDecimal auctionMaxPrice;

    /**
     * 竞拍出价
     */
    @TableField("auction_price")
    @ApiModelProperty(value = "出价", required = true)
    private BigDecimal auctionPrice;
}
