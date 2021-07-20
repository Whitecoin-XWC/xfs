package com.nft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.controller.vo.NoticeVo;
import com.nft.dao.entity.NoticeEntity;
import com.nft.service.dto.NoticeResult;

import java.math.BigDecimal;

/**
 * Description: nft
 * Created by moloq on 2021/7/19 10:25
 */
public interface NoticeService extends IService<NoticeEntity> {

    int getNoticeCount(String userAddress);

    NoticeResult getNotice(NoticeVo noticeVo);

    void insertCopyrightFeeNotice(String fileId, String buyUserAddress, BigDecimal price, String coinType);

    void insertAuctionNotice(long auctionId,String fileId,String auctionner);
}
