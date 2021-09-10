package com.nft.service;

import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.ShopVo;
import com.nft.dao.entity.ShopPO;

/**
 * Description: nft
 * Created by moloq on 2021/9/10 9:21
 */
public interface ShopService {
    int saveShop(ShopPO shopPO);

    int updateShop(ShopPO shopPO);

    ResultVO queryShop(ShopVo shopVo);
}
