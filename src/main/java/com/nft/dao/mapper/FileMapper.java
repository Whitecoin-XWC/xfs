package com.nft.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.dao.entity.FilePO;
import com.nft.service.dto.FileResultDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMapper extends BaseMapper<FilePO> {

    IPage<FileResultDTO> selectFileList(IPage<FileResultDTO> page, FileResultDTO fileResultDTO);

    IPage<FileResultDTO> selectCreateFileList(IPage<FileResultDTO> page, FileResultDTO fileResultDTO);

    IPage<FileResultDTO> selectFollowFileList(IPage<FileResultDTO> page, FileResultDTO fileResultDTO);
}
