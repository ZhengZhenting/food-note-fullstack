package cn.kmbeast.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * jwt token 工具类
 */
public class JwtUtil {

    /**
     * 密钥（必须 >= 32 字节）
     */
    private static final String privateKey =
            "d8c986df851242b5906feeea9b3acf86d8c986df";

    /**
     * 生成安全 key 对象
     */
    private static final SecretKey key =
            Keys.hmacShaKeyFor(privateKey.getBytes(StandardCharsets.UTF_8));

    /**
     * 有效期一周
     */
    private static final long time = 1000L * 60 * 60 * 24 * 7;

    /**
     * 生成 token
     */
    public static String toToken(Integer id, Integer role) {

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claim("id", id)
                .claim("role", role)
                .setSubject("用户认证")
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setId(UUID.randomUUID().toString())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 token
     */
    public static Claims fromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }
}