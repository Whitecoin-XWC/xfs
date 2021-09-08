package com.nft.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nft.dao.entity.OrderPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: nft
 * Created by moloq on 2021/9/8 15:49
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderPO> {
}
