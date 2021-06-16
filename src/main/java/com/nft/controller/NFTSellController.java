package com.nft.controller;

import com.nft.commons.vo.ResultVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "文件出售", tags = "文件出售接口")
@Slf4j
@RestController
@RequestMapping("nftSell")
public class NFTSellController {

    /**
     * 出售
     * @return
     */
    public ResultVO sell(){
        return null;
    }

    /**
     * 出售成功
     * @return
     */
    public ResultVO sellSuccess(){
        return null;
    }
}
