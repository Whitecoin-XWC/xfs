package com.nft.tasks;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.mapper.AuctionMapper;
import com.nft.service.AuctionService;
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

    /**
     * 修改拍卖倒计时结束的记录
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void syncAuctionEnd() {
        QueryWrapper<AuctionEntity> queryWrapper = new QueryWrapper<>();
        LocalDateTime time = LocalDateTime.now().minusHours(24);
        queryWrapper.ge("auction_start_time", time);
        List<AuctionEntity> auctionEntities = auctionService.list(queryWrapper);
        for (AuctionEntity auctionEntity : auctionEntities) {
            auctionEntity.setAuctionStatus(AUCTION_END.getStatuCode());
        }
        auctionService.updateBatchById(auctionEntities);
    }
}
