package com.nft.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class FileVO {

    /**
     * tokenId
     */
    @ApiModelProperty("tokenId，从上传接口返回")
    private String tokenId;

    /**
     * 用户标识
     */
    @ApiModelProperty("用户地址")
    private String userAddress;

    /**
     * 文件类型
     */
    @ApiModelProperty("文件类型，0文件，1 图片，2视频，3音频，4动图")
    private Integer mediaType;

    /**
     * 文件状态
     */
    @ApiModelProperty("文件状态，1. 已上传，2. 已发布，3. 已付费，4 出售中，5 拍卖中")
    private Integer status;

    /**
     * 第几页
     */
    @ApiModelProperty("第几页")
    private int page;
    /**
     * 每个大小
     */
    @ApiModelProperty("每页大小，默认20")
    private int pageSize = 20;
}
