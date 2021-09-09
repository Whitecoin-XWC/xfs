package com.nft.service;

import com.nft.dao.entity.UserAddressPO;
import com.nft.dao.entity.UserinfoPO;

public interface UserInfoService {

    int updateUserInfo(UserinfoPO userinfoPO);

    UserinfoPO getUserInfo(String id);

    void addAddress(UserAddressPO addressPO);

    void updateAddress(UserAddressPO addressPO);

    UserAddressPO queryAddress(UserAddressPO addressPO);
}
