package com.nft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nft.dao.entity.UserAddressPO;
import com.nft.dao.entity.UserFilePO;
import com.nft.dao.entity.UserinfoPO;
import com.nft.dao.mapper.UserAddressMapper;
import com.nft.dao.mapper.UserFileMapper;
import com.nft.dao.mapper.UserInfoMapper;
import com.nft.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserFileMapper userFileMapper;

    @Resource
    private UserAddressMapper userAddressMapper;

    /**
     * update user info
     *
     * @param userinfoPO
     * @return
     */
    @Override
    public int updateUserInfo(UserinfoPO userinfoPO) {
        UserinfoPO userinfoPO1 = userInfoMapper.selectById(userinfoPO.getId());
        if (userinfoPO1 == null) {
            userinfoPO.setCreateTime(new Date());
            userInfoMapper.insert(userinfoPO);


        } else {
            userinfoPO1.setNickName(userinfoPO.getNickName());
            userinfoPO1.setIntroduction(userinfoPO.getIntroduction());
            userInfoMapper.updateById(userinfoPO1);
        }

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", userinfoPO.getId());

        UserFilePO userFilePO = new UserFilePO();
        userFilePO.setUserName(userinfoPO.getNickName());
        userFileMapper.update(userFilePO, updateWrapper);
        return 1;
    }

    /**
     * search user info
     *
     * @param id
     * @return
     */
    @Override
    public UserinfoPO getUserInfo(String id) {
        return userInfoMapper.selectById(id);
    }

    @Override
    public void addAddress(UserAddressPO addressPO) {
        addressPO.setCreateTime(new Date());
        userAddressMapper.insert(addressPO);
    }

    @Override
    public void updateAddress(UserAddressPO addressPO) {
        addressPO.setUpdateTime(new Date());
        userAddressMapper.updateById(addressPO);
    }

    @Override
    public UserAddressPO queryAddress(UserAddressPO addressPO) {
        QueryWrapper<UserAddressPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", addressPO.getUserId());
        return userAddressMapper.selectOne(queryWrapper);
    }
}
