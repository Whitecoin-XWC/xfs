package com.nft.commons.vo;

import lombok.Data;

@Data
public class PageResultVO {

    /**
     * 当前页
     */
    private long currentPage;
    /**
     * 每页条数
     */
    private long pageSize;
    /**
     * 总条数
     */
    private long count;
    /**
     * 总页数
     */
    private long pageTotal;
    /**
     * 图片url前缀
     */
    private String imgUrl;
    /**
     * 数据
     */
    private Object records;
}
