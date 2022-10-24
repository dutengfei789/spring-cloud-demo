package com.dtf.user.stream;

import com.dtf.auth.service.AuthService;
import com.dtf.auth.service.pojo.Account;
import com.dtf.auth.service.pojo.AuthCode;
import com.dtf.auth.service.pojo.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;

/**
 * @author dtf
 * @desc 用户消息消费
 * @date 2022-10-24 11:34
 */
@EnableBinding(value = {
        ForceLogoutTopic.class
})
@Slf4j
@RequiredArgsConstructor
public class UserMessageConsumer {

    private final AuthService authService;

    @StreamListener(ForceLogoutTopic.INPUT)
    public void logoutTopicMessage(String payload) {
        log.info("force logout user,uid={}", payload);

        Account account = Account.builder()
                .userId(payload)
                .skipVerification(true)
                .build();

        AuthResponse resp = authService.delete(account);
        if (!AuthCode.SUCCESS.getCode().equals(resp.getCode())) {
            log.error("Error occurred when deleting user session,uid={}", payload);
            throw new RuntimeException("Cannot delete user session");
        }

    }


    @ServiceActivator(inputChannel = "force-logout-topic.force-logout-group.errors")
    public void fallback(Message message) {

        log.info("Force logout failed");
        //执行强制登出

    }


}
