package com.nft.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.controller.vo.FileVO;
import com.nft.service.dto.FileResultDTO;

public interface UserFileService {

    /**
     * 查询本站的所有文件
     * @return
     */
    IPage<FileResultDTO> selectFiles(FileVO fileVO);
}
