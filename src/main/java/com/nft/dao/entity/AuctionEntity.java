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
    private Long id;

    /**
     * 拍卖的产品id
     */
    @TableField("file_token_id")
    private String fileTokenId;

    /**
     * 拍卖id
     */
    @TableField("auction_id")
    private String auctionId;

    /**
     * 拍卖的代币
     */
    @TableField("auction_coin")
    private String auctionCoin;

    /**
     * 拍卖保留价
     */
    @TableField("auction_retain_price")
    private BigDecimal auctionRetainPrice;

    /**
     * 最低加价幅度
     */
    @TableField("auction_min_markup")
    private BigDecimal auctionMinMarkup;

    /**
     * 拍卖最高价
     */
    @TableField("auction_max_price")
    private BigDecimal auctionMaxPrice;

    /**
     * 拍卖状态
     */
    @TableField("auction_status")
    private Integer auctionStatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 拍卖开始时间
     */
    @TableField("auction_start_time")
    private Date auctionStartTime;

    /**
     * 记录修改时间
     */
    @TableField("update_time")
    private Date updateTime;
}
