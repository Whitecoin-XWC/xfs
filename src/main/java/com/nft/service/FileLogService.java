package com.nft.service;

import com.nft.service.dto.FileLogAttach;

public interface FileLogService {

    /**
     * 保存日志
     * @param fileId
     * @param action
     * @param type
     * @return
     */
    int saveLog(String fileId, String action, Integer type, FileLogAttach fileLogAttach);
}
