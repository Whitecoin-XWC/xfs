package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.bean.AuctionStatus;
import com.nft.commons.util.LogUtil;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.FileLogPO;
import com.nft.dao.mapper.AuctionMapper;
import com.nft.dao.mapper.FileLogMapper;
import com.nft.service.AuctionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description: nft
 * Created by moloq on 2021/6/28 11:35
 */
@Service
public class AuctionServiceImpl extends ServiceImpl<AuctionMapper, AuctionEntity> implements AuctionService {

    @Resource
    private AuctionMapper auctionMapper;
    @Resource
    private FileLogMapper fileLogMapper;

    @Override
    public int insertAuction(AuctionEntity auctionEntity) {
        auctionEntity.setAuctionStatus(AuctionStatus.AUCTION_START.getStatuCode());
        auctionEntity.setCreateTime(new Date());
        int insert = auctionMapper.insert(auctionEntity);
        if (insert > 0) {
            saveLog(auctionEntity.getFileTokenId(), auctionEntity.getAuctionCreater(), "创建拍卖");
        }
        return insert;
    }

    @Override
    public int updateAuction(AuctionEntity update) {
        update.setUpdateTime(new Date());
        return auctionMapper.updateById(update);
    }

    @Override
    public AuctionEntity queryAuction(AuctionEntity query) {
        QueryWrapper<AuctionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_token_id", query.getFileTokenId());
        queryWrapper.eq("auction_id", query.getAuctionId());
        queryWrapper.orderByDesc("create_time");
        List<AuctionEntity> auctionEntities = auctionMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(auctionEntities)) {
            return new AuctionEntity();
        }
        return auctionEntities.get(0);
    }

    @Override
    public void saveLog(String fileId, String userTag, String action) {
        FileLogPO fileLogPO = new FileLogPO();
        fileLogPO.setFileId(fileId);
        fileLogPO.setLogInfo(LogUtil.getLogInfo(userTag, action));
        fileLogPO.setCreateTime(new Date());
        fileLogMapper.insert(fileLogPO);
    }
}
