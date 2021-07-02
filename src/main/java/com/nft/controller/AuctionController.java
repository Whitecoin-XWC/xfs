package com.nft.controller;

import com.nft.commons.vo.ResultVO;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.AuctionHistoryEntity;
import com.nft.service.AuctionHistoryService;
import com.nft.service.AuctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private AuctionService auctionService;
    @Autowired
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


    @ApiOperation("修改拍卖记录的日志")
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

    @ApiOperation("新增一条拍卖出价记录")
    @PostMapping("/history/create")
    public ResultVO createHistory(@RequestBody AuctionHistoryEntity historyEntity) {
        try {
            int count = auctionHistoryService.insertAuctionHistory(historyEntity);
            return ResultVO.successMsg("插入拍卖成功");
        } catch (Exception e) {
            log.error("create history fail,{}", e);
            return ResultVO.fail("插入拍卖记录失败");
        }
    }

    @ApiOperation("查询所有的拍卖出价记录")
    @GetMapping("/history/query")
    public ResultVO queryHistory(String mediaId) {
        try {
            List<AuctionHistoryEntity> result = auctionHistoryService.queryAll(mediaId);
            return ResultVO.success(result);
        } catch (Exception e) {
            log.error("query auction history fail,{}", e);
            return ResultVO.fail("查询拍卖记录失败");
        }
    }
}
