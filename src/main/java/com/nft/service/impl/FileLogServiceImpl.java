package com.nft.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nft.dao.entity.FileLogPO;
import com.nft.dao.entity.FilePO;
import com.nft.dao.entity.UserFilePO;
import com.nft.dao.entity.UserinfoPO;
import com.nft.dao.mapper.*;
import com.nft.service.FileLogService;
import com.nft.service.dto.FileLogAttach;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * log service
 */
@Service("fileLogService")
public class FileLogServiceImpl implements FileLogService {

    @Resource
    private FileLogMapper fileLogMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserFileMapper userFileMapper;
    @Resource
    private ShopMapper shopMapper;

    /**
     * save log
     *
     * @param fileId        文件ID，也就是文件的tokenId
     * @param action        日志动作描述，比如发布了nft，出售了nft
     * @param userAddress   创建这个日志的用户地址
     * @param type          0 普通变化，1 竞拍，2 出售
     * @param fileLogAttach 日志携带的附件，用于记录更多的数据
     * @return
     */
    @Override
    public int saveLog(String fileId, String action, String userAddress, Integer type, FileLogAttach fileLogAttach) {
        FileLogPO fileLogPO = new FileLogPO();
        fileLogPO.setFileId(fileId);
        fileLogPO.setLogInfo(action);
        /* 获取用户昵称 */
        fileLogPO.setUserId(userAddress);
        UserinfoPO userinfoPO = userInfoMapper.selectById(userAddress);
        if (userinfoPO != null && !StringUtils.isEmpty(userinfoPO.getNickName())) {
            fileLogPO.setUserId(userinfoPO.getNickName());
        }
        fileLogPO.setCreateTime(new Date());
        fileLogPO.setType(type);
        /* check nft is entity or virtual */
        QueryWrapper<UserFilePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id",fileId);
        queryWrapper.eq("type",0);
        UserFilePO userFilePO = userFileMapper.selectOne(queryWrapper);
        if (userFilePO != null && shopMapper.selectById(userFilePO.getUserId()) != null){
            fileLogAttach.setEntity(Boolean.TRUE);
        }

        if (fileLogAttach != null) {
            fileLogPO.setOther(JSON.toJSONString(fileLogAttach));
        }
        return fileLogMapper.insert(fileLogPO);
    }
}
