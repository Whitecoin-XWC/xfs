package com.nft.tasks;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.AuctionHistoryEntity;
import com.nft.dao.mapper.AuctionMapper;
import com.nft.service.AuctionHistoryService;
import com.nft.service.AuctionService;
import com.nft.service.FileLogService;
import com.nft.service.dto.FileLogAttach;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.nft.bean.AuctionStatus.AUCTION_END;

/**
 * Description: nft
 * Created by moloq on 2021/7/8 13:53
 */
@Component
public class AuctionTask {

    @Resource
    private AuctionService auctionService;

    @Resource
    private FileLogService fileLogService;

    /**
     * 修改拍卖倒计时结束的记录
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void syncAuctionEnd() {
        QueryWrapper<AuctionEntity> queryWrapper = new QueryWrapper<>();
//        LocalDateTime time = LocalDateTime.now().minusHours(24);
        //TODO 修改拍卖时长
        LocalDateTime time = LocalDateTime.now().minusMinutes(10);
        queryWrapper.le("auction_start_time", time);
        queryWrapper.eq("auction_status", 1);
        List<AuctionEntity> auctionEntities = auctionService.list(queryWrapper);
        for (AuctionEntity auctionEntity : auctionEntities) {
            auctionEntity.setAuctionStatus(AUCTION_END.getStatuCode());
            auctionEntity.setUpdateTime(new Date());
        }
        auctionService.updateBatchById(auctionEntities);
        /* 插入拍卖结束的记录 */
        for (AuctionEntity auctionEntity : auctionEntities) {
            fileLogService.saveLog(auctionEntity.getFileTokenId(), "拍卖结束", "", 1, new FileLogAttach());
        }
    }
}
