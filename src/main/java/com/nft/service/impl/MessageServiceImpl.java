package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nft.commons.vo.PageResultVO;
import com.nft.commons.vo.ResultVO;
import com.nft.dao.entity.MessageEntity;
import com.nft.dao.entity.UserFilePO;
import com.nft.dao.entity.UserinfoPO;
import com.nft.dao.mapper.MessageMapper;
import com.nft.dao.mapper.UserFileMapper;
import com.nft.dao.mapper.UserInfoMapper;
import com.nft.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Description: nft
 * Created by moloq on 2021/9/2 14:03
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UserFileMapper userFileMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public ResultVO addMessage(MessageEntity messageEntity) {
        messageEntity.setCreateTime(new Date());
        int insert = 0;
        try {
            insert = messageMapper.insert(messageEntity);
        } catch (Exception e) {
            log.error("add message has exception: {}", e.getMessage());
            return ResultVO.fail("add message has error");
        }
        if (insert > 0) {
            return ResultVO.successMsg("add message success");
        } else {
            return ResultVO.fail("add message fail");
        }
    }

    @Override
    public ResultVO queryMessage(String tokenId, int pageNo, int pageSize) {
        Page<MessageEntity> page = new Page<>(pageNo, pageSize);
        QueryWrapper<MessageEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token_id", tokenId);
        queryWrapper.orderByDesc("create_time");
        Page<MessageEntity> messageEntityPage = messageMapper.selectPage(page, queryWrapper);
        if (messageEntityPage == null || CollectionUtils.isEmpty(messageEntityPage.getRecords())) {
            log.info("not found message");
            return ResultVO.fail("not found message");
        }
        // get nft owner
        QueryWrapper<UserFilePO> queryUserFile = new QueryWrapper<>();
        queryUserFile.eq("file_id", tokenId);
        queryUserFile.eq("type", 1);
        UserFilePO userFilePO = userFileMapper.selectOne(queryUserFile);
        String userId = "";
        if (userFilePO == null) {
            queryUserFile = new QueryWrapper<>();
            queryUserFile.eq("file_id", tokenId);
            queryUserFile.eq("type", 0);
            userFilePO = userFileMapper.selectOne(queryUserFile);
            userId = userFilePO.getUserId();
        } else {
            userId = userFilePO.getUserId();
        }

        String finalUserId = userId;
        messageEntityPage.getRecords().forEach(messageEntity -> {
            /* 获取用户昵称 */
            UserinfoPO userinfoPO = userInfoMapper.selectById(messageEntity.getUserAddress());
            messageEntity.setNickName(userinfoPO == null ? "" : userinfoPO.getNickName());
            if (messageEntity.getUserAddress().equalsIgnoreCase(finalUserId)) {
                messageEntity.setOwner(Boolean.TRUE);
            }
        });
        PageResultVO resultVO = new PageResultVO();
        resultVO.setCount(messageEntityPage.getTotal());
        resultVO.setCurrentPage(messageEntityPage.getCurrent());
        resultVO.setPageSize(messageEntityPage.getSize());
        resultVO.setPageTotal(messageEntityPage.getPages());
        resultVO.setRecords(messageEntityPage.getRecords());
        return ResultVO.success(resultVO);
    }
}
