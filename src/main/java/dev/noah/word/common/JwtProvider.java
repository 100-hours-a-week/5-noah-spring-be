package dev.noah.word.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expiration-in-ms}")
    private int expirationInMs;

    public String extractJwt(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) {
                String jwt = cookie.getValue();
                return jwt == null || jwt.isEmpty() ? null : jwt;
            }
        }

        return null;
    }

    public String generateJwt(long memberId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationInMs);

        return Jwts.builder().subject(Long.toString(memberId)).issuedAt(now).expiration(expiryDate).signWith(getSigningKey()).compact();
    }

    public boolean validateJwt(String jwt) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(jwt);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public long getMemberIdFromJwt(String jwt) {
        return Long.parseLong(Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(jwt).getPayload().getSubject());
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
