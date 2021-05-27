package com.nft.service.impl;

import com.nft.dao.entity.FilePO;
import com.nft.dao.mapper.FileMapper;
import com.nft.service.NFTService;
import com.nft.service.dto.FileDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NFTServiceImpl implements NFTService {

    @Resource
    private FileMapper fileMapper;

    /**
     * 保存文件
     * @param filePO
     * @return
     */
    @Override
    public int save(FilePO filePO) {

        FilePO fileItem = fileMapper.selectById(filePO.getId());
        if(fileItem != null || !StringUtils.isEmpty(fileItem.getId())){
            return -1;
        }

        return 0;
    }

    /**
     * 获取一个文件
     * @param filePO
     * @return
     */
    @Override
    public FileDTO getFile(FilePO filePO) {
        return null;
    }

    /**
     * 查询当前用户的所有文件
     * @param filePO
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<FileDTO> selectFiles(FilePO filePO, int page, int pageSize) {
        return null;
    }
}
