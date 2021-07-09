package com.nft.service;

import com.nft.dao.entity.AuctionHistoryEntity;

import java.util.List;

/**
 * Description: nft
 * Created by moloq on 2021/6/28 11:35
 */
public interface AuctionHistoryService {
    int insertAuctionHistory(AuctionHistoryEntity historyEntity);

    List<AuctionHistoryEntity> queryAll(String mediaId,String auctionId);

    int count(Long auctionId);
}
