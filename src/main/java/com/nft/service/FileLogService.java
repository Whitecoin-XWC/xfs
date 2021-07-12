package com.nft.service;

import com.alibaba.fastjson.JSONObject;
import com.nft.service.dto.FileLogMessage;

public interface FileLogService {

    /**
     * 保存日志
     * @param fileId
     * @param action
     * @param type
     * @return
     */
    int saveLog(String fileId, String action, Integer type, FileLogMessage fileLogMessage);
}
