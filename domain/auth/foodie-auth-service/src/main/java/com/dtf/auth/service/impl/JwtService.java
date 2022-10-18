package com.dtf.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.dtf.auth.service.pojo.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author dtf
 * @desc jwt功能封装
 * @date 2022-10-17 11:52
 */
@Slf4j
@Service
public class JwtService {

    private static final String KEY = "changeIt";

    private static final String ISSUER = "dtf";

    private static final long TOKEN_EXPIRE_TIME = 24 * 3600 * 1000;

    private static final String USER_ID = "userId";

    public String token(Account account) {
        Date now = new Date();

        //这里提供多种加密算法，生产可用更高级加密算法 。
        //【最常用】采用非对称加密，auth-service只负责生成jwt-token
        //由各个业务方（或网关层）在自己代码里用key校验token正确性
        //代码：范围规范，并节约一次http 回调

        Algorithm algorithm = Algorithm.HMAC256(KEY);
        String token = JWT.create().withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXPIRE_TIME))
                //role信息也可以放入claim中
                .withClaim(USER_ID, account.getUserId())
                .sign(algorithm);
        log.info("jwt generate，userId={}", account.getUserId());
        return token;
    }

    /**
     * 校验token
     * @param token
     * @param userId
     * @return 校验是否通过
     */
    public boolean verify(String token, String userId) {

        log.info("verify jwt token-userId:{}",userId);
        Algorithm algorithm = Algorithm.HMAC256(KEY);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .withClaim(USER_ID, userId)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("auth failed,userId:{}", userId, e);
        }
        return false;

    }
}
