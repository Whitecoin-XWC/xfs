package com.nft.controller;

import com.nft.bean.AuctionStatus;
import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.SellVO;
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
@Api(value = "auction", tags = "auction api")
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

    @ApiOperation("create auction record")
    @PostMapping("/create")
    public ResultVO create(@RequestBody AuctionEntity auctionEntity) {
        log.info("create auction record start, :{}", auctionEntity.getFileTokenId());
        try {
            int insert = auctionService.insertAuction(auctionEntity);
            if (insert > 0) {
                fileLogService.saveLog(auctionEntity.getFileTokenId(), "创建拍卖", auctionEntity.getAuctionCreater(), 1, new FileLogAttach(auctionEntity.getTradeId(), auctionEntity.getAuctionRetainPrice(), auctionEntity.getAuctionCoin()));
            }
            log.info("create auction record end, :{}", auctionEntity.getFileTokenId());
            return ResultVO.successMsg("create auction record success");
        } catch (Exception e) {
            log.error("create auction fail, :{},{}", auctionEntity.getFileTokenId(), e);
            return ResultVO.fail("create auction record fail");
        }
    }


    @ApiOperation("update auction record")
    @PostMapping("/update")
    public ResultVO update(@RequestBody AuctionEntity update) {
        log.info("update auction record start: {}", update.getFileTokenId());
        try {
            int count = auctionService.updateAuction(update);
            if (count > 0) {
                AuctionEntity auctionEntity = auctionService.getById(update.getId());
                FileLogAttach fileLogAttach = new FileLogAttach(update.getTradeId());
                if (update.getAuctionRetainPrice() != null) {
                    fileLogAttach.setPrice(update.getAuctionRetainPrice());
                    fileLogAttach.setCoinType(update.getAuctionCoin());
                }
                fileLogService.saveLog(auctionEntity.getFileTokenId(), "修改拍卖记录", auctionEntity.getAuctionCreater(), 1, fileLogAttach);
                return ResultVO.successMsg("update auction record success");
            }
            log.info("update auction record end: {}", update.getFileTokenId());
            return ResultVO.fail("update auction record fail");
        } catch (Exception e) {
            log.error("update auction fail: {}, {}", update.getFileTokenId(), e);
            return ResultVO.fail("update auction record fail");
        }
    }

    @ApiOperation("cancel auction record")
    @PostMapping("/cancel")
    public ResultVO cancelAuction(@RequestBody AuctionEntity auctionEntity) {
        log.info("cancel auction record start: {}", auctionEntity.getFileTokenId());
        try {
            AuctionEntity query = auctionService.queryAuction(auctionEntity.getFileTokenId());
            if (query == null || query.getAuctionStatus() != 0) {
                return ResultVO.fail("竞拍中,不允许取消拍卖");
            }
            String fileTokenId = auctionEntity.getFileTokenId();
            auctionService.cancelAuction(fileTokenId, query.getId());
            fileLogService.saveLog(fileTokenId, "取消拍卖", query.getAuctionCreater(), 1, new FileLogAttach(auctionEntity.getTradeId()));
            log.info("cancel auction record end: {}", auctionEntity.getFileTokenId());
            return ResultVO.successMsg("取消成功");
        } catch (Exception e) {
            log.error("cancel auction fail: {}, {}", auctionEntity.getFileTokenId(), e);
            return ResultVO.fail("取消拍卖失败");
        }
    }

    @ApiOperation("receive nft")
    @PostMapping("/receive")
    public ResultVO receive(@RequestBody ReceivePO receivePO) {
        log.info("receive nft start: {}", receivePO.getFileTokenId());
        try {
            String fileTokenId = receivePO.getFileTokenId();
            String userAddress = receivePO.getUserAddress();
            AuctionEntity query = auctionService.queryAuction(fileTokenId);
            if (query == null || query.getAuctionStatus() != 2) {
                return ResultVO.fail("竞拍未结束,不能领取");
            }
            String receive = auctionService.receive(fileTokenId, userAddress, query.getId());
            fileLogService.saveLog(fileTokenId, "领取了NFT", userAddress, 1, new FileLogAttach("", query.getAuctionMaxPrice(), query.getAuctionCoin()));
            /* 插入版权费通知 */
            noticeService.insertCopyrightFeeNotice(fileTokenId, userAddress, query.getAuctionMaxPrice(), query.getAuctionCoin());
            log.info("receive nft success: {}", receivePO.getFileTokenId());
            return ResultVO.successMsg(receive);
        } catch (Exception e) {
            log.error("receive nft fail: {}, {}", receivePO.getFileTokenId(), e);
            return ResultVO.fail("领取失败");
        }
    }

    @ApiOperation("query latest auction record")
    @PostMapping("/query")
    public ResultVO query(@RequestBody AuctionEntity query) {
        try {
            AuctionEntity auctionEntity = auctionService.queryAuction(query.getFileTokenId());
            return ResultVO.success(auctionEntity);
        } catch (Exception e) {
            log.error("update auction fail: {}", e);
            return ResultVO.fail("获取最新的拍卖记录失败");
        }
    }

    @ApiOperation("add bid record")
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

    @ApiOperation("query all bid records")
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
