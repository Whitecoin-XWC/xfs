package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.commons.util.LogUtil;
import com.nft.controller.vo.FileLogVO;
import com.nft.controller.vo.FileVO;
import com.nft.controller.vo.PubVO;
import com.nft.dao.entity.FileLogPO;
import com.nft.dao.entity.FilePO;
import com.nft.dao.entity.UserFilePO;
import com.nft.dao.mapper.FileLogMapper;
import com.nft.dao.mapper.FileMapper;
import com.nft.dao.mapper.UserFileMapper;
import com.nft.dao.mapper.UserInfoMapper;
import com.nft.service.NFTService;
import com.nft.service.dto.FileResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class NFTServiceImpl implements NFTService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileLogMapper fileLogMapper;

    @Resource
    private UserFileMapper userFileMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Value("${fileUpload.img-url}")
    private String imgUrl;

    /**
     * 上传文件
     *
     * @param filePO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int upload(FilePO filePO) {
        FilePO fileItem = fileMapper.selectById(filePO.getId());
        if (fileItem != null && !StringUtils.isEmpty(fileItem.getId())) {
            return -1;
        }

        // 保存文件
        int result = fileMapper.insert(filePO);

        // 跟用户产生关联
        if (result > 0) {
            UserFilePO userFilePO = new UserFilePO();
            userFilePO.setCreateTime(new Date());
            userFilePO.setType(0);
            userFilePO.setFileId(filePO.getId());
            userFilePO.setUserId(filePO.getUserAddress());
            userFileMapper.insert(userFilePO);
        }
        saveLog(fileItem.getId(), filePO.getUserAddress(), "上传");
        return 1;
    }

    /**
     * 发布
     *
     * @param pubVO
     * @return
     */
    @Override
    public int pub(PubVO pubVO) {
        FilePO fileItem = fileMapper.selectById(pubVO.getTokenId());
        if (fileItem == null || StringUtils.isEmpty(fileItem.getId())) {
            return -1;
        }
        fileItem.setFileStatus(2);
        fileItem.setPubTime(new Date());
        fileMapper.updateById(fileItem);

        saveLog(fileItem.getId(), pubVO.getUserAddress(), "发布");

        return 1;
    }

    /**
     * 付费
     *
     * @param pubVO
     * @return
     */
    @Override
    public int pay(PubVO pubVO) {
        FilePO fileItem = fileMapper.selectById(pubVO.getTokenId());
        if (fileItem == null || StringUtils.isEmpty(fileItem.getId())) {
            return -1;
        }
        fileItem.setFileStatus(3);
        fileItem.setPayTime(new Date());
        fileMapper.updateById(fileItem);

        saveLog(fileItem.getId(), pubVO.getUserAddress(), "付费");

        return 1;
    }

    /**
     * 获取一个文件
     *
     * @param filePO
     * @return
     */
    @Override
    public FilePO getFileDetail(FilePO filePO) {
        FilePO fileDetail = fileMapper.selectById(filePO.getId());
        if (fileDetail == null || StringUtils.isEmpty(fileDetail.getId())) {
            return null;
        }
        QueryWrapper<UserFilePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", filePO.getId());
        UserFilePO userFilePO = userFileMapper.selectOne(queryWrapper);
        fileDetail.setUserAddress(userFilePO.getUserId());
        return fileDetail;
    }

    /**
     * 获取一个文件变化日志
     *
     * @param fileLogVO
     * @return
     */
    @Override
    public IPage<FileLogPO> getFileLog(FileLogVO fileLogVO) {
        QueryWrapper<FileLogPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileLogVO.getTokenId());
        queryWrapper.orderByDesc("create_time");

        Page<FileLogPO> fileLogPOPage = new Page<>(fileLogVO.getPage(), fileLogVO.getPageSize());
        return fileLogMapper.selectPage(fileLogPOPage, queryWrapper);
    }

    /**
     * 查询当前用户的所有文件
     *
     * @return
     */
    @Override
    public IPage<FileResultDTO> selectFiles(FileVO fileVO) {
        IPage<FileResultDTO> pageWrapper = new Page<>(fileVO.getPage(), fileVO.getPageSize());

        FileResultDTO fileResultDTO = new FileResultDTO();
        if (!StringUtils.isEmpty(fileVO.getUserAddress())) {
            fileResultDTO.setUserAddress(fileVO.getUserAddress());
        }
        if (fileVO.getMediaType() != null && fileVO.getMediaType() > -1) {
            fileResultDTO.setMediaType(fileVO.getMediaType());
        }
        if (fileVO.getStatus() != null && fileVO.getStatus() > -1) {
            fileResultDTO.setFileStatus(fileVO.getStatus());
        }
        if (!StringUtils.isEmpty(fileVO.getTokenId())) {
            fileResultDTO.setId(fileVO.getTokenId());
        }
        if (fileVO.getSource() != null && fileVO.getSource() > -1) {
            fileResultDTO.setSource(fileVO.getSource());
        }
        return fileMapper.selectFileList(pageWrapper, fileResultDTO);
    }

    /**
     * 保存日志
     *
     * @param fileId
     * @param userTag
     * @param action
     */
    private void saveLog(String fileId, String userTag, String action) {
        FileLogPO fileLogPO = new FileLogPO();
        fileLogPO.setFileId(fileId);
        fileLogPO.setLogInfo(LogUtil.getLogInfo(userTag, action));
        fileLogPO.setCreateTime(new Date());
        fileLogMapper.insert(fileLogPO);
    }

    /**
     * 搜索
     *
     * @param keyWord
     * @return
     */
    @Override
    public Map<String, Object> search(String keyWord) {

        Map<String, Object> resultMap = new HashMap<>();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("id", keyWord);
        queryWrapper.or();
        queryWrapper.like("nick_name", keyWord);

        Page page = userInfoMapper.selectPage(new Page<>(1, 100), queryWrapper);
        if (page != null && page.getRecords() != null) {
            resultMap.put("users", page.getRecords());
        } else {
            resultMap.put("users", new ArrayList<>());
        }


        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.like("file_title", keyWord);

        Page page1 = fileMapper.selectPage(new Page<>(1, 100), queryWrapper1);
        if (page1 != null && page1.getRecords() != null) {
            resultMap.put("imagUrl", imgUrl);
            resultMap.put("files", page1.getRecords());
        } else {
            resultMap.put("files", new ArrayList<>());
        }
        return resultMap;
    }
}
