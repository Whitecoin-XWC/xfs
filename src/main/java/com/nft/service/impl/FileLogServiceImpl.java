package com.nft.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nft.commons.util.LogUtil;
import com.nft.dao.entity.FileLogPO;
import com.nft.dao.mapper.FileLogMapper;
import com.nft.service.FileLogService;
import com.nft.service.dto.FileLogMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service("fileLogService")
public class FileLogServiceImpl implements FileLogService {

    @Resource
    private FileLogMapper fileLogMapper;

    @Override
    public int saveLog(String fileId, String action, Integer type, FileLogMessage fileLogMessage) {
        FileLogPO fileLogPO = new FileLogPO();
        fileLogPO.setFileId(fileId);
        fileLogPO.setLogInfo(action);
        fileLogPO.setCreateTime(new Date());
        fileLogPO.setType(type);
        if(fileLogMessage != null){
            fileLogPO.setOther(JSON.toJSONString(fileLogMessage));
        }
        return fileLogMapper.insert(fileLogPO);
    }
}
