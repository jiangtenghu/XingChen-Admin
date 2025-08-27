package com.admin.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT工具类 - 公共模块
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private static String secretKey;
    private static Long expiration;

    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    public void setSecretKey(String secretKey) {
        JwtUtil.secretKey = secretKey;
    }

    @Value("${jwt.expiration:1800}")
    public void setExpiration(Long expiration) {
        JwtUtil.expiration = expiration;
    }

    /**
     * 获取密钥
     */
    private static SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * 生成token
     */
    public static String generateToken(String userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignKey())
                .compact();
    }

    /**
     * 验证token
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            logger.error("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从token中获取用户ID
     */
    public static String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            Object userIdObj = claims.get("userId");
            return userIdObj != null ? userIdObj.toString() : null;
        } catch (Exception e) {
            logger.error("获取用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从token中获取用户名
     */
    public static String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            // 优先从claims中获取username，如果没有则从subject获取
            Object usernameObj = claims.get("username");
            if (usernameObj != null) {
                return usernameObj.toString();
            }
            return claims.getSubject();
        } catch (Exception e) {
            logger.error("获取用户名失败: {}", e.getMessage());
            return null;
        }
    }
}
