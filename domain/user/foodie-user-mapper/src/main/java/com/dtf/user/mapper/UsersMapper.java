package com.dtf.user.mapper;


import com.dtf.my.mapper.MyMapper;
import com.dtf.user.pojo.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper extends MyMapper<Users> {
}