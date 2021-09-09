package com.nft.controller;

import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.OrderVO;
import com.nft.dao.entity.OrderPO;
import com.nft.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description: nft
 * Created by moloq on 2021/9/9 16:53
 */
@Api(value = "order", tags = "order api")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @ApiOperation("update order")
    @PostMapping("/updateOrder")
    public ResultVO updateOrder(OrderPO orderPO) {
        int updateOrder = orderService.updateOrder(orderPO);
        if (updateOrder < 1) {
            return ResultVO.fail("update order fail");
        }
        return ResultVO.successMsg("update order success");
    }

    @ApiOperation("query order")
    @PostMapping("/queryOrder")
    public ResultVO queryOrder(OrderVO orderVO) {
        return orderService.queryOrder(orderVO);
    }
}
