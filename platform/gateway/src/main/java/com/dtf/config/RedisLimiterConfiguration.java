package com.dtf.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * @author dtf
 * @desc 限流
 * @date 2022-10-18 18:16
 */
@Configuration
public class RedisLimiterConfiguration {


    @Bean
    @Primary
    public KeyResolver remoteAddrKeyResolver(){
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostName());
    }

    @Bean("redisLimiterUser")
    @Primary
    public RedisRateLimiter redisLimiterUser(){
        return new RedisRateLimiter(10, 20);
    }

    @Bean("redisLimiterItem")
    public RedisRateLimiter redisLimiterItem(){
        return new RedisRateLimiter(20, 50);
    }
}
