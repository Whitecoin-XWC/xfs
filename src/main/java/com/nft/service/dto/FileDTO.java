package com.nft.service.dto;

import com.nft.dao.entity.FileLogPO;
import com.nft.dao.entity.FilePO;
import lombok.Data;

import java.util.List;

@Data
public class FileDTO {

    private FilePO filePO;

    private List<FileLogPO> fileLogPOList;

    private String imgUrl;
}
