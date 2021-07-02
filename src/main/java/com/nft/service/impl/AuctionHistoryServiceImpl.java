package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nft.dao.entity.AuctionHistoryEntity;
import com.nft.dao.mapper.AuctionHistoryMapper;
import com.nft.service.AuctionHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: nft
 * Created by moloq on 2021/6/28 11:38
 */
@Service
public class AuctionHistoryServiceImpl implements AuctionHistoryService {

    @Resource
    private AuctionHistoryMapper auctionHistoryMapper;

    @Override
    public int insertAuctionHistory(AuctionHistoryEntity historyEntity) {
        return auctionHistoryMapper.insert(historyEntity);
    }

    @Override
    public List<AuctionHistoryEntity> queryAll(String mediaId) {
        return auctionHistoryMapper.selectList(new QueryWrapper());
    }
}
