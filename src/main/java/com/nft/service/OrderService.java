package com.nft.service;

import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.OrderVO;
import com.nft.dao.entity.OrderPO;

import java.util.List;

/**
 * Description: nft
 * Created by moloq on 2021/9/9 16:32
 */
public interface OrderService {
    int saveOrder(OrderPO orderPO);

    int updateOrder(OrderPO orderPO);

    ResultVO queryOrder(OrderVO orderVO);
}
