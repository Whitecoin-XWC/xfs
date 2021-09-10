package com.nft.controller;

import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.ShopVo;
import com.nft.dao.entity.ShopPO;
import com.nft.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description: nft
 * Created by moloq on 2021/9/10 9:20
 */
@RestController
@RequestMapping("/shop")
@Api(value = "shop api", tags = "shop api")
public class ShopController {

    @Resource
    private ShopService shopService;

    @PostMapping("/saveShop")
    @ApiOperation("save shop")
    public ResultVO saveShop(@RequestBody ShopPO shopPO) {
        int saveShop = shopService.saveShop(shopPO);
        if (saveShop < 1) {
            return ResultVO.fail("add shop fail");
        }
        return ResultVO.successMsg("add shop success");
    }

    @PostMapping("/updateShop")
    @ApiOperation("update shop")
    public ResultVO updateShop(@RequestBody ShopPO shopPO) {
        int updateShop = shopService.updateShop(shopPO);
        if (updateShop < 1) {
            return ResultVO.fail("update shop fail");
        }
        return ResultVO.successMsg("update shop success");
    }

    @PostMapping("/queryShop")
    @ApiOperation("query shop")
    public ResultVO queryShop(@RequestBody ShopVo shopVo) {
        return shopService.queryShop(shopVo);
    }
}
