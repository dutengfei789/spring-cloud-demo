package com.dtf.my.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author dtf
 * @desc 通用mapper
 * @date 2022-06-14 17:28
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
