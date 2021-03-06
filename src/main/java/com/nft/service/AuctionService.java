package com.nft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nft.dao.entity.AuctionEntity;

import java.math.BigDecimal;

/**
 * Description: nft
 * Created by moloq on 2021/6/28 11:34
 */
public interface AuctionService extends IService<AuctionEntity> {
    int insertAuction(AuctionEntity auctionEntity);

    int updateAuction(AuctionEntity update);

    AuctionEntity queryAuction(String fileTokenId);

    int cancelAuction(String fileTokenId,long id);

    String receive(String fileTokenId,String userAddress,long auctionId);

    BigDecimal getCoinPrice(String coin);
}
