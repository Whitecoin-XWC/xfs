package com.nft.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nft.controller.vo.*;
import com.nft.dao.entity.FileLogPO;
import com.nft.dao.entity.FilePO;
import com.nft.dao.entity.NoticeEntity;
import com.nft.service.dto.FileResultDTO;
import com.nft.service.dto.NoticeResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface NFTService {

    int follow(FollowVO followVO);

    int delFollow(FollowVO followVO);

    int upload(FilePO filePO);

    int pub(PubVO pubVO);

    int pay(PubVO pubVO);

    int fileUserChange(FileUserChangeVO fileUserChangeVO);

    FilePO getFileDetail(FilePO filePO);

    IPage<FileLogPO> getFileLog(FileLogVO fileLogVO);

    IPage<FileResultDTO> selectFiles(FileVO fileVO);

    Map<String,Object> search(String keyWord);

}
