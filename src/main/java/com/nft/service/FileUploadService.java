package com.nft.service;

import com.nft.dao.entity.FilePO;
import com.nft.service.dto.FileDTO;

import java.util.List;

public interface FileUploadService {

    /**
     * 保存文件
     * @param filePO
     * @return
     */
    int save(FilePO filePO);

    /**
     * 获取一个文件
     * @param filePO
     * @return
     */
    FileDTO getFile(FilePO filePO);

    /**
     * 查询当前用户的所有文件
     * @param filePO
     * @param page
     * @param pageSize
     * @return
     */
    List<FileDTO> selectFiles(FilePO filePO, int page, int pageSize);
}
