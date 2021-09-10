package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.commons.vo.PageResultVO;
import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.ShopVo;
import com.nft.dao.entity.ShopPO;
import com.nft.dao.mapper.ShopMapper;
import com.nft.service.ShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description: nft
 * Created by moloq on 2021/9/10 9:21
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Resource
    private ShopMapper shopMapper;

    @Override
    public int saveShop(ShopPO shopPO) {
        return shopMapper.insert(shopPO);
    }

    @Override
    public int updateShop(ShopPO shopPO) {
        return shopMapper.updateById(shopPO);
    }

    @Override
    public ResultVO queryShop(ShopVo shopVo) {
        Page<ShopPO> shopPOPage = shopMapper.selectPage(new Page<>(shopVo.getPage(), shopVo.getPageSize()), new QueryWrapper<>());
        PageResultVO resultVO = new PageResultVO();
        resultVO.setCount(shopPOPage.getTotal());
        resultVO.setCurrentPage(shopPOPage.getCurrent());
        resultVO.setPageSize(shopPOPage.getSize());
        resultVO.setPageTotal(shopPOPage.getPages());
        resultVO.setRecords(shopPOPage.getRecords());
        return ResultVO.success(resultVO);
    }
}
