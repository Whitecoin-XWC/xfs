package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.AuctionHistoryEntity;
import com.nft.dao.mapper.AuctionHistoryMapper;
import com.nft.dao.mapper.AuctionMapper;
import com.nft.service.AuctionHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description: nft
 * Created by moloq on 2021/6/28 11:38
 */
@Service
public class AuctionHistoryServiceImpl implements AuctionHistoryService {

    @Resource
    private AuctionHistoryMapper auctionHistoryMapper;
    @Resource
    private AuctionMapper auctionMapper;

    @Override
    public int insertAuctionHistory(AuctionHistoryEntity historyEntity) {
        historyEntity.setAuctionMaxPrice(historyEntity.getAuctionPrice());
        historyEntity.setAuctionTime(new Date());
        return auctionHistoryMapper.insert(historyEntity);
    }

    @Override
    public List<AuctionHistoryEntity> queryAll(String mediaId, String auctionId) {
        QueryWrapper<AuctionEntity> query = new QueryWrapper<>();
        query.eq("auction_id", auctionId);
        query.orderByDesc("create_time");
        List<AuctionEntity> auctionEntities = auctionMapper.selectList(query);
        long id = 0;
        if (!CollectionUtils.isEmpty(auctionEntities)) {
            id = auctionEntities.get(0).getId();
        }

        QueryWrapper<AuctionHistoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", mediaId);
        if (id != 0) {
            queryWrapper.eq("auction_id", id);
        }
        return auctionHistoryMapper.selectList(queryWrapper);
    }

    @Override
    public int count(Long auctionId) {
        QueryWrapper<AuctionHistoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("auction_id", auctionId);
        return auctionHistoryMapper.selectCount(queryWrapper);
    }
}
