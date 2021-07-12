package com.nft.service.dto;

import lombok.Data;

/**
 * 日志附件，用于记录更多数据
 * 可以自己添加字段
 */
@Data
public class FileLogAttach {

    /**
     * 交易ID
     */
    private String tractionId;
}
