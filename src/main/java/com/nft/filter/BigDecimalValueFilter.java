package com.nft.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description: nft
 * Created by moloq on 2021/7/27 15:14
 */
public class BigDecimalValueFilter implements ValueFilter {
    @Override
    public Object process(Object o, String s, Object value) {
        if (value != null && value instanceof BigDecimal) {
            return String.format("%.6f", ((BigDecimal) value).doubleValue());
        }

        if (value != null && value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(value) + "(UTC)";
        }
        return value;
    }
}
