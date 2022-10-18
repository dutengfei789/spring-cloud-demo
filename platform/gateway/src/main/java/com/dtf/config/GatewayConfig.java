package com.dtf.config;

import com.dtf.filter.AuthFiler;
import com.dtf.filter.TimerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;

import java.time.ZonedDateTime;

/**
 * @author dtf
 * @desc 路由配置。自定义一个路由规则
 * @date 2022-09-04 19:29
 */
@Configuration
public class GatewayConfig {

    @Autowired
    private TimerFilter timerFilter;

    @Autowired
    private KeyResolver keyResolver;

    @Autowired
    @Qualifier("redisLimiterUser")
    private RateLimiter rateLimiterUser;
    @Bean
    @Order
    public RouteLocator customizedRoutes(RouteLocatorBuilder builder, AuthFiler authFiler) {
        return builder.routes()
                //配置权限认证
                //auth服务在网关有多种做法。
                // 1)最常用是：网关本地校验jwt
                // 2) 把AuthFilter设置全局，然后在AuthFilter中配置过滤的url。（也可从config中拉取）
                // 3） 也可以采用intercept
                // 4) 硬编码。示例这种不够灵活。
                //一个ur可匹配多个路由，注册先来后到

                .route(r -> r.path("/address/*", "/userInfo/**/cent/**", "userInfo/**", "center/**")
                        .filters(f -> f.filter(authFiler))
                        .uri("lb://FOODIE-USER-SERVICE"))
                //指定路由规则
                .route(r -> r.path("/java/**")
                        .and().method(HttpMethod.GET)
                        .and().header("name")
                        //指定使用的过滤器
                        .filters(f -> f.stripPrefix(1)
                                .addResponseHeader("java-param", "gateway-config")
                                .filter(timerFilter))
                        .uri("lb://FEIGN-CLIENT")
                )
                .route(r -> r.path("/address/**", "/passport/**", "/userInfo/**", "center/**")
                        .filters(f -> f.requestRateLimiter(config -> {
                            config.setKeyResolver(keyResolver);
                            config.setRateLimiter(rateLimiterUser);}))
                        .uri("lb://FOODIE-USER-SERVICE"))
                //也可以指定路由规则的id
                .route("secKillRoute", r -> r.path("secKill/**")
                        //路由规则生效时间。当前时间+1min
                        .and().after(ZonedDateTime.now().plusMinutes(1L))
                        .uri("lb://FEIGN-CLIENT"))
                .build();
    }

}
