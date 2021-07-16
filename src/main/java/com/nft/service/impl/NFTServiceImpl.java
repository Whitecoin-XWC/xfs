package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.commons.util.LogUtil;
import com.nft.commons.vo.ResultVO;
import com.nft.controller.vo.*;
import com.nft.dao.entity.*;
import com.nft.dao.mapper.*;
import com.nft.service.AuctionService;
import com.nft.service.FileLogService;
import com.nft.service.NFTService;
import com.nft.service.dto.FileLogAttach;
import com.nft.service.dto.FileResultDTO;
import org.springframework.beans.BeanUtils;
import com.nft.service.dto.NoticeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class NFTServiceImpl implements NFTService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileLogMapper fileLogMapper;

    @Resource
    private FileLogService fileLogService;

    @Resource
    private UserFileMapper userFileMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private SellInfoMapper sellInfoMapper;

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    private AuctionMapper auctionMapper;

    @Resource
    private AuctionService auctionService;


    @Value("${fileUpload.img-url}")
    private String imgUrl;

    /**
     * 关注
     *
     * @param followVO
     * @return
     */
    @Override
    public int follow(FollowVO followVO) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", followVO.getUserAddress());
        queryWrapper.eq("file_id", followVO.getTokenId());
        queryWrapper.eq("type", 2);
        List<UserFilePO> userFilePOList = userFileMapper.selectList(queryWrapper);

        if (userFilePOList == null || userFilePOList.size() < 1) {
            UserFilePO userFilePO = new UserFilePO();
            userFilePO.setUserId(followVO.getUserAddress());
            userFilePO.setFileId(followVO.getTokenId());
            userFilePO.setType(2);
            userFilePO.setCreateTime(new Date());
            return userFileMapper.insert(userFilePO);
        }
        return 1;
    }

    /**
     * 取消关注
     *
     * @param followVO
     * @return
     */
    @Override
    public int delFollow(FollowVO followVO) {
        UpdateWrapper queryWrapper = new UpdateWrapper();
        queryWrapper.eq("user_id", followVO.getUserAddress());
        queryWrapper.eq("file_id", followVO.getTokenId());
        queryWrapper.eq("type", 2);
        return userFileMapper.delete(queryWrapper);
    }

    /**
     * 上传文件
     *
     * @param filePO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int upload(FilePO filePO) {
        FilePO fileItem = fileMapper.selectById(filePO.getId());
        if (fileItem != null && !StringUtils.isEmpty(fileItem.getId())) {
            return -1;
        }

        // 保存文件
        int result = fileMapper.insert(filePO);

        // 跟用户产生关联
        if (result > 0) {
            UserFilePO userFilePO = new UserFilePO();
            userFilePO.setCreateTime(new Date());
            userFilePO.setType(0);
            userFilePO.setFileId(filePO.getId());
            userFilePO.setUserId(filePO.getUserAddress());
            userFileMapper.insert(userFilePO);

            fileLogService.saveLog(filePO.getId(), filePO.getUserAddress() + "上传了这个NFT", 0, null);
        }
        return 1;
    }

    /**
     * 发布
     *
     * @param pubVO
     * @return
     */
    @Override
    public int pub(PubVO pubVO) {
        FilePO fileItem = fileMapper.selectById(pubVO.getTokenId());
        if (fileItem == null || StringUtils.isEmpty(fileItem.getId())) {
            return -1;
        }
        fileItem.setFileStatus(2);
        fileItem.setPubTime(new Date());
        fileMapper.updateById(fileItem);

        FileLogAttach fileLogAttach = new FileLogAttach();
        fileLogAttach.setTractionId(pubVO.getTractionId());
        fileLogService.saveLog(fileItem.getId(), pubVO.getUserAddress() + "发布了这个NFT", 0, fileLogAttach);
        return 1;
    }

    /**
     * 付费
     *
     * @param pubVO
     * @return
     */
    @Override
    public int pay(PubVO pubVO) {
        FilePO fileItem = fileMapper.selectById(pubVO.getTokenId());
        if (fileItem == null || StringUtils.isEmpty(fileItem.getId())) {
            return -1;
        }
        fileItem.setFileStatus(3);
        fileItem.setPayTime(new Date());
        fileMapper.updateById(fileItem);

        FileLogAttach fileLogAttach = new FileLogAttach();
        fileLogAttach.setTractionId(pubVO.getTractionId());
        fileLogService.saveLog(fileItem.getId(), pubVO.getUserAddress() + "付费了这个NFT", 0, fileLogAttach);

        return 1;
    }

    /**
     * 转移
     *
     * @return
     */
    @Override
    public int fileUserChange(FileUserChangeVO fileUserChangeVO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("file_id", fileUserChangeVO.getTokenId());
        UserFilePO userFilePO = userFileMapper.selectOne(queryWrapper);
        if (userFilePO == null) {
            return -1;
        }
        String oldUser = userFilePO.getUserId();
        userFilePO.setUserId(fileUserChangeVO.getUserAddress());
        userFileMapper.updateById(userFilePO);

        FileLogAttach fileLogAttach = new FileLogAttach();
        fileLogAttach.setTractionId(fileUserChangeVO.getTractionId());
        fileLogService.saveLog(fileUserChangeVO.getTokenId(), "这个NFT转从" + oldUser + "手里移给了" + fileUserChangeVO.getUserAddress(), 0, fileLogAttach);

        insertCopyrightFeeNotice(fileUserChangeVO.getTokenId(), fileUserChangeVO.getUserAddress(), fileUserChangeVO.getPrice(), fileUserChangeVO.getName());

        return 1;
    }

    /**
     * 获取一个文件
     *
     * @param filePO
     * @return
     */
    @Override
    public FilePO getFileDetail(FilePO filePO) {
        FilePO fileDetail = fileMapper.selectById(filePO.getId());
        if (fileDetail == null || StringUtils.isEmpty(fileDetail.getId())) {
            return null;
        }
        QueryWrapper<UserFilePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", filePO.getId());
        queryWrapper.eq("type", 0);
        UserFilePO userFilePO = userFileMapper.selectOne(queryWrapper);
        fileDetail.setUserAddress(userFilePO.getUserId());


        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("token_id", filePO.getId());
        SellInfoPO sellInfoPO = sellInfoMapper.selectOne(queryWrapper1);
        if (sellInfoPO != null) {
            fileDetail.setUnit(sellInfoPO.getUnit());
            fileDetail.setPrice(sellInfoPO.getPrice());
        }
        if (StringUtils.isEmpty(filePO.getUserAddress())) {
            fileDetail.setCollect(0);
        } else {
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("user_id", filePO.getUserAddress());
            queryWrapper2.eq("file_id", filePO.getId());
            queryWrapper2.eq("type", 2);
            UserFilePO userFilePO1 = userFileMapper.selectOne(queryWrapper2);
            if (userFilePO1 != null) {
                fileDetail.setCollect(1);
            } else {
                fileDetail.setCollect(0);
            }
        }
        return fileDetail;
    }

    /**
     * 获取一个文件变化日志
     *
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
     *
     * @return
     */
    @Override
    public IPage<FileResultDTO> selectFiles(FileVO fileVO) {
        IPage<FileResultDTO> pageWrapper = new Page<>(fileVO.getPage(), fileVO.getPageSize());

        FileResultDTO fileResultDTO = new FileResultDTO();
        if (!StringUtils.isEmpty(fileVO.getUserAddress())) {
            fileResultDTO.setUserAddress(fileVO.getUserAddress());
        }
        if (fileVO.getMediaType() != null && fileVO.getMediaType() > -1) {
            fileResultDTO.setMediaType(fileVO.getMediaType());
        }
        if (fileVO.getStatus() != null && fileVO.getStatus() > -1) {
            fileResultDTO.setFileStatus(fileVO.getStatus());
        }
        if (!StringUtils.isEmpty(fileVO.getTokenId())) {
            fileResultDTO.setId(fileVO.getTokenId());
        }
        if (fileVO.getSource() != null && fileVO.getSource() > -1) {
            fileResultDTO.setSource(fileVO.getSource());
        }
        return fileMapper.selectFileList(pageWrapper, fileResultDTO);
    }

    /**
     * 搜索
     *
     * @param keyWord
     * @return
     */
    @Override
    public Map<String, Object> search(String keyWord) {

        Map<String, Object> resultMap = new HashMap<>();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("id", keyWord);
        queryWrapper.or();
        queryWrapper.like("nick_name", keyWord);

        Page page = userInfoMapper.selectPage(new Page<>(1, 100), queryWrapper);
        if (page != null && page.getRecords() != null) {
            resultMap.put("users", page.getRecords());
        } else {
            resultMap.put("users", new ArrayList<>());
        }


        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.like("file_title", keyWord);

        Page page1 = fileMapper.selectPage(new Page<>(1, 100), queryWrapper1);
        if (page1 != null && page1.getRecords() != null) {
            resultMap.put("imagUrl", imgUrl);

            List<FileResultDTO> fileResultDTOList = new ArrayList<>();

            List<FilePO> filePOList = page1.getRecords();
            for(FilePO filePO : filePOList){
                FileResultDTO fileResultDTO = new FileResultDTO();
                BeanUtils.copyProperties(filePO, fileResultDTO);

                QueryWrapper queryWrapper2 = new QueryWrapper();
                queryWrapper2.eq("file_id", filePO.getId());
                List<UserFilePO> userFilePOList = userFileMapper.selectList(queryWrapper2);
                if(userFilePOList != null && userFilePOList.size() > 0){
                    fileResultDTO.setUserName(userFilePOList.get(0).getUserName());
                }
                fileResultDTOList.add(fileResultDTO);
            }

            resultMap.put("files", fileResultDTOList);
        } else {
            resultMap.put("files", new ArrayList<>());
        }
        return resultMap;
    }

    @Override
    public NoticeResult getNotice(NoticeVo noticeVo) {
        NoticeResult results = new NoticeResult();
        List<NoticeResult.MyAuction> myAuctions = new ArrayList<>();
        List<NoticeResult.CopyrightFee> copyrightFees = new ArrayList<>();

        QueryWrapper<NoticeEntity> noticeQueryWrapper = new QueryWrapper<>();
        noticeQueryWrapper.eq("notice_pople", noticeVo.getUserAddress());
        noticeQueryWrapper.orderByDesc("create_time");
        Page<NoticeEntity> noticeEntityPage = noticeMapper.selectPage(new Page<>(noticeVo.getPageNo(), noticeVo.getPageSize()), noticeQueryWrapper);
        if (noticeEntityPage == null) {
            return new NoticeResult();
        }
        List<NoticeEntity> records = noticeEntityPage.getRecords();
        for (NoticeEntity record : records) {
            String noticeFile = record.getNoticeFile();
            /* 获取nft信息 */
            QueryWrapper<FilePO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", noticeFile);
            FilePO filePO = fileMapper.selectById(queryWrapper);
            if (filePO == null) {
                break;
            }

            if (record.getNoticeType() == 2) {
                /* 版权费通知 */
                NoticeResult.CopyrightFee copyrightFee = getCopyrightFee(record, filePO.getFileName());
                copyrightFees.add(copyrightFee);
            }
            if (record.getNoticeType() == 1) {
                /* 您的竞拍通知 */
                long auctionId = record.getAuctionId();
                AuctionEntity auctionEntity = auctionMapper.selectById(auctionId);
                if (auctionEntity == null) {
                    break;
                }
                NoticeResult.MyAuction myAuction = getMyAuction(auctionEntity, noticeVo.getUserAddress(), filePO.getFileName());

                myAuctions.add(myAuction);
            }

        }
        results.setMyAuction(myAuctions);
        results.setCopyrightFee(copyrightFees);
        return results;
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

    private NoticeResult.CopyrightFee getCopyrightFee(NoticeEntity record, String fileName) {
        NoticeResult.CopyrightFee copyrightFee = new NoticeResult.CopyrightFee();
        copyrightFee.setCopyrightFee(record.getCopyrightFee());
        BigDecimal coinPrice = auctionService.getCoinPrice(record.getCoinType());
        copyrightFee.setCopyrightFeeUsdt(record.getCopyrightFee().multiply(coinPrice));
        copyrightFee.setNFTName(fileName);
        copyrightFee.setProvider(record.getRecipient());
        return copyrightFee;
    }

    private NoticeResult.MyAuction getMyAuction(AuctionEntity auctionEntity, String userAddress, String fileName) {


        NoticeResult.MyAuction myAuction = new NoticeResult.MyAuction();
        /* 获取拍卖信息 */

        if (auctionEntity.getAuctionStatus() == 1 && userAddress.equals(auctionEntity.getAuctionMaxEr())) {
            myAuction.setAuctionResult("拍卖最高价");
            myAuction.setAuctionPrice(auctionEntity.getAuctionMaxPrice());
            BigDecimal coinPrice = auctionService.getCoinPrice(auctionEntity.getAuctionCoin());
            myAuction.setAuctionPriceUsdt(auctionEntity.getAuctionMaxPrice().multiply(coinPrice));
            /* 计算拍卖倒计时 */
            Date auctionStartTime = auctionEntity.getAuctionStartTime();
            LocalDateTime localDateTime = auctionStartTime.toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();
            Duration between = Duration.between(LocalDateTime.now(), localDateTime.plusHours(24));
            long millis = between.toMillis();
            if (millis >= 0) {
                myAuction.setRemainingTime(between.toMillis());
            }
        }
        if (auctionEntity.getAuctionStatus() == 2 && userAddress.equals(auctionEntity.getAuctionMaxEr())) {
            myAuction.setAuctionResult("获胜");
        }

        if (auctionEntity.getAuctionStatus() == 2 && !userAddress.equals(auctionEntity.getAuctionMaxEr())) {
            myAuction.setAuctionResult("淘汰");
        }
        myAuction.setNFTName(fileName);
        return myAuction;
    }
}
