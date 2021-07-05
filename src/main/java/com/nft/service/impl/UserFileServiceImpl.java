package com.nft.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.controller.vo.FileVO;
import com.nft.service.NFTService;
import com.nft.service.UserFileService;
import com.nft.service.dto.FileResultDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userFileService")
public class UserFileServiceImpl implements UserFileService {

    @Resource
    private NFTService nftService;

    /**
     * 查询当前用户的文件
     * @return
     */
    @Override
    public IPage<FileResultDTO> selectFiles(FileVO fileVO) {
        return nftService.selectFiles(fileVO);
    }
}
