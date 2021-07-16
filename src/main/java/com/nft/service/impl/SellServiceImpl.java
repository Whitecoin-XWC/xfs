package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nft.controller.vo.BuyVO;
import com.nft.controller.vo.SellVO;
import com.nft.dao.entity.*;
import com.nft.dao.mapper.FileLogMapper;
import com.nft.dao.mapper.FileMapper;
import com.nft.dao.mapper.SellInfoMapper;
import com.nft.dao.mapper.UserFileMapper;
import com.nft.service.FileLogService;
import com.nft.service.NFTService;
import com.nft.service.SellService;
import com.nft.service.dto.FileLogAttach;
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

    @Resource
    private NFTService nftService;

    @Override
    @Transactional
    public int sell(SellVO sellVO) {

        FileLogAttach fileLogAttach = new FileLogAttach();
        fileLogAttach.setTractionId(sellVO.getTractionId());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("token_id", sellVO.getTokenId());
        SellInfoPO sellInfoPO = sellInfoMapper.selectOne(queryWrapper);
        if(sellInfoPO != null){
            sellInfoPO.setPrice(sellVO.getPrice());
            sellInfoPO.setUnit(sellVO.getUnit());
            sellInfoMapper.updateById(sellInfoPO);
            saveLog(sellVO.getTokenId(), sellVO.getUserAddress(), "更改了这个NFT的售价", fileLogAttach);
        } else {
            sellInfoPO = new SellInfoPO();
            sellInfoPO.setUnit(sellVO.getUnit());
            sellInfoPO.setPrice(sellVO.getPrice());
            sellInfoPO.setStatus(0);
            sellInfoPO.setTokenId(sellVO.getTokenId());
            sellInfoMapper.insert(sellInfoPO);

            updateFileStatus(sellVO.getTokenId(), 4);

            saveLog(sellVO.getTokenId(), sellVO.getUserAddress(), "设置了这个NFT的售价", fileLogAttach);
        }

        return 1;
    }

    @Override
    @Transactional
    public int delSell(SellVO sellVO) {
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("token_id", sellVO.getTokenId());

        updateFileStatus(sellVO.getTokenId(), 2);

        FileLogAttach fileLogAttach = new FileLogAttach();
        fileLogAttach.setTractionId(sellVO.getTractionId());
        saveLog(sellVO.getTokenId(), sellVO.getUserAddress(), "取消了这个NFT的售价", fileLogAttach);
        return sellInfoMapper.delete(wrapper);
    }

    @Override
    @Transactional
    public int buySuccess(BuyVO buyVO) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("file_id", buyVO.getTokenId());
        queryWrapper.eq("type", 0);
        UserFilePO userFilePO = userFileMapper.selectOne(queryWrapper);
        if(userFilePO == null){
            return -1;
        }

        userFilePO.setUserId(buyVO.getBuyUserAddress());
        userFileMapper.updateById(userFilePO);

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("token_id", buyVO.getTokenId());
        SellInfoPO sellInfoPO = sellInfoMapper.selectOne(updateWrapper);
        sellInfoMapper.delete(updateWrapper);

        updateFileStatus(buyVO.getTokenId(), 2);

        FileLogAttach fileLogAttach = new FileLogAttach();
        fileLogAttach.setTractionId(buyVO.getTractionId());
        saveLog(buyVO.getTokenId(), buyVO.getBuyUserAddress(), "购买了这个NFT", fileLogAttach);
        /* 插入通知记录 */
        nftService.insertCopyrightFeeNotice(buyVO.getTokenId(),buyVO.getBuyUserAddress(),sellInfoPO.getPrice(),sellInfoPO.getUnit());
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
    private void saveLog(String fileId, String userTag, String action, FileLogAttach logAttach){
        fileLogService.saveLog(fileId,userTag + action, 2, logAttach);
    }
}
