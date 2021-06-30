package com.nft.service;

import com.nft.dao.entity.UserinfoPO;

public interface UserInfoService {

    int updateUserInfo(UserinfoPO userinfoPO);

    UserinfoPO getUserInfo(String id);
}
