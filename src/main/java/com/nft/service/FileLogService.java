package com.nft.service;

import com.nft.service.dto.FileLogAttach;

public interface FileLogService {

    /**
     * save log
     * @param fileId  tokenId
     * @param action
     * @param userAddress
     * @param type 0 普通变化，1 竞拍，2 出售
     * @param fileLogAttach
     * @return
     */
    int saveLog(String fileId, String action, String userAddress, Integer type, FileLogAttach fileLogAttach);
}
