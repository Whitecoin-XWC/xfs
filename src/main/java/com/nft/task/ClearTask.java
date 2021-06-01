package com.nft.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nft.dao.entity.FilePO;
import com.nft.dao.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Slf4j
@Component
public class ClearTask {

    @Resource
    private FileMapper fileMapper;

    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void clear(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("file_status", -1);
        List<FilePO> filePOList = fileMapper.selectList(queryWrapper);
        if(filePOList == null){
            return;
        }

        log.info("开始清理无效文件，总数:{}个", filePOList.size());

        for(FilePO filePO : filePOList){
            if((System.currentTimeMillis() - filePO.getCreateTime().getTime()) > 1000 * 60 * 10 && filePO.getFileStatus().equals(-1)){
                fileMapper.deleteById(filePO.getId());

                File file = new File(filePO.getFilePath());
                if(file.exists()){
                    file.delete();
                    log.info("清理文件{}", filePO.getFilePath());
                }
            }
        }
    }
}
