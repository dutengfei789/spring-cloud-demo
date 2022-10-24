package com.dtf.order.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author dtf
 * @desc 订单状态检查
 * @date 2022-10-24 17:52
 */
public interface CheckOrderTopic {

    String INPUT = "orderstatus-consumer";

    String OUTPUT = "orderstatus-producer";

    @Input(INPUT)
    SubscribableChannel input();

    @Output(OUTPUT)
    MessageChannel output();
}
