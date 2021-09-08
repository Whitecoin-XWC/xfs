package com.nft.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Description: nft
 * Created by moloq on 2021/9/8 15:41
 */
@Data
@Getter
@Setter
@TableName("nft_user_address")
public class UserAddressPO {
    private Integer id;
    private Integer userId;
    private String address;
    private String zipCode;
    private String phone;
    private String name;
    private Date createTime;
    private Date updateTime;
}
