package com.dtf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
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

    @Bean
    @Order
    public RouteLocator customizedRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                //指定路由规则
                .route(r->r.path("/java/**")
                        .and().method(HttpMethod.GET)
                        .and().header("name")
                        //指定使用的过滤器
                        .filters(f->f.stripPrefix(1)
                                .addResponseHeader("java-param","gateway-config")
                                .filter(timerFilter))
                        .uri("lb://FEIGN-CLIENT")
                )
                //也可以指定路由规则的id
                .route("secKillRoute",r->r.path("secKill/**")
                        //路由规则生效时间。当前时间+1min
                        .and().after(ZonedDateTime.now().plusMinutes(1L))
                        .uri("lb://FEIGN-CLIENT"))
                .build();
    }
}
