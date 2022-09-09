package com.dtf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author dtf
 * @desc 请求耗时统计过滤器
 * <p>
 *     <li>全局过滤器使用GlobalFilter。注入spring即可
 *     <li>
 *         局部过滤器使用GatewayFilter.注入Spring且配置到指定路由规则中
 *     </li>
 * </p>
 * @date 2022-09-04 19:20
 */
@Slf4j
@Component
public class TimerFilter implements GatewayFilter, Ordered {


    /**
     * filter过滤器。在zuul中，pre，after执行
     * @param exchange the current server exchange
     * @param chain provides a way to delegate to the next filter
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        StopWatch timer = new StopWatch();
        timer.start(exchange.getRequest().getURI().getRawPath());
        //可以放参数
        exchange.getAttributes().put("requestTimeBegin", System.currentTimeMillis());
        //响应式编程
        return chain.filter(exchange).then(
                Mono.fromRunnable(()->{
                    timer.stop();
                    log.info(timer.prettyPrint());

                })
        );
    }

    /**
     * 过滤器执行顺序。数据越小，越先执行。对于after内容来说，越靠后执行
     *
     * @return 执行顺序
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
