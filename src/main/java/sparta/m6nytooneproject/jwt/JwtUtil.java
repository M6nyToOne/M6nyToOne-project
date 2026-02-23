package sparta.m6nytooneproject.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sparta.m6nytooneproject.user.entity.UserRole;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long EXPIRATION = 1000 * 60 * 60; // 1시간

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // UserRole 타입으로 받기
    public String createToken(Long id,String email, UserRole role) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", id)
                .claim("role", role.name()) // "ADMIN", "USER" 같은 깔끔한 문자열
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public Long getId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    public boolean isExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }
}