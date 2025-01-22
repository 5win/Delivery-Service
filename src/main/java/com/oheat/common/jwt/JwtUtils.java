package com.oheat.common.jwt;

import com.oheat.user.constant.Role;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.exception.InvalidUserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {

    private static final SecretKey key = SIG.HS256.key().build();
    private static final long expirationMs = 86400000;

    public static String generateJwtToken(UserJpaEntity user) {
        Date now = new Date();
        return Jwts.builder()
            .subject(user.getUsername())
            .claim("role", user.getRole())
            .issuedAt(now)
            .expiration(new Date(now.getTime() + expirationMs))
            .signWith(key)
            .compact();
    }

    public static String getUsername(String token) {
        return parseJwtToken(token).getPayload().getSubject();
    }

    public static Role getUserRole(String token) {
        return Role.valueOf((String) parseJwtToken(token).getPayload().get("role"));
    }

    public static boolean validateJwtToken(String token) {
        return parseJwtToken(token) != null;
    }

    private static Jws<Claims> parseJwtToken(String token) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        } catch (JwtException e) {
            log.error(e.getMessage());
            throw new InvalidUserException();
        }
    }
}
