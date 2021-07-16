package com.nft.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.commons.vo.PageResultVO;
import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.*;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.FileLogPO;
import com.nft.dao.entity.FilePO;
import com.nft.service.AuctionService;
import com.nft.service.NFTService;
import com.nft.service.dto.FileDTO;
import com.nft.service.dto.FileResultDTO;
import com.nft.service.dto.NoticeResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "文件管理", tags = "文件管理接口")
@Slf4j
@RestController
@RequestMapping("/nft")
public class NFTController {

    /**
     * 图片访问前缀
     */
    @Value("${fileUpload.img-url}")
    private String imgUrl;

    @Resource
    private NFTService nftService;
    @Resource
    private AuctionService auctionService;


    /**
     * 关注
     *
     * @param followVO
     * @return
     */
    @ApiOperation("关注")
    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public ResultVO follow(@RequestBody FollowVO followVO) {
        try {
            nftService.follow(followVO);
            return ResultVO.successMsg("关注成功");
        } catch (Exception e) {
            log.error("关注异常", e);
            return ResultVO.fail("关注异常");
        }
    }

    /**
     * 取消关注
     *
     * @param followVO
     * @return
     */
    @ApiOperation("取消关注")
    @RequestMapping(value = "/delFollow", method = RequestMethod.POST)
    public ResultVO delFollow(@RequestBody FollowVO followVO) {
        try {
            nftService.delFollow(followVO);
            return ResultVO.successMsg("取消关注成功");
        } catch (Exception e) {
            log.error("取消关注异常", e);
            return ResultVO.fail("取消关注异常");
        }
    }

    /**
     * 搜索
     *
     * @param searchVO
     * @return
     */
    @ApiOperation("搜索")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResultVO search(@RequestBody SearchVO searchVO) {
        try {
            return ResultVO.success(nftService.search(searchVO.getKeyWord()));
        } catch (Exception e) {
            log.error("查询异常", e);
            return ResultVO.fail("查询异常" + e.getMessage());
        }
    }

    /**
     * 查询首页文件列表
     *
     * @param fileVO
     * @return
     */
    @ApiOperation("查询首页文件列表")
    @RequestMapping(value = "/selectIndexList", method = RequestMethod.POST)
    public ResultVO selectIndexList(@RequestBody FileVO fileVO) {
        try {
            PageResultVO pageResultVO = new PageResultVO();
            fileVO.setMediaType(fileVO.getMediaType());
            fileVO.setStatus(fileVO.getStatus());
            fileVO.setSource(fileVO.getSource());
            IPage<FileResultDTO> iPage = nftService.selectFiles(fileVO);
            if (iPage != null) {
                for (FileResultDTO record : iPage.getRecords()) {
                    if (record.getFileStatus() == 5) {
                        AuctionEntity auctionEntity = auctionService.queryAuction(record.getId());
                        record.setAuctionMaxPrice(auctionEntity.getAuctionMaxPrice());
                        record.setRemainingTime(auctionEntity.getRemainingTime());
                    }
                }
                pageResultVO.setCount(iPage.getTotal());
                pageResultVO.setCurrentPage(iPage.getCurrent());
                pageResultVO.setPageSize(iPage.getSize());
                pageResultVO.setPageTotal(iPage.getPages());
                pageResultVO.setImgUrl(imgUrl);
                pageResultVO.setRecords(iPage.getRecords());
            }
            return ResultVO.success(pageResultVO);
        } catch (Exception e) {
            log.error("查询异常", e);
            return ResultVO.fail("查询异常" + e.getMessage());
        }
    }

    /**
     * 发布
     *
     * @param pubVO
     * @return
     */
    @ApiOperation("发布文件")
    @RequestMapping(value = "/pub", method = RequestMethod.POST)
    public ResultVO pub(@RequestBody PubVO pubVO) {
        try {
            int result = nftService.pub(pubVO);
            if (result > 0) {
                return ResultVO.successMsg("发布成功");
            }
            return ResultVO.fail("发布失败");
        } catch (Exception e) {
            log.error("发布异常", e);
            return ResultVO.fail("发布异常" + e.getMessage());
        }
    }

    /**
     * 付费
     *
     * @param pubVO
     * @return
     */
    @ApiOperation("付费，已可用，但是因为目前免费 所以此接口暂时用不到")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ResultVO pay(@RequestBody PubVO pubVO) {
        try {
            int result = nftService.pay(pubVO);
            if (result > 0) {
                return ResultVO.successMsg("付费成功");
            }
            return ResultVO.fail("付费失败");
        } catch (Exception e) {
            log.error("付费异常", e);
            return ResultVO.fail("付费异常" + e.getMessage());
        }
    }


    /**
     * 转移
     *
     * @param fileUserChangeVO
     * @return
     */
    @ApiOperation("文件转移")
    @RequestMapping(value = "/fileUserChange", method = RequestMethod.POST)
    public ResultVO fileUserChange(@RequestBody FileUserChangeVO fileUserChangeVO) {
        try {
            int result = nftService.fileUserChange(fileUserChangeVO);
            if (result > 0) {
                return ResultVO.successMsg("转移成功");
            }
            return ResultVO.fail("转移失败");
        } catch (Exception e) {
            log.error("转移异常", e);
            return ResultVO.fail("转移异常" + e.getMessage());
        }
    }

    /**
     * 查询文件详情
     *
     * @return
     */
    @ApiOperation("查询文件详情")
    @RequestMapping(value = "/getFileDetail", method = RequestMethod.POST)
    public ResultVO getFileDetail(@RequestBody FileVO fileVO) {
        try {
            FilePO filePO = new FilePO();
            filePO.setId(fileVO.getTokenId());
            filePO.setUserAddress(fileVO.getUserAddress());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setImgUrl(imgUrl);
            fileDTO.setFilePO(nftService.getFileDetail(filePO));
            return ResultVO.success(fileDTO);
        } catch (Exception e) {
            log.error("获取文件异常", e);
            return ResultVO.fail("获取文件异常" + e.getMessage());
        }
    }

    /**
     * 获取文件变化日志
     *
     * @return
     */
    @ApiOperation("获取文件变化日志")
    @RequestMapping(value = "/getFileLog", method = RequestMethod.POST)
    public ResultVO getFileLog(@RequestBody FileLogVO fileLogVO) {
        try {
            PageResultVO pageResultVO = new PageResultVO();

            IPage<FileLogPO> iPage = nftService.getFileLog(fileLogVO);
            if (iPage != null) {
                pageResultVO.setCount(iPage.getTotal());
                pageResultVO.setCurrentPage(iPage.getCurrent());
                pageResultVO.setPageSize(iPage.getSize());
                pageResultVO.setPageTotal(iPage.getPages());
                pageResultVO.setImgUrl(imgUrl);
                pageResultVO.setRecords(iPage.getRecords());
            }

            return ResultVO.success(pageResultVO);
        } catch (Exception e) {
            log.error("获取文件变化日志异常", e);
            return ResultVO.fail("获取文件变化日志异常" + e.getMessage());
        }
    }

    @ApiOperation("获取通知接口")
    @PostMapping("/notice")
    public ResultVO getNotice(NoticeVo noticeVo) {
        if (noticeVo == null
                || StringUtils.isBlank(noticeVo.getUserAddress())
                || noticeVo.getPageNo() == null || noticeVo.getPageSize() == null) {
            return ResultVO.fail("参数错误");
        }
        NoticeResult noticeResult = nftService.getNotice(noticeVo);
        return ResultVO.success(noticeResult);
    }
}
