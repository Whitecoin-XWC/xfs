package com.nft.controller;

import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.BuyVO;
import com.nft.controller.vo.SellVO;
import com.nft.service.SellService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "sell file", tags = "sell file api")
@Slf4j
@RestController
@RequestMapping("nftSell")
public class NFTSellController {

    @Resource
    private SellService sellService;


    /**
     * set or update sell price
     *
     * @return
     */
    @ApiOperation("set or update sell price")
    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public ResultVO sell(@RequestBody SellVO sellVO) {
        log.info("set or update sell price start :{}", sellVO.getTokenId());
        try {
            sellService.sell(sellVO);
            log.info("set or update sell price success :{}", sellVO.getTokenId());
            return ResultVO.successMsg("设置成功");
        } catch (Exception e) {
            log.error("set or update sell price has exception :{},{}", sellVO.getTokenId(), e);
            return ResultVO.fail("设置/更新售价异常");
        }
    }

    /**
     * chancel price
     *
     * @return
     */
    @ApiOperation("取消售价")
    @RequestMapping(value = "/delSell", method = RequestMethod.POST)
    public ResultVO delSell(@RequestBody SellVO sellVO) {
        try {
            sellService.delSell(sellVO);
            return ResultVO.successMsg("取消售价成功");
        } catch (Exception e) {
            log.error("取消售价异常", e);
            return ResultVO.fail("取消售价异常");
        }
    }

    /**
     * buy success
     *
     * @return
     */
    @ApiOperation("buy success")
    @RequestMapping(value = "/buySuccess", method = RequestMethod.POST)
    public ResultVO buySuccess(@RequestBody BuyVO buyVO) {
        log.info("buy nft start :{}", buyVO.getTokenId());
        try {
            int success = sellService.buySuccess(buyVO);
            if (success == 0) {
                log.info("buy nft success :{}", buyVO.getTokenId());
                return ResultVO.successMsg("购买成功");
            }
            log.info("buy nft fail :{}", buyVO.getTokenId());
            return ResultVO.fail("购买异常");
        } catch (Exception e) {
            log.error("buy nft has exception :{},{}", buyVO.getTokenId(), e);
            return ResultVO.fail("购买异常");
        }
    }
}
