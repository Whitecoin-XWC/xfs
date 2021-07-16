package com.nft.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.controller.vo.*;
import com.nft.dao.entity.FileLogPO;
import com.nft.dao.entity.FilePO;
import com.nft.dao.entity.NoticeEntity;
import com.nft.service.dto.FileResultDTO;
import com.nft.service.dto.NoticeResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface NFTService {

    /**
     * 关注
     * @param followVO
     * @return
     */
    int follow(FollowVO followVO);

    /**
     * 取消关注
     * @param followVO
     * @return
     */
    int delFollow(FollowVO followVO);

    /**
     * 上传文件
     * @param filePO
     * @return
     */
    int upload(FilePO filePO);

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
     * 转移
     * @return
     */
    int fileUserChange(FileUserChangeVO fileUserChangeVO);

    /**
     * 获取一个文件
     * @param filePO
     * @return
     */
    FilePO getFileDetail(FilePO filePO);

    /**
     * 获取一个文件
     * @param fileLogVO
     * @return
     */
    IPage<FileLogPO> getFileLog(FileLogVO fileLogVO);

    /**
     * 查询本站的所有文件
     * @return
     */
    IPage<FileResultDTO> selectFiles(FileVO fileVO);

    /**
     * 搜索
     * @param keyWord
     * @return
     */
    Map<String,Object> search(String keyWord);

    NoticeResult getNotice(NoticeVo noticeVo);

    void insertCopyrightFeeNotice(String fileId, String buyUserAddress, BigDecimal price,String coinType);

    void insertAuctionNotice(long auctionId,String fileId,String auctionner);
}
