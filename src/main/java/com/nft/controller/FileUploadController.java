package com.nft.controller;

import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.UploadResultVO;
import com.nft.dao.entity.FilePO;
import com.nft.service.NFTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 提供给客户端的接口
 */
@Slf4j
@Api(value = "文件上传", tags = "文件上传接口")
@RestController
@RequestMapping("fileUpload")
public class FileUploadController {

    /**
     * 图片保存路径
     */
    @Value("${fileUpload.save-path}")
    private String savePath;

    /**
     * 图片访问前缀
     */
    @Value("${fileUpload.img-url}")
    private String imgUrl;

    /**
     * nft服务层
     */
    @Resource
    private NFTService nftService;

    /**
     * 文件上传
     *
     * @param request
     * @return
     */
    @ApiOperation("文件上传")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "id"),
        @ApiImplicitParam(name = "mediaType", value = "文件类型，1 图片，2视频，3音频"),
        @ApiImplicitParam(name = "file", value = "文件流")
    })
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResultVO upload(HttpServletRequest request) {
        try {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest params = (MultipartHttpServletRequest) request;

                String id = params.getParameter("id");
                String mediaType = params.getParameter("mediaType");
                if (StringUtils.isEmpty(id)) {
                    return ResultVO.fail("id不可以为空");
                }
                if (StringUtils.isEmpty(mediaType)) {
                    return ResultVO.fail("文件类型不可以为空");
                }

                // 保存图片
                FilePO filePO = upload(params);
                filePO.setUserTag(id);
                filePO.setMediaType(Integer.parseInt(mediaType));
                int result =nftService.save(filePO);
                if(result < 0){
                    return ResultVO.fail("已经存在相同的文件");
                }

                // 返回文件url和tokenId
                UploadResultVO uploadResultVO = new UploadResultVO();
                uploadResultVO.setTokenId(filePO.getId());
                uploadResultVO.setUrl(imgUrl + filePO.getFileName());
                return ResultVO.success(uploadResultVO);
            } else {
                return ResultVO.fail("请使用formData格式请求此接口");
            }
        } catch (Exception e) {
            log.error("上传失败", e);
            return ResultVO.fail("上传出现异常" + e.getMessage());
        }
    }

    /**
     * 保存文件
     *
     * @param params
     * @return
     */
    private FilePO upload(MultipartHttpServletRequest params) throws Exception {
        MultiValueMap<String, MultipartFile> multipartFileMultiValueMap = params.getMultiFileMap();
        if (multipartFileMultiValueMap == null || multipartFileMultiValueMap.size() < 1) {
            throw new Exception("上传失败,请选择文件");
        }

        FilePO filePO = new FilePO();

        MultipartFile multipartFile = multipartFileMultiValueMap.getFirst("file");
        String fileName = UUID.randomUUID().toString();
        String oldFileName = multipartFile.getOriginalFilename();
        String newFileName = fileName + oldFileName.substring(oldFileName.lastIndexOf("."));

        multipartFile.transferTo(new File(savePath + "/" + newFileName));

        String id = DigestUtils.md5DigestAsHex(multipartFile.getBytes());
        filePO.setId(id);
        filePO.setCreateTime(new Date());
        filePO.setFileName(newFileName);
        filePO.setFileStatus(0);
        filePO.setFilePath("");
        return filePO;
    }
}
