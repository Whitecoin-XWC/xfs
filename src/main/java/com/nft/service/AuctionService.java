package com.nft.service;

import com.nft.dao.entity.AuctionEntity;

/**
 * Description: nft
 * Created by moloq on 2021/6/28 11:34
 */
public interface AuctionService{
    int insertAuction(AuctionEntity auctionEntity);

    int updateAuction(AuctionEntity update);
}
