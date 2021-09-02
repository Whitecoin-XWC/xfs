package com.nft.service;

import com.nft.commons.vo.ResultVO;
import com.nft.dao.entity.MessageEntity;

/**
 * Description: nft
 * Created by moloq on 2021/9/2 14:01
 */
public interface MessageService {
    ResultVO addMessage(MessageEntity messageEntity);

    ResultVO queryMessage(String tokenId,int pageNo,int pageSize);
}
