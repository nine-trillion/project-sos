package team.project.sos.common.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import team.project.sos.common.excepion.BaseException;
import team.project.sos.domain.auth.exception.AuthError;
import team.project.sos.domain.user.enums.UserRole;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * JWT를 생성, 검증하는 역할을 수행합니다.
 * 비밀 키는 환경변수 {@code JWT_SECRET_KEY}에서 주입되며, Base64로 디코딩되어 사용됩니다.
 * 토큰은 사용자 식별 정보(subject), 이메일, 역할(role), 생성일 및 만료일 정보를 포함합니다.
 */
@Slf4j(topic = "JwtUtil")
@Component
public class JwtProvider {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_EXPIRATION = 1000 * 60 * 60; // 1시간

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 객체 초기화 시 비밀키를 Base64로 디코딩하여 {@link Key} 객체를 생성합니다.
     */
    @PostConstruct
    public void init() {
        log.info(">>> JWT_SECRET_KEY: [{}]", secretKey);
        byte[] bytes = Base64.getDecoder().decode(secretKey.trim());
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * JWT 토큰을 생성합니다.
     *
     * @param userId   사용자 ID
     * @param email    사용자 이메일
     * @param userRole 사용자 역할
     * @return JWT 문자열
     */
    public String createToken(Long userId, String email, UserRole userRole) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .claim("userRole", userRole)
                .setExpiration(new Date(date.getTime() + TOKEN_EXPIRATION))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    /**
     * "Bearer " 접두사가 포함된 Authorization 헤더로부터 JWT만 추출합니다.
     *
     * @param tokenValue Authorization 헤더 값
     * @return JWT 문자열
     * @throws BaseException 토큰이 없거나 형식이 잘못된 경우
     */
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7).trim();
        }
        throw new BaseException(AuthError.TOKEN_NOT_FOUND);
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
