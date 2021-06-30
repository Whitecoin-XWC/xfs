package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.commons.util.LogUtil;
import com.nft.controller.vo.FileLogVO;
import com.nft.controller.vo.PubVO;
import com.nft.dao.entity.FileLogPO;
import com.nft.dao.entity.FilePO;
import com.nft.dao.entity.UserFilePO;
import com.nft.dao.mapper.FileLogMapper;
import com.nft.dao.mapper.FileMapper;
import com.nft.dao.mapper.UserFileMapper;
import com.nft.service.NFTService;
import com.nft.service.dto.FileResultDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NFTServiceImpl implements NFTService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileLogMapper fileLogMapper;

    @Resource
    private UserFileMapper userFileMapper;

    /**
     * 上传文件
     * @param filePO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int upload(FilePO filePO) {
        FilePO fileItem = fileMapper.selectById(filePO.getId());
        if(fileItem != null && !StringUtils.isEmpty(fileItem.getId())){
            return -1;
        }

        // 保存文件
        int result = fileMapper.insert(filePO);

        // 跟用户产生关联
        if(result > 0){
            UserFilePO userFilePO = new UserFilePO();
            userFilePO.setCreateTime(new Date());
            userFilePO.setType(0);
            userFilePO.setFileId(filePO.getId());
            userFilePO.setUserId(filePO.getUserAddress());
            userFileMapper.insert(userFilePO);
        }

        return 1;
    }

    /**
     * 发布
     * @param pubVO
     * @return
     */
    @Override
    public int pub(PubVO pubVO) {
        FilePO fileItem = fileMapper.selectById(pubVO.getTokenId());
        if(fileItem == null || StringUtils.isEmpty(fileItem.getId())){
            return -1;
        }
        fileItem.setFileStatus(3);
        fileItem.setPubTime(new Date());
        fileItem.setPayTime(new Date());
        fileMapper.updateById(fileItem);

        saveLog(fileItem.getId(), pubVO.getUserAddress(),"发布");

        return 1;
    }

    /**
     * 付费
     * @param pubVO
     * @return
     */
    @Override
    public int pay(PubVO pubVO) {
        FilePO fileItem = fileMapper.selectById(pubVO.getTokenId());
        if(fileItem == null || StringUtils.isEmpty(fileItem.getId())){
            return -1;
        }
        fileItem.setFileStatus(3);
        fileItem.setPayTime(new Date());
        fileMapper.updateById(fileItem);

        saveLog(fileItem.getId(), pubVO.getUserAddress(),"付费");

        return 1;
    }

    /**
     * 获取一个文件
     * @param filePO
     * @return
     */
    @Override
    public FilePO getFileDetail(FilePO filePO) {
        FilePO fileDetail = fileMapper.selectById(filePO.getId());
        if(fileDetail == null || StringUtils.isEmpty(fileDetail.getId())){
            return null;
        }
        return fileDetail;
    }

    /**
     * 获取一个文件
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
     * @param userTag
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public IPage<FileResultDTO> selectFiles(String userTag, int page, int pageSize) {
        IPage<FileResultDTO> pageWrapper = new Page<>(pageSize, pageSize);

        FileResultDTO fileResultDTO = new FileResultDTO();
        if(!StringUtils.isEmpty(userTag)){
            fileResultDTO.setUserAddress(userTag);
        }
        return fileMapper.selectFileList(pageWrapper, fileResultDTO);
    }

    /**
     * 保存日志
     * @param fileId
     * @param userTag
     * @param action
     */
    private void saveLog(String fileId, String userTag, String action){
        FileLogPO fileLogPO = new FileLogPO();
        fileLogPO.setFileId(fileId);
        fileLogPO.setLogInfo(LogUtil.getLogInfo(userTag,action));
        fileLogPO.setCreateTime(new Date());
        fileLogMapper.insert(fileLogPO);
    }
}
