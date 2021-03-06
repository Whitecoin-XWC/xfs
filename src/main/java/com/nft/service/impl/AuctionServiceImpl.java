package com.nft.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.bean.AuctionStatus;
import com.nft.dao.entity.*;
import com.nft.dao.mapper.*;
import com.nft.service.AuctionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Description: nft
 * Created by moloq on 2021/6/28 11:35
 */
@Service
@Slf4j
public class AuctionServiceImpl extends ServiceImpl<AuctionMapper, AuctionEntity> implements AuctionService {

    @Resource
    private AuctionMapper auctionMapper;
    @Resource
    private FileMapper fileMapper;
    @Resource
    private UserFileMapper userFileMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    @Value("${tokenswap.api.url}")
    private String apiUrl;

    @Override
    public int insertAuction(AuctionEntity auctionEntity) {
        auctionEntity.setAuctionStatus(AuctionStatus.AUCTION_START.getStatuCode());
        auctionEntity.setCreateTime(new Date());
        int insert = auctionMapper.insert(auctionEntity);
        if (insert > 0) {
            //update nft status to auctioning
            FilePO filePO = new FilePO();
            filePO.setId(auctionEntity.getFileTokenId());
            filePO.setFileStatus(5);
            fileMapper.updateById(filePO);
        }
        return insert;
    }

    @Override
    public int updateAuction(AuctionEntity update) {
        update.setUpdateTime(new Date());
        return auctionMapper.updateById(update);
    }

    @Override
    public AuctionEntity queryAuction(String fileTokenId) {
        QueryWrapper<AuctionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_token_id", fileTokenId);
        queryWrapper.orderByDesc("create_time");
        List<AuctionEntity> auctionEntities = auctionMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(auctionEntities)) {
            return new AuctionEntity();
        }
        AuctionEntity auctionEntity = auctionEntities.get(0);
        BigDecimal coinPrice = getCoinPrice(auctionEntity.getAuctionCoin());
        auctionEntity.setAuctionMinMarkupUsdt(auctionEntity.getAuctionMinMarkup().multiply(coinPrice).setScale(8, BigDecimal.ROUND_DOWN));
        auctionEntity.setAuctionRetainPriceUsdt(auctionEntity.getAuctionRetainPrice().multiply(coinPrice).setScale(8, BigDecimal.ROUND_DOWN));
        if (auctionEntity.getAuctionMaxPrice() != null) {
            auctionEntity.setAuctionMaxPriceUsdt(auctionEntity.getAuctionMaxPrice().multiply(coinPrice).setScale(8, BigDecimal.ROUND_DOWN));
        }
        String auctionMaxEr = auctionEntity.getAuctionMaxEr();
        UserinfoPO userinfoPO = userInfoMapper.selectById(auctionMaxEr);
        if (userinfoPO != null && StringUtils.isNotBlank(userinfoPO.getNickName())) {
            auctionEntity.setUserName(userinfoPO.getNickName());
        }
        /* Calculate the countdown time */
        if (auctionEntity.getAuctionStatus() > 0 && auctionEntity.getAuctionStartTime() != null) {
            Date auctionStartTime = auctionEntity.getAuctionStartTime();
            LocalDateTime localDateTime = auctionStartTime.toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();
            Duration between = Duration.between(LocalDateTime.now(), localDateTime.plusHours(24));
            long millis = between.toMillis();
            if (millis >= 0) {
                auctionEntity.setRemainingTime(between.toMillis());
            }
        }
        return auctionEntity;
    }

    @Override
    public int cancelAuction(String fileTokenId, long id) {
        FilePO filePO = new FilePO();
        filePO.setId(fileTokenId);
        filePO.setFileStatus(2);
        int update = fileMapper.updateById(filePO);
        auctionMapper.deleteById(id);
        return update;
    }

    @Override
    public String receive(String fileTokenId, String userAddress, long auctionId) {
        FilePO filePO = new FilePO();
        filePO.setId(fileTokenId);
        filePO.setFileStatus(2);
        int update = fileMapper.updateById(filePO);
        if (update <= 0) {
            return "????????????????????????";
        }

        /* update auction status to receive success */
        AuctionEntity entity = auctionMapper.selectById(auctionId);
        if (entity == null) {
            return "??????????????????";
        }
        entity.setAuctionStatus(AuctionStatus.RECEIVED.getStatuCode());
        int updateAuction = auctionMapper.updateById(entity);
        if (updateAuction <= 0) {
            return "????????????????????????";
        }


        /* ??????nft????????? */
        QueryWrapper<UserFilePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileTokenId);
        queryWrapper.eq("type", 1);
        UserFilePO userFilePO = userFileMapper.selectOne(queryWrapper);
        if (userFilePO == null) {
            /* update nft owner */
            userFilePO = new UserFilePO();
            userFilePO.setCreateTime(new Date());
            userFilePO.setFileId(fileTokenId);
            userFilePO.setUserId(userAddress);
            userFilePO.setType(1);
            userFileMapper.insert(userFilePO);
        } else {
            /* update nft owner */
            userFilePO.setUserId(userAddress);
            userFileMapper.updateById(userFilePO);
        }
        return "????????????";
    }


    @Override
    public BigDecimal getCoinPrice(String coin) {
        HttpGet httpGet = new HttpGet(apiUrl);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            log.info("get tokenswap query coin price,httpStatus: {}", response.getStatusLine());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                log.error("get tokenswap query coin price,bad httpStatus,httpStatus: {}", statusCode);
                return BigDecimal.ZERO;
            }

            String responseStr = EntityUtils.toString(responseEntity);
            if (StringUtils.isNotBlank(responseStr)) {
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                String code = jsonObject.getString("code");
                if ("0".equalsIgnoreCase(code)) {
                    String data = jsonObject.getString("data");
                    List<JSONObject> jsonObjects = JSONArray.parseArray(data, JSONObject.class);
                    for (JSONObject object : jsonObjects) {
                        if (coin.equalsIgnoreCase(object.getString("name"))) {
                            String price = object.getString("price");
                            return new BigDecimal(price);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("get tokenswap query coin price,fail,e: {}", e);
        }
        return BigDecimal.ZERO;
    }
}
