package com.dtf.order.stream;

import com.dtf.enums.OrderStatusEnum;
import com.dtf.order.mapper.OrderStatusMapper;
import com.dtf.order.pojo.OrderStatus;
import com.dtf.order.pojo.bo.OrderStatusCheckBO;
import com.dtf.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author dtf
 * @desc 订单检查消费者
 * @date 2022-10-24 17:56
 */
@Slf4j
@EnableBinding(value = {
        CheckOrderTopic.class
})
@RequiredArgsConstructor
public class CheckOrderConsumer {

    private final OrderStatusMapper orderStatusMapper;

    public void consumerOrderStatusMessage(OrderStatusCheckBO checkBO) {
        String orderId = checkBO.getOrderId();
        log.info("received order check request,order={}", orderId);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(orderStatus);

        if (CollectionUtils.isEmpty(list)) {
            log.info("order paid or closed,orderId={}", orderId);
            return;
        }

        Date createdTime = list.get(0).getCreatedTime();
        int days = DateUtil.daysBetween(createdTime, new Date());
        if (days > 1) {
            OrderStatus update = new OrderStatus();
            update.setOrderId(orderId);
            update.setOrderStatus(OrderStatusEnum.CLOSE.type);
            update.setCloseTime(new Date());
            int count = orderStatusMapper.updateByPrimaryKey(update);
            log.info("closed order,orderId={},count={}", orderId, count);
        }
    }
}
