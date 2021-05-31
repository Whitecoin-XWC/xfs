package com.nft.commons.util;

public class LogUtil {

    /**
     * 日志模板
     */
    private static String logInfoTemplate = "{userTag}{action}了这个NFT";

    /**
     * 获取日志内容
     * @param userTag
     * @param action
     * @return
     */
    public static String getLogInfo(String userTag, String action){
        return logInfoTemplate.replace("{userTag}",userTag).replace("{action}",action);
    }
}
