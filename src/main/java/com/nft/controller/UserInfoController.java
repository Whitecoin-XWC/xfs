package com.nft.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.commons.vo.PageResultVO;
import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.FileVO;
import com.nft.controller.vo.SelectVO;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.UserinfoPO;
import com.nft.service.AuctionService;
import com.nft.service.UserFileService;
import com.nft.service.UserInfoService;
import com.nft.service.dto.FileResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "userinfo manager", tags = "userinfo manager api")
@Slf4j
@RestController
@RequestMapping("userInfo")
public class UserInfoController {

    @Resource
    private UserFileService userFileService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * img url head
     */
    @Value("${fileUpload.img-url}")
    private String imgUrl;


    /**
     * update user info
     *
     * @return
     */
    @ApiOperation("update userinfo api")
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public ResultVO updateUserInfo(@RequestBody UserinfoPO userinfoPO) {
        try {
            userInfoService.updateUserInfo(userinfoPO);
            return ResultVO.successMsg("更新成功");
        } catch (Exception e) {
            log.error("update userinfo has exception: {}", e);
            return ResultVO.fail("更新用户信息异常" + e.getMessage());
        }
    }

    /**
     * get user info
     *
     * @return
     */
    @ApiOperation("query userinfo api")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public ResultVO getUserInfo(@RequestBody UserinfoPO userinfoPO) {
        try {
            return ResultVO.success(userInfoService.getUserInfo(userinfoPO.getId()));
        } catch (Exception e) {
            log.error("query userinfo has exception: {}", e);
            return ResultVO.fail("查询用户资料异常" + e.getMessage());
        }
    }

    /**
     * get all file for user
     *
     * @return
     */
    @ApiOperation("query user nft")
    @RequestMapping(value = "/selectFiles", method = RequestMethod.POST)
    public ResultVO selectFiles(@RequestBody SelectVO selectVO) {
        try {
            PageResultVO pageResultVO = new PageResultVO();

            FileVO fileVO = new FileVO();
            fileVO.setUserAddress(selectVO.getUserAddress());
            fileVO.setPage(selectVO.getPage());
            fileVO.setPageSize(selectVO.getPageSize());
            fileVO.setSource(selectVO.getSource());
            fileVO.setMediaType(null);
            fileVO.setStatus(null);
            IPage<FileResultDTO> iPage = userFileService.selectFiles(fileVO);
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
            log.error("query user nft has exception: {}", e);
            return ResultVO.fail("查询异常" + e.getMessage());
        }
    }
}
