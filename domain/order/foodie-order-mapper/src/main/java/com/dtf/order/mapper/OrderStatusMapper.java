package com.dtf.order.mapper;


import com.dtf.my.mapper.MyMapper;
import com.dtf.order.pojo.OrderStatus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderStatusMapper extends MyMapper<OrderStatus> {
}