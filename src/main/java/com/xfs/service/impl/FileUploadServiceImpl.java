package com.xfs.service.impl;

import com.xfs.dao.entity.FilePO;
import com.xfs.service.FileUploadService;
import com.xfs.service.dto.FileDTO;

import java.util.List;

public class FileUploadServiceImpl implements FileUploadService {

    /**
     * 保存文件
     * @param filePO
     * @return
     */
    @Override
    public int save(FilePO filePO) {
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
