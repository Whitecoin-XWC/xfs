package com.nft.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.commons.vo.PageResultVO;
import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.*;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.FileLogPO;
import com.nft.dao.entity.FilePO;
import com.nft.dao.entity.MessageEntity;
import com.nft.service.AuctionService;
import com.nft.service.MessageService;
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

@Api(value = "file manager", tags = "file manage api")
@Slf4j
@RestController
@RequestMapping("/nft")
public class NFTController {

    /**
     * prefix path of get file
     */
    @Value("${fileUpload.img-url}")
    private String imgUrl;

    @Resource
    private NFTService nftService;

    @Resource
    private MessageService messageService;

    /**
     * follow nft
     *
     * @param followVO
     * @return
     */
    @ApiOperation("follow nft")
    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public ResultVO follow(@RequestBody FollowVO followVO) {
        log.info("follow nft start :{}", followVO.getTokenId());
        try {
            nftService.follow(followVO);
            log.info("follow nft success :{}", followVO.getTokenId());
            return ResultVO.successMsg("关注成功");
        } catch (Exception e) {
            log.error("follow nft fail :{},{}", followVO.getTokenId(), e);
            return ResultVO.fail("关注异常");
        }
    }

    /**
     * unsubscribe nft
     *
     * @param followVO
     * @return
     */
    @ApiOperation("unsubscribe nft")
    @RequestMapping(value = "/delFollow", method = RequestMethod.POST)
    public ResultVO delFollow(@RequestBody FollowVO followVO) {
        log.info("unsubscribe nft start :{}", followVO.getTokenId());
        try {
            nftService.delFollow(followVO);
            log.info("unsubscribe nft success :{}", followVO.getTokenId());
            return ResultVO.successMsg("取消关注成功");
        } catch (Exception e) {
            log.error("unsubscribe nft has exception :{},{}", followVO.getTokenId(), e);
            return ResultVO.fail("取消关注异常");
        }
    }

    /**
     * search nft
     *
     * @param searchVO
     * @return
     */
    @ApiOperation("search nft")
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
     * search all files of homepage
     *
     * @param fileVO
     * @return
     */
    @ApiOperation("files list of homepage")
    @RequestMapping(value = "/selectIndexList", method = RequestMethod.POST)
    public ResultVO selectIndexList(@RequestBody FileVO fileVO) {
        try {
            PageResultVO pageResultVO = new PageResultVO();
            fileVO.setMediaType(fileVO.getMediaType());
            fileVO.setStatus(fileVO.getStatus());
            fileVO.setSource(fileVO.getSource());
            fileVO.setIsIndex("yes");
            IPage<FileResultDTO> iPage = nftService.selectFiles(fileVO);
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
            log.error("查询异常", e);
            return ResultVO.fail("查询异常" + e.getMessage());
        }
    }

    /**
     * pub file
     *
     * @param pubVO
     * @return
     */
    @ApiOperation("pub file")
    @RequestMapping(value = "/pub", method = RequestMethod.POST)
    public ResultVO pub(@RequestBody PubVO pubVO) {
        log.info("pub file start :{}", pubVO.getTokenId());
        try {
            int result = nftService.pub(pubVO);
            if (result > 0) {
                log.info("pub file success :{}", pubVO.getTokenId());
                return ResultVO.successMsg("发布成功");
            } else if (result == -1) {
                log.error("not found pub file :{}", pubVO.getTokenId());
                return ResultVO.fail("文件不存在");
            } else if (result == -2) {
                log.error("this file has pub by other user :{}", pubVO.getTokenId());
                return ResultVO.fail("文件已经被别人发行过了");
            }
            log.error("pub file fail :{}", pubVO.getTokenId());
            return ResultVO.fail("发布失败");
        } catch (Exception e) {
            log.error("pub file has exception :{},{}", pubVO.getTokenId(), e);
            return ResultVO.fail("发布异常" + e.getMessage());
        }
    }

    /**
     * pay
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
     * transfer nft
     *
     * @param fileUserChangeVO
     * @return
     */
    @ApiOperation("transfer nft")
    @RequestMapping(value = "/fileUserChange", method = RequestMethod.POST)
    public ResultVO fileUserChange(@RequestBody FileUserChangeVO fileUserChangeVO) {
        log.info("transfer nft start :{}", fileUserChangeVO.getTokenId());
        try {
            int result = nftService.fileUserChange(fileUserChangeVO);
            if (result > 0) {
                log.info("transfer nft success :{}", fileUserChangeVO.getTokenId());
                return ResultVO.successMsg("转移成功");
            }
            log.info("transfer nft fail :{}", fileUserChangeVO.getTokenId());
            return ResultVO.fail("转移失败");
        } catch (Exception e) {
            log.error("transfer nft has exception :{},{}", fileUserChangeVO.getTokenId(), e);
            return ResultVO.fail("转移异常" + e.getMessage());
        }
    }

    /**
     * get file detail
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
     * get file log
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

    @ApiOperation("add message")
    @PostMapping("/addMessage")
    public ResultVO addMessage(@RequestBody MessageEntity messageEntity) {
        return messageService.addMessage(messageEntity);
    }

    @ApiOperation("query message")
    @PostMapping("/queryMessage")
    public ResultVO queryMessage(@RequestBody QueryMessageVo messageVo) {
        return messageService.queryMessage(messageVo.getTokenId(), messageVo.getPage(), messageVo.getPageSize());
    }
}
