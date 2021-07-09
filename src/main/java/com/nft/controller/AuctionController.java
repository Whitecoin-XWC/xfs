package com.nft.controller;

import com.nft.bean.AuctionStatus;
import com.nft.commons.vo.ResultVO;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.AuctionHistoryEntity;
import com.nft.service.AuctionHistoryService;
import com.nft.service.AuctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation("新增拍卖记录")
    @PostMapping("/create")
    public ResultVO create(@RequestBody AuctionEntity auctionEntity) {
        try {
            int insert = auctionService.insertAuction(auctionEntity);
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
            return ResultVO.successMsg("更新记录成功");
        } catch (Exception e) {
            log.error("update auction fail,{}", e);
            return ResultVO.fail("更新拍卖记录失败");
        }
    }

    @ApiOperation("获取最新的拍卖记录")
    @PostMapping("/query")
    public ResultVO query(@RequestBody AuctionEntity query) {
        try {
            AuctionEntity auctionEntity = auctionService.queryAuction(query);
            return ResultVO.success(auctionEntity);
        } catch (Exception e) {
            log.error("update auction fail,{}", e);
            return ResultVO.fail("更新拍卖记录失败");
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
                auctionService.saveLog(historyEntity.getFileId(), historyEntity.getAuctioneer(), "开始拍卖");
            }
            update.setId(historyEntity.getAuctionId());
            update.setUpdateTime(new Date());
            update.setAuctionMaxPrice(historyEntity.getAuctionMaxPrice());
            update.setAuctionMaxEr(historyEntity.getAuctioneer());
            auctionService.updateAuction(update);

            return ResultVO.successMsg("插入拍卖成功");
        } catch (Exception e) {
            log.error("create history fail,{}", e);
            return ResultVO.fail("插入拍卖记录失败");
        }
    }

    @ApiOperation("查询所有的出价记录")
    @GetMapping("/history/query")
    public ResultVO queryHistory(String mediaId,String auctionId) {
        try {
            List<AuctionHistoryEntity> result = auctionHistoryService.queryAll(mediaId,auctionId);
            return ResultVO.success(result);
        } catch (Exception e) {
            log.error("query auction history fail,{}", e);
            return ResultVO.fail("查询拍卖记录失败");
        }
    }
}
