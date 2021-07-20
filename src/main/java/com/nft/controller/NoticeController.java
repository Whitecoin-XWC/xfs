package com.nft.controller;

import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.NoticeVo;
import com.nft.service.NoticeService;
import com.nft.service.dto.NoticeResult;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description: nft
 * Created by moloq on 2021/7/19 10:30
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @ApiOperation("获取通知接口")
    @PostMapping("/notice")
    public ResultVO getNotice(@RequestBody NoticeVo noticeVo) {
        if (noticeVo == null
                || StringUtils.isBlank(noticeVo.getUserAddress())
                || noticeVo.getPageNo() == null || noticeVo.getPageSize() == null) {
            return ResultVO.fail("参数错误");
        }
        NoticeResult noticeResult = noticeService.getNotice(noticeVo);
        return ResultVO.success(noticeResult);
    }

    @ApiOperation("获取通知总数接口")
    @PostMapping("/noticeCount")
    public ResultVO getNoticeCount(@RequestBody NoticeVo noticeVo) {
        return ResultVO.success(noticeService.getNoticeCount(noticeVo.getUserAddress()));
    }
}
