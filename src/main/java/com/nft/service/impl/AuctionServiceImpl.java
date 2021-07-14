package com.nft.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nft.bean.AuctionStatus;
import com.nft.dao.entity.AuctionEntity;
import com.nft.dao.entity.FilePO;
import com.nft.dao.mapper.AuctionMapper;
import com.nft.dao.mapper.FileMapper;
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

    @Value("${tokenswap.api.url}")
    private String apiUrl;

    @Override
    public int insertAuction(AuctionEntity auctionEntity) {
        auctionEntity.setAuctionStatus(AuctionStatus.AUCTION_START.getStatuCode());
        auctionEntity.setCreateTime(new Date());
        int insert = auctionMapper.insert(auctionEntity);
        if (insert > 0) {
            //修改主流程状态为5--拍卖中
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
    public AuctionEntity queryAuction(AuctionEntity query) {
        QueryWrapper<AuctionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_token_id", query.getFileTokenId());
        queryWrapper.orderByDesc("create_time");
        List<AuctionEntity> auctionEntities = auctionMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(auctionEntities)) {
            return new AuctionEntity();
        }
        AuctionEntity auctionEntity = auctionEntities.get(0);
        BigDecimal coinPrice = getCoinPrice(auctionEntity.getAuctionCoin());
        auctionEntity.setAuctionMinMarkupUsdt(auctionEntity.getAuctionMinMarkup().multiply(coinPrice));
        auctionEntity.setAuctionRetainPriceUsdt(auctionEntity.getAuctionRetainPrice().multiply(coinPrice));
        return auctionEntity;
    }

    @Override
    public int cancelAuction(String fileTokenId) {
        FilePO filePO = new FilePO();
        filePO.setId(fileTokenId);
        filePO.setFileStatus(2);
        int update = fileMapper.updateById(filePO);
        return update;
    }


    private BigDecimal getCoinPrice(String coin) {
        HttpGet httpGet = new HttpGet(apiUrl);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            log.info("get tokenswap query coin price,httpStatus: {}", response.getStatusLine());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                log.error("get tokenswap query coin price,bad httpstatus,httpStatus: {}", statusCode);
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
