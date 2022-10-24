package com.dtf.user.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author dtf
 * @desc 强制登出消息主题
 * @date 2022-10-24 11:25
 */
public interface ForceLogoutTopic {

    String INPUT = "force-logout-consumer";

    String OUTPUT = "force-logout-producer";

    @Input(INPUT)
    SubscribableChannel input();

    @Output(OUTPUT)
    MessageChannel output();
}
