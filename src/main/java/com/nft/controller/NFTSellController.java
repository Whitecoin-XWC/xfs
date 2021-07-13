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

@Api(value = "文件出售", tags = "文件出售接口")
@Slf4j
@RestController
@RequestMapping("nftSell")
public class NFTSellController {

    @Resource
    private SellService sellService;


    /**
     * 设置/更新售价
     * @return
     */
    @ApiOperation("设置/更新售价")
    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public ResultVO sell(@RequestBody SellVO sellVO){
        try {
            sellService.sell(sellVO);
            return ResultVO.successMsg("设置成功");
        }catch (Exception e){
            log.error("设置/更新售价异常", e);
            return ResultVO.fail("设置/更新售价异常");
        }
    }

    /**
     * 取消售价
     * @return
     */
    @ApiOperation("取消售价")
    @RequestMapping(value = "/delSell", method = RequestMethod.POST)
    public ResultVO delSell(@RequestBody SellVO sellVO){
        try {
            sellService.delSell(sellVO);
            return ResultVO.successMsg("取消售价成功");
        }catch (Exception e){
            log.error("取消售价异常", e);
            return ResultVO.fail("取消售价异常");
        }
    }

    /**
     * 购买成功
     * @return
     */
    @ApiOperation("购买成功")
    @RequestMapping(value = "/buySuccess", method = RequestMethod.POST)
    public ResultVO buySuccess(@RequestBody BuyVO buyVO){
        try {
            sellService.buySuccess(buyVO);
            return ResultVO.successMsg("购买成功");
        }catch (Exception e){
            log.error("购买异常", e);
            return ResultVO.fail("购买异常");
        }
    }
}
