package com.nft.tasks;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nft.dao.entity.FilePO;
import com.nft.dao.mapper.FileMapper;
import com.nft.dao.mapper.UserFileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Slf4j
@Component
public class NftFileTask {

    @Resource
    private UserFileMapper userFileMapper;

    @Resource
    private FileMapper fileMapper;

    @Scheduled(fixedRate = 1000 * 60)
    public void clearNftFile(){
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("file_status", 1);

            List<FilePO> filePOList = fileMapper.selectList(queryWrapper);

            for(FilePO filePO : filePOList){
                QueryWrapper queryWrapper2 = new QueryWrapper();
                queryWrapper2.ge("file_status", 2);
                queryWrapper2.eq("md5", filePO.getMd5());
                List<FilePO> filePOS = fileMapper.selectList(queryWrapper2);
                if((filePOS == null || filePOS.size() < 1) && (System.currentTimeMillis() - filePO.getCreateTime().getTime()) < 5 * 60 * 1000){
                    continue;
                }

                fileMapper.deleteById(filePO.getId());

                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("file_id", filePO.getId());
                userFileMapper.delete(updateWrapper);

                File file = new File(filePO.getFilePath());
                if(file.exists()){
                    file.delete();
                }
            }
        } catch (Exception e){
            log.error("清理过期nft异常", e);
        }
    }
}
