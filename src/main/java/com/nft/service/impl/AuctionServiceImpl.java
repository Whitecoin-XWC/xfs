package com.nft.service.impl;

import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.mapper.AuctionMapper;
import com.nft.service.AuctionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description: nft
 * Created by moloq on 2021/6/28 11:35
 */
@Service
public class AuctionServiceImpl implements AuctionService {

    @Resource
    private AuctionMapper auctionMapper;

    @Override
    public int insertAuction(AuctionEntity auctionEntity) {
        return auctionMapper.insert(auctionEntity);
    }

    @Override
    public int updateAuction(AuctionEntity update) {
        return auctionMapper.updateById(update);
    }
}
