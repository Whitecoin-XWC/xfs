package com.nft.service;

import com.nft.controller.vo.BuyVO;
import com.nft.controller.vo.SellVO;

public interface SellService {

    int sell(SellVO sellVO);

    int delSell(SellVO sellVO);

    int buySuccess(BuyVO buyVO);
}
