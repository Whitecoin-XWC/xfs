package com.nft.controller;

import com.nft.bean.AuctionStatus;
import com.nft.commons.vo.ResultVO;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.AuctionHistoryEntity;
import com.nft.dao.entity.ReceivePO;
import com.nft.service.*;
import com.nft.service.dto.FileLogAttach;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description: nft
 * Created by moloq on 2021/6/17 9:35
 */
@Api(value = "拍卖", tags = "拍卖接口")
@Slf4j
@RestController
@RequestMapping("/auction")
public class AuctionController {
    @Resource
    private AuctionService auctionService;
    @Resource
    private AuctionHistoryService auctionHistoryService;
    @Resource
    private FileLogService fileLogService;
    @Resource
    private NoticeService noticeService;

    @ApiOperation("新增拍卖记录")
    @PostMapping("/create")
    public ResultVO create(@RequestBody AuctionEntity auctionEntity) {
        try {
            int insert = auctionService.insertAuction(auctionEntity);
            if (insert > 0) {
                fileLogService.saveLog(auctionEntity.getFileTokenId(), "创建拍卖", auctionEntity.getAuctionCreater(), 1, new FileLogAttach(auctionEntity.getTradeId()));
            }
            return ResultVO.successMsg("插入拍卖记录成功");
        } catch (Exception e) {
            log.error("create auction fail,{}", e);
            return ResultVO.fail("插入记录失败");
        }
    }


    @ApiOperation("修改拍卖记录")
    @PostMapping("/update")
    public ResultVO update(@RequestBody AuctionEntity update) {
        try {
            int count = auctionService.updateAuction(update);
            if (count > 0) {
                AuctionEntity auctionEntity = auctionService.getById(update.getId());
                fileLogService.saveLog(auctionEntity.getFileTokenId(), "修改拍卖记录", auctionEntity.getAuctionCreater(), 1, new FileLogAttach(update.getTradeId()));
                return ResultVO.successMsg("更新记录成功");
            }
            return ResultVO.fail("更新拍卖记录失败");
        } catch (Exception e) {
            log.error("update auction fail,{}", e);
            return ResultVO.fail("更新拍卖记录失败");
        }
    }

    @ApiOperation("取消拍卖")
    @PostMapping("/cancel")
    public ResultVO cancelAuction(@RequestBody AuctionEntity auctionEntity) {
        try {
            AuctionEntity query = auctionService.queryAuction(auctionEntity.getFileTokenId());
            if (query == null || query.getAuctionStatus() != 0) {
                return ResultVO.fail("竞拍中,不允许取消拍卖");
            }
            String fileTokenId = auctionEntity.getFileTokenId();
            auctionService.cancelAuction(fileTokenId);
            fileLogService.saveLog(fileTokenId, "取消拍卖", query.getAuctionCreater(), 1, new FileLogAttach(auctionEntity.getTradeId()));
            return ResultVO.successMsg("取消成功");
        } catch (Exception e) {
            log.error("cancel auction fail,{}", e);
            return ResultVO.fail("取消拍卖失败");
        }
    }

    @ApiOperation("领取成功")
    @PostMapping("/receive")
    public ResultVO receive(@RequestBody ReceivePO receivePO) {
        try {
            String fileTokenId = receivePO.getFileTokenId();
            String userAddress = receivePO.getUserAddress();
            AuctionEntity query = auctionService.queryAuction(fileTokenId);
            if (query == null || query.getAuctionStatus() != 2) {
                return ResultVO.fail("竞拍未结束,不能领取");
            }
            String receive = auctionService.receive(fileTokenId, userAddress, query.getId());
            fileLogService.saveLog(fileTokenId, "领取了nft", userAddress, 1, new FileLogAttach());
            /* 插入版权费通知 */
            noticeService.insertCopyrightFeeNotice(fileTokenId, userAddress, query.getAuctionMaxPrice(), query.getAuctionCoin());
            return ResultVO.successMsg(receive);
        } catch (Exception e) {
            log.error("cancel auction fail,{}", e);
            return ResultVO.fail("领取失败");
        }
    }

    @ApiOperation("获取最新的拍卖记录")
    @PostMapping("/query")
    public ResultVO query(@RequestBody AuctionEntity query) {
        try {
            AuctionEntity auctionEntity = auctionService.queryAuction(query.getFileTokenId());
            return ResultVO.success(auctionEntity);
        } catch (Exception e) {
            log.error("update auction fail,{}", e);
            return ResultVO.fail("获取最新的拍卖记录失败");
        }
    }

    @ApiOperation("新增一条拍卖出价记录")
    @PostMapping("/history/create")
    public ResultVO createHistory(@RequestBody AuctionHistoryEntity historyEntity) {
        try {
            //查询是不是第一次出价
            int count = auctionHistoryService.count(historyEntity.getAuctionId());

            int insert = auctionHistoryService.insertAuctionHistory(historyEntity);
            // 修改拍卖记录出价最高者
            AuctionEntity update = new AuctionEntity();
            if (count == 0 && insert > 0) {
                update.setAuctionStatus(AuctionStatus.AUCTIONING.getStatuCode());
                update.setAuctionStartTime(new Date());
//                fileLogService.saveLog(historyEntity.getFileId(), historyEntity.getAuctioneer() + "开始拍卖", 1, new FileLogAttach(historyEntity.getTradeId()));
            }
            update.setId(historyEntity.getAuctionId());
            update.setUpdateTime(new Date());
            update.setAuctionMaxPrice(historyEntity.getAuctionMaxPrice());
            update.setAuctionMaxEr(historyEntity.getAuctioneer());
            auctionService.updateAuction(update);

            /* 插入拍卖的通知记录 */
            noticeService.insertAuctionNotice(historyEntity.getAuctionId(), historyEntity.getFileId(), historyEntity.getAuctioneer());
            return ResultVO.successMsg("插入拍卖成功");
        } catch (Exception e) {
            log.error("create history fail,{}", e);
            return ResultVO.fail("插入拍卖记录失败");
        }
    }

    @ApiOperation("查询所有的出价记录")
    @GetMapping("/history/query")
    public ResultVO queryHistory(String mediaId, String auctionId) {
        try {
            List<AuctionHistoryEntity> result = auctionHistoryService.queryAll(mediaId, auctionId);
            return ResultVO.success(result);
        } catch (Exception e) {
            log.error("query auction history fail,{}", e);
            return ResultVO.fail("查询拍卖记录失败");
        }
    }
}
