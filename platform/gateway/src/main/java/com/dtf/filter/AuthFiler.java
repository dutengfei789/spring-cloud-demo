package com.dtf.filter;

import com.dtf.auth.service.AuthService;
import com.dtf.auth.service.pojo.Account;
import com.dtf.auth.service.pojo.AuthCode;
import com.dtf.auth.service.pojo.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author dtf
 * @desc auth认证
 * @date 2022-10-18 16:17
 */
@Component("authFilter")
@Slf4j
@RequiredArgsConstructor
public class AuthFiler implements GatewayFilter, Ordered {

    private static final String AUTH = "Authorization";

    private static final String USERNAME = "fooide-user-name";

    private static final String USERID = "foodie-user-id";

    private  final AuthService authService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("auth start");
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders header = request.getHeaders();
        String userId = header.getFirst(USERID);
        String token = header.getFirst(AUTH);

        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isBlank(token)) {
            log.error("token not found");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        if (StringUtils.isBlank(userId)) {
            log.error("userId not found");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        Account account = Account.builder().token(token).userId(userId).build();
        AuthResponse authResp = authService.verify(account);
        if (!Objects.equals(authResp.getCode(), AuthCode.SUCCESS.getCode())) {
            log.error("invalid token");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        //TODO 将用户信息存放在Header中传递下游业务
        ServerHttpRequest buildRequest = request.mutate()
                .header(USERID, userId)
                .header(AUTH, token)
                .build();
        response.getHeaders().add(USERID,userId);
        response.getHeaders().add(AUTH, token);

        return chain.filter(exchange.mutate()
                .request(buildRequest)
                .response(response)
                .build());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
