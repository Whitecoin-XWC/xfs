package com.nft.bean;

/**
 * Description: nft
 * Created by moloq on 2021/7/8 14:02
 */
public enum AuctionStatus {
    AUCTION_START(0,"创建拍卖，未开始竞拍"),
    AUCTIONING(1,"竞拍中"),
    AUCTION_END(2,"竞拍结束，待领取"),
    RECEIVED(3,"领取完成");
    private int statuCode;
    private String statu;

    AuctionStatus() {
    }

    AuctionStatus(int statuCode, String statu) {
        this.statuCode = statuCode;
        this.statu = statu;
    }

    public int getStatuCode() {
        return statuCode;
    }

    public void setStatuCode(int statuCode) {
        this.statuCode = statuCode;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }
}
