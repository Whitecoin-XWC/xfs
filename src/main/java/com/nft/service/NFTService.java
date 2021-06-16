package com.nft.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.controller.vo.PubVO;
import com.nft.dao.entity.FilePO;
import com.nft.service.dto.FileDTO;

public interface NFTService {

    /**
     * 上传文件
     * @param filePO
     * @return
     */
    int upload(FilePO filePO);

    /**
     * 保存文件
     * @param filePO
     * @return
     */
    int save(FilePO filePO);

    /**
     * 发布文件
     * @param pubVO
     * @return
     */
    int pub(PubVO pubVO);

    /**
     * 付费
     * @param pubVO
     * @return
     */
    int pay(PubVO pubVO);

    /**
     * 获取一个文件
     * @param filePO
     * @return
     */
    FileDTO getFileDetail(FilePO filePO);

    /**
     * 获取一个文件
     * @param filePO
     * @return
     */
    FileDTO getFile(FilePO filePO);

    /**
     * 查询当前用户的所有文件
     * @param userTag
     * @param page
     * @param pageSize
     * @return
     */
    IPage<FilePO> selectFiles(String userTag, int page, int pageSize);
}
