package com.shuting.flowEdaProgram.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {
    private static final String SECRET =
            "d11d47d81109f095dc88c519a2d838ba5744a329d7dbb337c6bf6adf818b1252";

    private static SecretKey hexStringToSecretKey() {
        // 将十六进制字符串转换为字节数组
        byte[] keyBytes = new byte[SECRET.length() / 2];
        for (int i = 0; i < SECRET.length(); i += 2) {
            keyBytes[i / 2] =
                    (byte)
                            ((Character.digit(SECRET.charAt(i), 16) << 4)
                                    + Character.digit(SECRET.charAt(i + 1), 16));
        }
        // 使用字节数组创建SecretKeySpec对象
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public static String generateToken(String subject, long expire) {
        String token =
                Jwts.builder()
                        .subject(subject)
                        .issuer("shuting")
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + expire))
                        .signWith(hexStringToSecretKey())
                        .compact();
        return token;
    }

    public static Claims parseToken(String token) {
        Jws<Claims> claims =
                Jwts.parser().verifyWith(hexStringToSecretKey()).build().parseSignedClaims(token);
        return claims.getPayload();
    }
}
