package com.nft.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.commons.vo.PageResultVO;
import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.FileVO;
import com.nft.controller.vo.PubVO;
import com.nft.controller.vo.SaveVO;
import com.nft.controller.vo.SelectVO;
import com.nft.dao.entity.FilePO;
import com.nft.service.NFTService;
import com.nft.service.dto.FileDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    /**
     * 保存文件
     * @return
     */
    @ApiOperation("保存文件")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultVO save(@RequestBody SaveVO saveVO){
        try {
            FilePO filePO = new FilePO();
            filePO.setId(saveVO.getTokenId());
            filePO.setFileTitle(saveVO.getTitle());
            filePO.setFileDes(saveVO.getDes());
            filePO.setUserTag(saveVO.getUserTag());
            int result= nftService.save(filePO);
            if(result > 0){
                return ResultVO.successMsg("保存成功");
            }
            return ResultVO.fail("保存失败");
        }catch (Exception e){
            log.error("保存异常", e);
            return ResultVO.fail("保存异常" + e.getMessage());
        }
    }

    /**
     * 发布
     * @param pubVO
     * @return
     */
    @ApiOperation("发布文件")
    @RequestMapping(value = "/pub", method = RequestMethod.POST)
    public ResultVO pub(@RequestBody PubVO pubVO){
        try {
            int result = nftService.pub(pubVO);
            if(result > 0){
                return ResultVO.successMsg("发布成功");
            }
            return ResultVO.fail("发布失败");
        }catch (Exception e){
            log.error("发布异常", e);
            return ResultVO.fail("发布异常" + e.getMessage());
        }
    }

    /**
     * 付费
     * @param pubVO
     * @return
     */
    @ApiOperation("付费，已可用，但是因为目前免费 所以此接口暂时用不到")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ResultVO pay(@RequestBody PubVO pubVO){
        try {
            int result = nftService.pay(pubVO);
            if(result > 0){
                return ResultVO.successMsg("付费成功");
            }
            return ResultVO.fail("付费失败");
        }catch (Exception e){
            log.error("付费异常", e);
            return ResultVO.fail("付费异常" + e.getMessage());
        }
    }

    /**
     * 获取文件
     * @return
     */
    @ApiOperation("获取文件")
    @RequestMapping(value = "/getFile", method = RequestMethod.POST)
    public ResultVO getFile(@RequestBody FileVO fileVO){
        try {
            FilePO filePO = new FilePO();
            filePO.setId(fileVO.getTokenId());
            filePO.setUserTag(fileVO.getUserTag());
            FileDTO fileDTO = nftService.getFile(filePO);
            fileDTO.setImgUrl(imgUrl);
            return ResultVO.success(fileDTO);
        }catch (Exception e){
            log.error("获取文件异常", e);
            return ResultVO.fail("获取文件异常" + e.getMessage());
        }
    }

    /**
     * 查询当前用户的所有文件
     * @return
     */
    @ApiOperation("查询当前用户上传的文件")
    @RequestMapping(value = "/selectFiles", method = RequestMethod.POST)
    public ResultVO selectFiles(@RequestBody SelectVO selectVO){
        try {
            PageResultVO pageResultVO = new PageResultVO();

            IPage<FilePO> iPage = nftService.selectFiles(selectVO.getUserTag(), selectVO.getPage(), selectVO.getPageSize());
            if(iPage != null){
                pageResultVO.setCount(iPage.getTotal());
                pageResultVO.setCurrentPage(iPage.getCurrent());
                pageResultVO.setPageSize(iPage.getSize());
                pageResultVO.setPageTotal(iPage.getPages());
                pageResultVO.setImgUrl(imgUrl);
                pageResultVO.setRecords(iPage.getRecords());
            }
            return ResultVO.success(pageResultVO);
        } catch (Exception e){
            log.error("查询异常", e);
            return ResultVO.fail("查询异常"+e.getMessage());
        }
    }
}
