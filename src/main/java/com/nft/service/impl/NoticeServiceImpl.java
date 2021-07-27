package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.controller.vo.NoticeVo;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.FilePO;
import com.nft.dao.entity.NoticeEntity;
import com.nft.dao.mapper.AuctionMapper;
import com.nft.dao.mapper.FileMapper;
import com.nft.dao.mapper.NoticeMapper;
import com.nft.service.AuctionService;
import com.nft.service.NoticeService;
import com.nft.service.dto.NoticeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: nft
 * Created by moloq on 2021/7/19 10:26
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeEntity> implements NoticeService {

    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private FileMapper fileMapper;
    @Resource
    private AuctionMapper auctionMapper;
    @Resource
    private AuctionService auctionService;

    @Value("${fileUpload.img-url}")
    private String imgUrl;

    @Override
    public NoticeResult getNotice(NoticeVo noticeVo) {
        NoticeResult result = new NoticeResult();
        List<NoticeResult.MyAuction> myAuctions = new ArrayList<>();
        List<NoticeResult.CopyrightFee> copyrightFees = new ArrayList<>();

        QueryWrapper<NoticeEntity> noticeQueryWrapper = new QueryWrapper<>();
        noticeQueryWrapper.eq("notice_pople", noticeVo.getUserAddress());
        noticeQueryWrapper.eq("notice_type", noticeVo.getType());
        noticeQueryWrapper.orderByDesc("create_time");
        Page<NoticeEntity> noticeEntityPage = noticeMapper.selectPage(new Page<>(noticeVo.getPageNo(), noticeVo.getPageSize()), noticeQueryWrapper);
        if (noticeEntityPage == null) {
            return new NoticeResult();
        }
        result.setImgUrl(imgUrl);
        List<NoticeEntity> records = noticeEntityPage.getRecords();
        for (NoticeEntity record : records) {
            String noticeFile = record.getNoticeFile();
            /* 获取nft信息 */
            FilePO filePO = fileMapper.selectById(noticeFile);
            if (filePO == null) {
                break;
            }

            if (record.getNoticeType() == 2) {
                /* 版权费通知 */
                NoticeResult.CopyrightFee copyrightFee = getCopyrightFee(record, filePO);
                copyrightFees.add(copyrightFee);
            }
            if (record.getNoticeType() == 1) {
                /* 您的竞拍通知 */
                long auctionId = record.getAuctionId();
                AuctionEntity auctionEntity = auctionMapper.selectById(auctionId);
                if (auctionEntity == null) {
                    break;
                }
                NoticeResult.MyAuction myAuction = getMyAuction(auctionEntity, noticeVo.getUserAddress(), filePO, record.getCoinType());

                myAuctions.add(myAuction);
            }

            /* 修改通知状态为已读 */
            record.setNoticeStatus(true);
        }
        result.setMyAuction(myAuctions);
        result.setCopyrightFee(copyrightFees);

        result.setCount(noticeEntityPage.getTotal());
        result.setCurrentPage(noticeEntityPage.getCurrent());
        result.setPageSize(noticeEntityPage.getSize());
        result.setPageTotal(noticeEntityPage.getPages());
        /* 将已读的通知修改状态 */
        saveOrUpdateBatch(records);
        return result;
    }

    @Override
    public void insertCopyrightFeeNotice(String fileId, String buyUserAddress, BigDecimal price, String coinType) {
        FilePO filePO = fileMapper.selectById(fileId);
        if (filePO == null) {
            return;
        }
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setNoticeFile(fileId);
        noticeEntity.setNoticePople(filePO.getCreater());
        noticeEntity.setCopyrightFee(price.multiply(filePO.getCopyrightFee()).divide(new BigDecimal(100)));
        noticeEntity.setCoinType(coinType);
        noticeEntity.setNoticeType(2);
        noticeEntity.setCreateTime(new Date());
        noticeMapper.insert(noticeEntity);
    }

    @Override
    public void insertAuctionNotice(long auctionId, String fileId, String auctionner) {
        /* 查询本次拍卖是否有了通知记录 */
        QueryWrapper<NoticeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("auction_id", auctionId);
        queryWrapper.eq("notice_pople", auctionner);
        List<NoticeEntity> noticeEntities = noticeMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(noticeEntities)) {
            /* 已经有了拍卖记录 不需要重新插入 */
            return;
        }
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setAuctionId(auctionId);
        noticeEntity.setNoticePople(auctionner);
        noticeEntity.setNoticeFile(fileId);
        noticeEntity.setNoticeType(1);
        noticeEntity.setCreateTime(new Date());
    }

    @Override
    public int getNoticeCount(String userAddress) {
        QueryWrapper<NoticeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notice_status", 0);
        queryWrapper.eq("notice_pople", userAddress);
        return noticeMapper.selectCount(queryWrapper);
    }

    private NoticeResult.CopyrightFee getCopyrightFee(NoticeEntity record, FilePO filePO) {
        NoticeResult.CopyrightFee copyrightFee = new NoticeResult.CopyrightFee();
        copyrightFee.setCopyrightFee(record.getCopyrightFee());
        BigDecimal coinPrice = auctionService.getCoinPrice(record.getCoinType());
        copyrightFee.setCopyrightFeeUsdt(record.getCopyrightFee().multiply(coinPrice).setScale(8, BigDecimal.ROUND_DOWN));
        copyrightFee.setNFTName(filePO.getFileTitle());
        copyrightFee.setFilePath(filePO.getFileName());
        copyrightFee.setCoinType(record.getCoinType());
        copyrightFee.setProvider(record.getRecipient());
        copyrightFee.setTokenId(filePO.getId());
        return copyrightFee;
    }

    private NoticeResult.MyAuction getMyAuction(AuctionEntity auctionEntity, String userAddress, FilePO filePO, String coinType) {


        NoticeResult.MyAuction myAuction = new NoticeResult.MyAuction();
        /* 获取拍卖信息 */

        if (auctionEntity.getAuctionStatus() == 1 && userAddress.equals(auctionEntity.getAuctionMaxEr())) {
            myAuction.setAuctionResult("拍卖最高价");
            myAuction.setAuctionPrice(auctionEntity.getAuctionMaxPrice());
            BigDecimal coinPrice = auctionService.getCoinPrice(auctionEntity.getAuctionCoin());
            myAuction.setAuctionPriceUsdt(auctionEntity.getAuctionMaxPrice().multiply(coinPrice).setScale(8, BigDecimal.ROUND_DOWN));
            /* 计算拍卖倒计时 */
            Date auctionStartTime = auctionEntity.getAuctionStartTime();
            if (auctionStartTime != null) {
                LocalDateTime localDateTime = auctionStartTime.toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();
                Duration between = Duration.between(LocalDateTime.now(), localDateTime.plusHours(24));
                long millis = between.toMillis();
                if (millis >= 0) {
                    myAuction.setRemainingTime(between.toMillis());
                }
            }
        }
        if (auctionEntity.getAuctionStatus() == 2 && userAddress.equals(auctionEntity.getAuctionMaxEr())) {
            myAuction.setAuctionResult("获胜");
        }

        if (auctionEntity.getAuctionStatus() == 2 && !userAddress.equals(auctionEntity.getAuctionMaxEr())) {
            myAuction.setAuctionResult("淘汰");
        }
        myAuction.setNFTName(filePO.getFileTitle());
        myAuction.setFilePath(filePO.getFileName());
        myAuction.setCoinType(coinType);
        myAuction.setTokenId(filePO.getId());
        return myAuction;
    }
}
