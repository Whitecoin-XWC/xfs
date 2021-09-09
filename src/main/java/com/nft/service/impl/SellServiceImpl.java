package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nft.controller.vo.BuyVO;
import com.nft.controller.vo.SellVO;
import com.nft.dao.entity.*;
import com.nft.dao.mapper.FileMapper;
import com.nft.dao.mapper.SellInfoMapper;
import com.nft.dao.mapper.UserFileMapper;
import com.nft.service.FileLogService;
import com.nft.service.NoticeService;
import com.nft.service.OrderService;
import com.nft.service.SellService;
import com.nft.service.dto.FileLogAttach;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

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
    private NoticeService noticeService;

    @Resource
    private OrderService orderService;

    @Override
    @Transactional
    public int sell(SellVO sellVO) {

        FileLogAttach fileLogAttach = new FileLogAttach();
        fileLogAttach.setTractionId(sellVO.getTractionId());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("token_id", sellVO.getTokenId());
        SellInfoPO sellInfoPO = sellInfoMapper.selectOne(queryWrapper);
        if (sellInfoPO != null) {
            sellInfoPO.setPrice(sellVO.getPrice());
            sellInfoPO.setUnit(sellVO.getUnit());
            sellInfoMapper.updateById(sellInfoPO);

            fileLogAttach.setPrice(sellVO.getPrice());
            fileLogAttach.setCoinType(StringUtils.isBlank(sellVO.getUnit()) ? sellInfoPO.getUnit() : sellVO.getUnit());
            saveLog(sellVO.getTokenId(), sellVO.getUserAddress(), "更改了这个NFT的售价", fileLogAttach);
        } else {
            sellInfoPO = new SellInfoPO();
            sellInfoPO.setUnit(sellVO.getUnit());
            sellInfoPO.setPrice(sellVO.getPrice());
            sellInfoPO.setStatus(0);
            sellInfoPO.setTokenId(sellVO.getTokenId());
            sellInfoMapper.insert(sellInfoPO);

            updateFileStatus(sellVO.getTokenId(), 4);
            fileLogAttach.setPrice(sellVO.getPrice());
            fileLogAttach.setCoinType(sellVO.getUnit());
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

        QueryWrapper<UserFilePO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("file_id", buyVO.getTokenId());
        queryWrapper.eq("type", 1);
        UserFilePO userFilePO = userFileMapper.selectOne(queryWrapper);
        if (userFilePO == null) {
            /* 插入用户拥有表 */
            userFilePO = new UserFilePO();
            userFilePO.setCreateTime(new Date());
            userFilePO.setFileId(buyVO.getTokenId());
            userFilePO.setUserId(buyVO.getBuyUserAddress());
            userFilePO.setType(1);
            userFileMapper.insert(userFilePO);
        } else {
            /* 修改拥有者 */
            userFilePO.setUserId(buyVO.getBuyUserAddress());
            userFileMapper.updateById(userFilePO);
        }

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("token_id", buyVO.getTokenId());
        SellInfoPO sellInfoPO = sellInfoMapper.selectOne(updateWrapper);

        /* buy success insert order */
        OrderPO orderPO = new OrderPO();
        orderPO.setUserAddress(buyVO.getBuyUserAddress());
        orderPO.setPrice(sellInfoPO.getPrice() + sellInfoPO.getUnit());
        orderPO.setTokenId(buyVO.getTokenId());
        orderService.saveOrder(orderPO);

        sellInfoMapper.delete(updateWrapper);

        updateFileStatus(buyVO.getTokenId(), 2);

        FileLogAttach fileLogAttach = new FileLogAttach();
        fileLogAttach.setTractionId(buyVO.getTractionId());
        fileLogAttach.setPrice(sellInfoPO.getPrice());
        fileLogAttach.setCoinType(sellInfoPO.getUnit());
        saveLog(buyVO.getTokenId(), buyVO.getBuyUserAddress(), "购买了这个NFT", fileLogAttach);
        /* 插入通知记录 */
        noticeService.insertCopyrightFeeNotice(buyVO.getTokenId(), buyVO.getBuyUserAddress(), sellInfoPO.getPrice(), sellInfoPO.getUnit());
        return 0;
    }

    private void updateFileStatus(String tokenId, Integer status) {
        FilePO filePO = fileMapper.selectById(tokenId);
        filePO.setFileStatus(status);
        fileMapper.updateById(filePO);
    }

    /**
     * save log
     *
     * @param fileId
     * @param userTag
     * @param action
     */
    private void saveLog(String fileId, String userTag, String action, FileLogAttach logAttach) {
        fileLogService.saveLog(fileId, action, userTag, 2, logAttach);
    }
}
