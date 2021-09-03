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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
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
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 提供给客户端的接口
 */
@Slf4j
@Api(value = "upload file", tags = "upload file api")
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
     * 文件后缀
     */
    @Value("${fileUpload.type}")
    private String mediaSuffix;

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
    @ApiOperation("upload file")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "file input stream"),
            @ApiImplicitParam(name = "title", value = "nft title"),
            @ApiImplicitParam(name = "des", value = "nft description"),
            @ApiImplicitParam(name = "userAddress", value = "user tag"),
            @ApiImplicitParam(name = "copyrightFee", value = "copyright fee")
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultVO save(HttpServletRequest request) {
        log.info("upload file start");
        try {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest params = (MultipartHttpServletRequest) request;

                // save nft file
                FilePO filePO = upload(params);

                log.info("upload file success, :{}", filePO.getId());
                // return file url and file token id
                UploadResultVO uploadResultVO = new UploadResultVO();
                uploadResultVO.setTokenId(filePO.getId());
                uploadResultVO.setUrl(imgUrl + filePO.getFileName());
                return ResultVO.success(uploadResultVO);
            } else {
                log.info("upload file fail, :use formData post");
                return ResultVO.fail("请使用formData格式请求此接口");
            }
        } catch (SizeLimitExceededException e) {
            log.error("upload file fail,file size bigger than 20M");
            return ResultVO.fail("上传文件最大20M");
        } catch (Exception e) {
            log.error("upload file has exception: {}", e);
            return ResultVO.fail("上传出现异常:" + e.getMessage());
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

        List<MultipartFile> multipartFileList = multipartFileMultiValueMap.get("file");
        if (CollectionUtils.isEmpty(multipartFileList)) {
            throw new Exception("上传失败,请选择文件");
        }

        MultipartFile multipartFile = multipartFileList.get(0);
        String fileName = UUID.randomUUID().toString();
        String oldFileName = multipartFile.getOriginalFilename();
        String suffix = "blank";
        assert oldFileName != null;
        if (oldFileName.lastIndexOf(".") > -1) {
            suffix = oldFileName.substring(oldFileName.lastIndexOf("."));
        }

        String newFileName = fileName + suffix;

        String mediaType = "-1";

        if (!StringUtils.isEmpty(mediaSuffix)) {
            boolean isMatch = false;
            String[] mediaTypes = mediaSuffix.split(",");
            for (String type : mediaTypes) {
                String[] tp = type.split("_");
                if (suffix.toUpperCase().endsWith(tp[0])) {
                    mediaType = tp[1];
                    isMatch = true;
                    break;
                }
            }
            if (!isMatch) {
                throw new Exception("不支持此类型的文件");
            }
        }

        File file = new File(savePath + "/" + newFileName);
        multipartFile.transferTo(file);

        String title = params.getParameter("title");
        String des = params.getParameter("des");
        String userTag = params.getParameter("userAddress");
        String copyright = params.getParameter("copyrightFee");


        String md5 = DigestUtils.md5Hex(new FileInputStream(file));
        filePO.setId(UUID.randomUUID().toString());
        filePO.setCreateTime(new Date());
        filePO.setFileName(newFileName);
        filePO.setMediaType(Integer.parseInt(mediaType));
        filePO.setFileStatus(1);
        filePO.setFilePath(savePath + "/" + newFileName);
        filePO.setFileTitle(title);
        filePO.setFileDes(des);
        filePO.setUserAddress(userTag);
        filePO.setCopyrightFee(copyright);
        filePO.setMd5(md5);
        filePO.setCreater(userTag);

        int result = nftService.upload(filePO);
        if (result < 0) {
            file.delete();
            throw new Exception("已经存在相同的文件");
        }

        return filePO;
    }
}
