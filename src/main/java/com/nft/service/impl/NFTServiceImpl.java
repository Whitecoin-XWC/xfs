package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.commons.util.LogUtil;
import com.nft.controller.vo.PubVO;
import com.nft.dao.entity.FileLogPO;
import com.nft.dao.entity.FilePO;
import com.nft.dao.mapper.FileLogMapper;
import com.nft.dao.mapper.FileMapper;
import com.nft.service.NFTService;
import com.nft.service.dto.FileDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class NFTServiceImpl implements NFTService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileLogMapper fileLogMapper;

    /**
     * 上传文件
     * @param filePO
     * @return
     */
    @Override
    public int upload(FilePO filePO) {
        FilePO fileItem = fileMapper.selectById(filePO.getId());
        if(fileItem != null && !StringUtils.isEmpty(fileItem.getId())){
            return -1;
        }

        return fileMapper.insert(filePO);
    }

    /**
     * 保存文件
     * @param filePO
     * @return
     */
    @Override
    @Transactional
    public int save(FilePO filePO) {

        FilePO fileItem = fileMapper.selectById(filePO.getId());
        if(fileItem == null || StringUtils.isEmpty(fileItem.getId())){
            return -1;
        }

        fileItem.setFileDes(filePO.getFileDes());
        fileItem.setFileTitle(filePO.getFileTitle());
        fileItem.setFileStatus(1);
        fileItem.setUserTag(filePO.getUserTag());

        fileMapper.updateById(fileItem);

        saveLog(filePO.getId(), filePO.getUserTag(),"铸造");

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

        saveLog(fileItem.getId(), fileItem.getUserTag(),"发布");

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

        saveLog(fileItem.getId(), fileItem.getUserTag(),"付费");

        return 1;
    }

    /**
     * 获取一个文件
     * @param filePO
     * @return
     */
    @Override
    public FileDTO getFileDetail(FilePO filePO) {
        FilePO fileDetail = fileMapper.selectById(filePO.getId());
        if(fileDetail == null || StringUtils.isEmpty(fileDetail.getId())){
            return null;
        }

        if(!StringUtils.isEmpty(filePO.getUserTag()) && !filePO.getUserTag().equals(fileDetail.getUserTag())){
            return null;
        }

        QueryWrapper<FileLogPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileDetail.getId());
        queryWrapper.orderByDesc("create_time");
        List<FileLogPO> fileLogPOList = fileLogMapper.selectList(queryWrapper);

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFilePO(fileDetail);
        fileDTO.setFileLogPOList(fileLogPOList);
        return fileDTO;
    }

    /**
     * 获取一个文件
     * @param filePO
     * @return
     */
    @Override
    public FileDTO getFile(FilePO filePO) {
        FilePO fileDetail = fileMapper.selectById(filePO.getId());
        if(fileDetail == null || StringUtils.isEmpty(fileDetail.getId())){
            return null;
        }

        FileDTO fileDTO = new FileDTO();
        fileDTO.setFilePO(fileDetail);
        return fileDTO;
    }

    /**
     * 查询当前用户的所有文件
     * @param userTag
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public IPage<FilePO> selectFiles(String userTag, int page, int pageSize) {

        Page<FilePO> poPage = new Page<>(page, pageSize);

        QueryWrapper<FilePO> wrapper = new QueryWrapper<>();
        wrapper.eq("user_tag", userTag);
        return fileMapper.selectPage(poPage, wrapper);
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
