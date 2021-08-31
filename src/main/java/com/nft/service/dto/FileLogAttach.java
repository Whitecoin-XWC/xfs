package com.nft.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 日志附件，用于记录更多数据
 * 可以自己添加字段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileLogAttach {

    /**
     * 交易ID
     */
    private String tractionId;

    /**
     * 价格
     */
    private BigDecimal price;
}
