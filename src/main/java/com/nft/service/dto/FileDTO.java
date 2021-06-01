package com.nft.service.dto;

import com.nft.dao.entity.FileLogPO;
import com.nft.dao.entity.FilePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class FileDTO {

    @ApiModelProperty("文件数据")
    private FilePO filePO;

    @ApiModelProperty("操作日志")
    private List<FileLogPO> fileLogPOList;

    @ApiModelProperty("图片url前缀")
    private String imgUrl;
}
