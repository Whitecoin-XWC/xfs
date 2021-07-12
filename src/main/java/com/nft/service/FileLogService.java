package com.nft.service;

import com.nft.service.dto.FileLogAttach;

public interface FileLogService {

    /**
     * 保存日志
     * @param fileId  文件ID，也就是文件的tokenId
     * @param action 日志动作描述，比如发布了nft，出售了nft
     * @param type 0 普通变化，1 竞拍，2 出售
     * @param fileLogAttach 日志携带的附件，用于记录更多的数据
     * @return
     */
    int saveLog(String fileId, String action, Integer type, FileLogAttach fileLogAttach);
}
