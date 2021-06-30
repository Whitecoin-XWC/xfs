package com.nft.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.dao.entity.FilePO;
import com.nft.service.dto.FileResultDTO;

public interface UserFileService {

    /**
     * 查询本站的所有文件
     * @param userTag
     * @param page
     * @param pageSize
     * @return
     */
    IPage<FileResultDTO> selectFiles(String userTag, int page, int pageSize);
}
