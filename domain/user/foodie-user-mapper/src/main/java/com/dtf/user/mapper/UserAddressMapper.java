package com.dtf.user.mapper;

import com.dtf.my.mapper.MyMapper;
import com.dtf.user.pojo.UserAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author dtf
 * @desc
 * @date 2022-07-18 11:46
 */
@Mapper
public interface UserAddressMapper extends MyMapper<UserAddress> {
}
