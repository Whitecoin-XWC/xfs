package com.nft.commons.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultVO {

    /**
     * 状态码
     */
    private int code;

    /**
     * 提示信息
     */
    private String info;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 成功返回数据
     * @param data
     * @return
     */
    public static ResultVO success(Object data){
        ResultVO resultVO = new ResultVO();
        resultVO.code = 200;
        resultVO.info = "请求成功";
        resultVO.data = data;
        return resultVO;
    }

    /**
     * 成功提示信息
     * @param msg
     * @return
     */
    public static ResultVO successMsg(String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.code = 200;
        resultVO.info = msg;
        return resultVO;
    }

    /**
     * 失败提示信息
     * @param msg
     * @return
     */
    public static ResultVO fail(String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.code = 500;
        resultVO.info = msg;
        return resultVO;
    }
}
