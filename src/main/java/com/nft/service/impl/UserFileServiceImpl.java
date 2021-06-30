package com.nft.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * @param userTag
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public IPage<FileResultDTO> selectFiles(String userTag, int page, int pageSize) {
        return nftService.selectFiles(userTag, page, pageSize);
    }
}
