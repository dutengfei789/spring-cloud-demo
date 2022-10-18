package com.dtf.auth.service.impl;

import com.dtf.auth.service.AuthService;
import com.dtf.auth.service.pojo.Account;
import com.dtf.auth.service.pojo.AuthCode;
import com.dtf.auth.service.pojo.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author dtf
 * @desc 认证实现
 * @date 2022-10-17 11:51
 */
@RestController
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;

    private final RedisTemplate redisTemplate;

    private static final String USER_TOKEN = "user_token-";

    @Override
    public AuthResponse tokenize(String userId) {
        Account account = Account.builder()
                .userId(userId)
                .build();
        String token = jwtService.token(account);
        account.setToken(token);
        account.setRefreshToken(UUID.randomUUID().toString());
        redisTemplate.opsForValue().set(USER_TOKEN + userId, account);
        redisTemplate.opsForValue().set(account.getRefreshToken(), userId);

        return AuthResponse.builder()
                .account(account)
                .code(AuthCode.SUCCESS.getCode())
                .build();
    }

    @Override
    public AuthResponse verify(Account account) {
        //TODO 校验redis当前token是否生效
        boolean success = jwtService.verify(account.getToken(), account.getUserId());
        return AuthResponse.builder()
                //todo 可返回invalid_token之类信息
                .code(success ? AuthCode.SUCCESS.getCode() : AuthCode.USER_NOT_FOUND.getCode())
                .build();
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        String userId = (String) redisTemplate.opsForValue().get(refreshToken);
        if (StringUtils.isBlank(userId)) {
            return AuthResponse.builder()
                    .code(AuthCode.USER_NOT_FOUND.getCode())
                    .build();
        }
        redisTemplate.delete(refreshToken);
        return tokenize(userId);
    }

    @Override
    public AuthResponse delete(@RequestBody Account account) {
        AuthResponse resp = new AuthResponse();
        resp.setCode(AuthCode.SUCCESS.getCode());

        if (account.isSkipVerification()) {
            redisTemplate.delete(USER_TOKEN + account.getUserId());
        }else {
            AuthResponse token = verify(account);
            if (AuthCode.SUCCESS.getCode().equals(token.getCode())) {
                redisTemplate.delete(USER_TOKEN + account.getUserId());
                redisTemplate.delete(account.getRefreshToken());
            }else {
                resp.setCode(AuthCode.USER_NOT_FOUND.getCode());
            }
        }
        return resp;
    }
}
