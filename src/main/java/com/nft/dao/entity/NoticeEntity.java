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
 * Created by moloq on 2021/7/15 17:05
 */
@Data
@Getter
@Setter
@TableName("nft_notice")
public class NoticeEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @TableField("notice_type")
    @ApiModelProperty(value = "通知类型")
    private int noticeType;

    @TableField("notice_pople")
    @ApiModelProperty(value = "通知人")
    private String noticePople;

    @TableField("notice_file")
    @ApiModelProperty(value = "通知相关文件")
    private String noticeFile;

    @TableField("copyright_fee")
    @ApiModelProperty(value = "版权费")
    private BigDecimal copyrightFee;

    @TableField("recipient")
    @ApiModelProperty(value = "接受人地址，涉及售卖和拍卖，转移")
    private String recipient;

    @TableField("coin_type")
    @ApiModelProperty(value = "代币类型")
    private String coinType;

    @TableField("auction_id")
    @ApiModelProperty(value = "拍卖id")
    private long auctionId;

    @TableField("notice_status")
    @ApiModelProperty(value = "通知是否已读")
    private boolean noticeStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;
}
