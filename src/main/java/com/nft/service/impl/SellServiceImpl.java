package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nft.controller.vo.BuyVO;
import com.nft.controller.vo.SellVO;
import com.nft.dao.entity.FileLogPO;
import com.nft.dao.entity.FilePO;
import com.nft.dao.entity.SellInfoPO;
import com.nft.dao.entity.UserFilePO;
import com.nft.dao.mapper.FileLogMapper;
import com.nft.dao.mapper.FileMapper;
import com.nft.dao.mapper.SellInfoMapper;
import com.nft.dao.mapper.UserFileMapper;
import com.nft.service.FileLogService;
import com.nft.service.SellService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("sellService")
public class SellServiceImpl implements SellService {

    @Resource
    private SellInfoMapper sellInfoMapper;

    @Resource
    private UserFileMapper userFileMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileLogService fileLogService;

    @Override
    @Transactional
    public int sell(SellVO sellVO) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("token_id", sellVO.getTokenId());
        SellInfoPO sellInfoPO = sellInfoMapper.selectOne(queryWrapper);
        if(sellInfoPO != null){
            sellInfoPO.setPrice(sellVO.getPrice());
            sellInfoPO.setUnit(sellVO.getUnit());
            sellInfoMapper.updateById(sellInfoPO);
            saveLog(sellVO.getTokenId(), sellVO.getUserAddress(), "更改了这个NFT的售价");
        } else {
            sellInfoPO = new SellInfoPO();
            sellInfoPO.setUnit(sellVO.getUnit());
            sellInfoPO.setPrice(sellVO.getPrice());
            sellInfoPO.setStatus(0);
            sellInfoPO.setTokenId(sellVO.getTokenId());
            sellInfoMapper.insert(sellInfoPO);

            updateFileStatus(sellVO.getTokenId(), 4);

            saveLog(sellVO.getTokenId(), sellVO.getUserAddress(), "设置了这个NFT的售价");
        }

        return 1;
    }

    @Override
    @Transactional
    public int delSell(SellVO sellVO) {
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("token_id", sellVO.getTokenId());

        updateFileStatus(sellVO.getTokenId(), 2);

        saveLog(sellVO.getTokenId(), sellVO.getUserAddress(), "取消了这个NFT的售价");
        return sellInfoMapper.delete(wrapper);
    }

    @Override
    @Transactional
    public int buySuccess(BuyVO buyVO) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("file_id", buyVO.getTokenId());
        UserFilePO userFilePO = userFileMapper.selectOne(queryWrapper);
        if(userFilePO == null){
            return -1;
        }

        userFilePO.setUserId(buyVO.getBuyUserAddress());
        userFileMapper.updateById(userFilePO);

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("token_id", buyVO.getTokenId());
        sellInfoMapper.delete(updateWrapper);

        updateFileStatus(buyVO.getTokenId(), 2);

        saveLog(buyVO.getTokenId(), buyVO.getBuyUserAddress(), "购买了这个NFT");
        return 0;
    }

    private void updateFileStatus(String tokenId, Integer status){
        FilePO filePO = fileMapper.selectById(tokenId);
        filePO.setFileStatus(status);
        fileMapper.updateById(filePO);
    }

    /**
     * 保存日志
     * @param fileId
     * @param userTag
     * @param action
     */
    private void saveLog(String fileId, String userTag, String action){
        fileLogService.saveLog(fileId,userTag + action, 2, null);
    }
}
