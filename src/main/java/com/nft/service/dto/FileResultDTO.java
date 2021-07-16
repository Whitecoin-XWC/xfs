package com.nft.service.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel
@Data
public class FileResultDTO {

    /**
     * 主键
     */
    @ApiModelProperty("tokenId")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    @TableField("file_title")
    private String fileTitle;
    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    @TableField("file_name")
    private String fileName;
    /**
     * 文件路径
     */
    @ApiModelProperty("文件存储路径")
    @TableField("file_path")
    private String filePath;
    /**
     * 付费时间，null 表示没付费
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("付费时间")
    @TableField("pay_time")
    private Date payTime;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private Date createTime;
    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发布时间")
    @TableField("pub_time")
    private Date pubTime;
    /**
     * 媒体类型 1 图片，2视频，3音频
     */
    @ApiModelProperty("媒体类型，暂时没用")
    @TableField("media_type")
    private Integer mediaType;
    /**
     * 状态，1. 已上传，2. 已发布，3. 已付费
     */
    @ApiModelProperty("状态 1. 已上传，2. 已发布，3. 已付费")
    @TableField("file_status")
    private Integer fileStatus;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    @TableField("file_des")
    private String fileDes;

    /**
     * 版权费（百分比）
     */
    @ApiModelProperty("版权费（百分比）")
    @TableField("copyright_fee")
    private BigDecimal copyrightFee;

    /**
     * 用户地址
     */
    @ApiModelProperty("用户钱包地址")
    private String userAddress;

    /**
     * 用户名
     */
    @ApiModelProperty("用户昵称")
    private String userName;

    /**
     * 售价
     */
    @ApiModelProperty("售价")
    private BigDecimal price;

    /**
     * 起拍价
     */
    @ApiModelProperty("起拍价")
    private BigDecimal pmPrice;

    @ApiModelProperty("拍卖最高价")
    private BigDecimal auctionMaxPrice;

    @ApiModelProperty("拍卖倒计时剩余时间")
    private long remainingTime;


    /**
     * 文件来源
     */
    @ApiModelProperty("文件来源 0创建， 1 商品，2 关注")
    private Integer source;
}
