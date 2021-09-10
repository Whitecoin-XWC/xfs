package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel
@TableName("nft_file")
public class FilePO {
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

    @ApiModelProperty("创建者")
    @TableField("creater")
    private String creater;

    @ApiModelProperty("创建者昵称")
    @TableField(exist = false)
    private String createrNickName;
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
    private Integer copyrightFee;

    /**
     * 文件的MD5
     */
    @ApiModelProperty("文件的MD5")
    @TableField("md5")
    private String md5;
    /**
     * 文件内容
     */
    @ApiModelProperty("文件内容")
    @TableField(exist = false)
    private String txtContent;


    @TableField(exist = false)
    private String userAddress;

    @TableField(exist = false)
    private String userAddressNickName;


    @TableField(exist = false)
    private String type;

    @TableField(exist = false)
    private String unit;

    @TableField(exist = false)
    private BigDecimal price;

    @TableField(exist = false)
    private BigDecimal priceUsdt;

    @TableField(exist = false)
    private Integer collect;

    @TableField(exist = false)
    private String entity;

    public void setCopyrightFee(String copyrightFeeStr) {
        if (StringUtils.isEmpty(copyrightFeeStr)) {
            copyrightFeeStr = "0";
        }
        this.copyrightFee = Integer.parseInt(copyrightFeeStr);
    }
}
