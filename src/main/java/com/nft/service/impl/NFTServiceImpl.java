package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.commons.util.FileUtil;
import com.nft.controller.vo.*;
import com.nft.dao.entity.*;
import com.nft.dao.mapper.*;
import com.nft.service.AuctionService;
import com.nft.service.FileLogService;
import com.nft.service.NFTService;
import com.nft.service.NoticeService;
import com.nft.service.dto.FileLogAttach;
import com.nft.service.dto.FileResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
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
    private ShopMapper shopMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private SellInfoMapper sellInfoMapper;

    @Resource
    private NoticeService noticeService;

    @Resource
    private AuctionService auctionService;

    @Value("${fileUpload.img-url}")
    private String imgUrl;

    private Lock lock = new ReentrantLock();

    /**
     * follow
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
            UserFilePO followFilePO = new UserFilePO();
            followFilePO.setUserId(followVO.getUserAddress());
            followFilePO.setFileId(followVO.getTokenId());
            followFilePO.setType(2);
            followFilePO.setCreateTime(new Date());
            return userFileMapper.insert(followFilePO);
        }
        return 1;
    }

    /**
     * chancel follow
     *
     * @param followVO
     * @return
     */
    @Override
    public int delFollow(FollowVO followVO) {
        UpdateWrapper<UserFilePO> queryWrapper = new UpdateWrapper();
        queryWrapper.eq("user_id", followVO.getUserAddress());
        queryWrapper.eq("file_id", followVO.getTokenId());
        queryWrapper.eq("type", 2);
        return userFileMapper.delete(queryWrapper);
    }

    /**
     * update file
     *
     * @param filePO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public int upload(FilePO filePO) {
        // ????????????
        int result = fileMapper.insert(filePO);

        // ?????????????????????
        if (result > 0) {

            /* ??????????????? */
            UserFilePO createFilePO = new UserFilePO();
            createFilePO.setCreateTime(new Date());
            createFilePO.setFileId(filePO.getId());
            createFilePO.setUserId(filePO.getUserAddress());

            UserinfoPO userinfoPO = userInfoMapper.selectById(filePO.getUserAddress());
            if (userinfoPO != null) {
                createFilePO.setUserName(userinfoPO.getNickName());
            }

            createFilePO.setType(0);
            userFileMapper.insert(createFilePO);
            fileLogService.saveLog(filePO.getId(), "???????????????NFT", filePO.getUserAddress(), 0, null);
        }
        return 1;
    }

    /**
     * pub file
     *
     * @param pubVO
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int pub(PubVO pubVO) {
        try {
            lock.lock();
            FilePO fileItem = fileMapper.selectById(pubVO.getTokenId());
            if (fileItem == null || StringUtils.isEmpty(fileItem.getId())) {
                return -1;
            }

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("md5", fileItem.getMd5());
            queryWrapper.ge("file_status", 2);
            List<FilePO> pubFiles = fileMapper.selectList(queryWrapper);
            if (pubFiles != null && pubFiles.size() > 0) {
                return -2;
            }

            fileItem.setFileStatus(2);
            fileItem.setPubTime(new Date());
            fileMapper.updateById(fileItem);

            FileLogAttach fileLogAttach = new FileLogAttach();
            fileLogAttach.setTractionId(pubVO.getTractionId());
            fileLogService.saveLog(fileItem.getId(), "???????????????NFT", pubVO.getUserAddress(), 0, fileLogAttach);
            return 1;
        } catch (Exception e) {
            log.error("??????NFT??????,tokenId:{}", pubVO.getTokenId(), e);
            return -3;
        } finally {
            lock.unlock();
        }
    }

    /**
     * pay
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
        fileLogService.saveLog(fileItem.getId(), "???????????????NFT", pubVO.getUserAddress(), 0, fileLogAttach);

        return 1;
    }

    /**
     * change user
     *
     * @return
     */
    @Override
    public int fileUserChange(FileUserChangeVO fileUserChangeVO) {
        QueryWrapper<UserFilePO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("file_id", fileUserChangeVO.getTokenId());
        queryWrapper.eq("type", 1);
        UserFilePO userFilePO = userFileMapper.selectOne(queryWrapper);
        String oldUser = "";
        if (userFilePO == null) {
            /* ????????????????????? */
            userFilePO = new UserFilePO();
            userFilePO.setCreateTime(new Date());
            userFilePO.setFileId(fileUserChangeVO.getTokenId());
            userFilePO.setUserId(fileUserChangeVO.getUserAddress());
            userFilePO.setType(1);
            userFileMapper.insert(userFilePO);

            queryWrapper = new QueryWrapper();
            queryWrapper.eq("file_id", fileUserChangeVO.getTokenId());
            queryWrapper.eq("type", 0);
            userFilePO = userFileMapper.selectOne(queryWrapper);
            if (userFilePO != null) {
                oldUser = userFilePO.getUserId();
            }
        } else {
            /* ??????????????? */
            oldUser = userFilePO.getUserId();
            userFilePO.setUserId(fileUserChangeVO.getUserAddress());
            userFileMapper.updateById(userFilePO);
        }

        FileLogAttach fileLogAttach = new FileLogAttach();
        fileLogAttach.setTractionId(fileUserChangeVO.getTractionId());
        fileLogService.saveLog(fileUserChangeVO.getTokenId(),
                "???????????????NFT", oldUser, 0, fileLogAttach);

        noticeService.insertCopyrightFeeNotice(fileUserChangeVO.getTokenId(),
                fileUserChangeVO.getUserAddress(), fileUserChangeVO.getPrice(), fileUserChangeVO.getName());

        return 1;
    }

    /**
     * get file detail
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

        if (new Integer(0).compareTo(fileDetail.getMediaType()) == 0) {
            fileDetail.setTxtContent(FileUtil.getContent(fileDetail.getFilePath()));
        }

        QueryWrapper<UserFilePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", filePO.getId());
        queryWrapper.eq("type", 1);
        UserFilePO userFilePO = userFileMapper.selectOne(queryWrapper);
        String userId = "";
        if (userFilePO == null) {
            userId = fileDetail.getCreater();
        } else {
            userId = userFilePO.getUserId();
        }
        /* ?????????????????? */
        UserinfoPO userinfoPO = userInfoMapper.selectById(userId);
        if (userinfoPO != null && !StringUtils.isEmpty(userinfoPO.getNickName())) {
            fileDetail.setUserAddressNickName(userinfoPO.getNickName());
        }
        fileDetail.setUserAddress(userId);

        /* ???????????????????????? */
        String creater = fileDetail.getCreater();
        UserinfoPO createUserInfo = userInfoMapper.selectById(creater);
        if (createUserInfo != null && !StringUtils.isEmpty(createUserInfo.getNickName())) {
            fileDetail.setCreaterNickName(createUserInfo.getNickName());
        }

        if (userId.equals(creater) && shopMapper.selectById(creater) != null) {
            fileDetail.setEntity("true");
        } else {
            fileDetail.setEntity("false");
        }


        QueryWrapper<SellInfoPO> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("token_id", filePO.getId());
        SellInfoPO sellInfoPO = sellInfoMapper.selectOne(queryWrapper1);
        if (sellInfoPO != null) {
            fileDetail.setUnit(sellInfoPO.getUnit());
            fileDetail.setPrice(sellInfoPO.getPrice());
            BigDecimal coinPrice = auctionService.getCoinPrice(sellInfoPO.getUnit());
            fileDetail.setPriceUsdt(sellInfoPO.getPrice().multiply(coinPrice).setScale(8, BigDecimal.ROUND_DOWN));
        }
        if (StringUtils.isEmpty(filePO.getUserAddress())) {
            fileDetail.setCollect(0);
        } else {
            QueryWrapper<UserFilePO> queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("user_id", filePO.getUserAddress());
            queryWrapper2.eq("file_id", filePO.getId());
            queryWrapper2.eq("type", 2);
            UserFilePO followFilePO = userFileMapper.selectOne(queryWrapper2);
            if (followFilePO != null) {
                fileDetail.setCollect(1);
            } else {
                fileDetail.setCollect(0);
            }
        }
        return fileDetail;
    }

    /**
     * get log for file
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
        Page<FileLogPO> resultPage = fileLogMapper.selectPage(fileLogPOPage, queryWrapper);
        if (resultPage == null) {
            return resultPage;
        }
        List<FileLogPO> records = resultPage.getRecords();
        for (FileLogPO record : records) {
            String userId = record.getUserId();
            UserinfoPO userinfoPO = userInfoMapper.selectById(userId);
            if (userinfoPO != null && !StringUtils.isEmpty(userinfoPO.getNickName())) {
                record.setUserId(userinfoPO.getNickName());
            }
        }
        return resultPage;
    }

    /**
     * get file list for user
     *
     * @return
     */
    @Override
    public IPage<FileResultDTO> selectFiles(FileVO fileVO) {
        IPage<FileResultDTO> pageWrapper = new Page<>(fileVO.getPage(), fileVO.getPageSize());

        FileResultDTO fileResultDTO = new FileResultDTO();
        fileResultDTO.setIsIndex(fileVO.getIsIndex());

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
        if (!StringUtils.isEmpty(fileVO.getSource())) {
            fileResultDTO.setSource(fileVO.getSource());
        }
        if (!StringUtils.isEmpty(fileVO.getEntity())) {
            fileResultDTO.setEntity(fileVO.getEntity());
        }

        IPage<FileResultDTO> iPage = fileMapper.selectFileList(pageWrapper, fileResultDTO);

        if (iPage != null && iPage.getRecords() != null) {
            for (FileResultDTO fileResultDTO1 : iPage.getRecords()) {
                if (new Integer(0).compareTo(fileResultDTO1.getMediaType()) == 0) {
                    fileResultDTO1.setTxtContent(FileUtil.getContent(fileResultDTO1.getFilePath()));
                }

                if (fileResultDTO1.getFileStatus() == 5) {
                    AuctionEntity auctionEntity = auctionService.queryAuction(fileResultDTO1.getId());
                    fileResultDTO1.setAuctionMaxPrice(auctionEntity.getAuctionMaxPrice());
                    fileResultDTO1.setRemainingTime(auctionEntity.getRemainingTime());
                    fileResultDTO1.setCoinType(auctionEntity.getAuctionCoin());
                    fileResultDTO1.setPmPrice(auctionEntity.getAuctionRetainPrice());
                    fileResultDTO1.setUnit(auctionEntity.getAuctionCoin());
                }

                if (fileResultDTO1.getCreater().equals(fileResultDTO1.getUserAddress())
                        && shopMapper.selectById(fileResultDTO1.getCreater()) != null) {
                    fileResultDTO1.setEntity("true");
                } else {
                    fileResultDTO1.setEntity("false");
                }
            }
        }
        return iPage;
    }

    /**
     * search
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
            for (FilePO filePO : filePOList) {
                FileResultDTO fileResultDTO = new FileResultDTO();
                BeanUtils.copyProperties(filePO, fileResultDTO);

                QueryWrapper<UserFilePO> queryWrapper2 = new QueryWrapper();
                queryWrapper2.eq("file_id", filePO.getId());
                queryWrapper2.eq("type", 1);
                List<UserFilePO> userFilePOList = userFileMapper.selectList(queryWrapper2);
                if (userFilePOList != null && userFilePOList.size() > 0) {
                    fileResultDTO.setUserName(userFilePOList.get(0).getUserName());
                } else {
                    UserinfoPO userinfoPO = userInfoMapper.selectById(filePO.getCreater());
                    if (userinfoPO != null) {
                        fileResultDTO.setUserName(userinfoPO.getNickName());
                    }
                }
                fileResultDTOList.add(fileResultDTO);
            }

            resultMap.put("files", fileResultDTOList);
        } else {
            resultMap.put("files", new ArrayList<>());
        }
        return resultMap;
    }
}
