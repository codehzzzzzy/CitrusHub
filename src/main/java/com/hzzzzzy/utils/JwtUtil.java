package com.hzzzzzy.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtUtil {

    /**
     * JWT的唯一身份标识
     */
    public static final String JWT_ID = UUID.randomUUID().toString();

    /**
     * 秘钥
     */
    public static final String JWT_SECRET = "hzzzzzy";

    public static String createToken(String userId,String username) throws UnsupportedEncodingException {
        Map<String,Object> header = new HashMap<>();
        header.put("typ","JWT");
        String jwtToken = JWT.create()
                .withHeader(header)
                .withJWTId(JWT_ID)
                .withClaim("userId",userId)
                .withClaim("username",username)
                .sign(Algorithm.HMAC256(JWT_SECRET));

        return jwtToken;

    }
}
