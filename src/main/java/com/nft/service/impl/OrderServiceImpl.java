package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.commons.vo.PageResultVO;
import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.OrderVO;
import com.nft.dao.entity.OrderPO;
import com.nft.dao.entity.UserAddressPO;
import com.nft.dao.mapper.OrderMapper;
import com.nft.dao.mapper.UserAddressMapper;
import com.nft.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description: nft
 * Created by moloq on 2021/9/9 16:36
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private UserAddressMapper userAddressMapper;

    @Override
    public int saveOrder(OrderPO orderPO) {
        String userAddress = orderPO.getUserAddress();
        UserAddressPO userAddressPO = userAddressMapper.selectById(userAddress);
        if (userAddressPO == null){
            /* not found user address */
            return 0;
        }
        orderPO.setAddress(userAddressPO.getAddress());
        orderPO.setOrderStatus("未发货");
        orderPO.setCreateTime(new Date());
        orderPO.setName(userAddressPO.getName());
        orderPO.setPhone(userAddressPO.getPhone());
        orderPO.setZipCode(userAddressPO.getZipCode());
        return orderMapper.insert(orderPO);
    }

    @Override
    public int updateOrder(OrderPO orderPO) {
        return orderMapper.updateById(orderPO);
    }

    @Override
    public ResultVO queryOrder(OrderVO orderVO) {
        QueryWrapper<OrderPO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(orderVO.getOrderStatus())) {
            queryWrapper.eq("order_status", orderVO.getOrderStatus());
        }
        if (StringUtils.isNotBlank(orderVO.getUserAddress())) {
            queryWrapper.eq("user_address", orderVO.getUserAddress());
        }
        Page<OrderPO> orderPOPage = orderMapper.selectPage(new Page<>(orderVO.getPage(), orderVO.getPageSize()), queryWrapper);
        PageResultVO resultVO = new PageResultVO();
        resultVO.setCount(orderPOPage.getTotal());
        resultVO.setCurrentPage(orderPOPage.getCurrent());
        resultVO.setPageSize(orderPOPage.getSize());
        resultVO.setPageTotal(orderPOPage.getPages());
        resultVO.setRecords(orderPOPage.getRecords());
        return ResultVO.success(resultVO);
    }
}
