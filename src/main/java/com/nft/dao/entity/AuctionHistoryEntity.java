package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private Long id;

    /**
     * 拍卖记录
     */
    @TableField("auction_id")
    private Long auctionId;

    /**
     * 拍卖人
     */
    @TableField("auctioneer")
    private String auctioneer;

    /**
     * 拍卖时间
     */
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
    private BigDecimal auctionPrice;
}
