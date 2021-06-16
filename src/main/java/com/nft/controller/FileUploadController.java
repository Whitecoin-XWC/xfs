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
    @ApiOperation("文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件流"),
            @ApiImplicitParam(name = "title", value = "标题"),
            @ApiImplicitParam(name = "des", value = "描述"),
            @ApiImplicitParam(name = "userTag", value = "用户标识"),
            @ApiImplicitParam(name = "copyrightFee", value = "版权费")
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultVO save(HttpServletRequest request) {
        try {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest params = (MultipartHttpServletRequest) request;

                // 保存图片
                FilePO filePO = upload(params);

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
        if (oldFileName.lastIndexOf(".") > -1) {
            suffix = oldFileName.substring(oldFileName.lastIndexOf("."));
        }

        String newFileName = fileName + suffix;

        String mediaType = "-1";

        if (!StringUtils.isEmpty(mediaSuffix)) {
            boolean isMatch = false;
            String[] mediaTypes = mediaSuffix.split(",");
            for (String type : mediaTypes) {
                String tp[] = type.split("_");
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
        String userTag = params.getParameter("userTag");
        String copyright = params.getParameter("copyrightFee");


        String id = DigestUtils.md5Hex(new FileInputStream(file));
        filePO.setId(id);
        filePO.setCreateTime(new Date());
        filePO.setFileName(newFileName);
        filePO.setMediaType(Integer.parseInt(mediaType));
        filePO.setFileStatus(1);
        filePO.setFilePath(savePath + "/" + newFileName);
        filePO.setFileTitle(title);
        filePO.setFileDes(des);
        filePO.setUserTag(userTag);
        filePO.setCopyrightFee(copyright);

        int result = nftService.upload(filePO);
        if (result < 0) {
            file.delete();
            throw new Exception("已经存在相同的文件");
        }

        return filePO;
    }
}
