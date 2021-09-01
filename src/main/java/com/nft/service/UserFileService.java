package com.nft.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.controller.vo.FileVO;
import com.nft.service.dto.FileResultDTO;

public interface UserFileService {

    IPage<FileResultDTO> selectFiles(FileVO fileVO);
}
