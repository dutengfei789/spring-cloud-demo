package com.dtf.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dtf
 * @desc api层
 * @date 2022-06-08 10:30
 */
@RestController
@Slf4j
public class Controller {

    @Value("${server.port}")
    private String port;

    @GetMapping("/hi")
    public String hello() {
        return "hello, eureka-client, port: " + port;
    }

    @PostMapping("/hi")
    public Friend sayHello(@RequestBody Friend friend) {
        friend.setPort(port);
        return friend;
    }
}
