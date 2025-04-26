package team.project.sos.common.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import team.project.sos.domain.user.enums.UserRole;

import java.io.IOException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String url = httpServletRequest.getRequestURI();
        log.info("JwtAuthFilter url{}",url);

        String method = httpServletRequest.getMethod();
        // "/api/auth"로 시작하는 요청은 jwt 검증을 건너뛰고 필터 체인 실행
        if (url.startsWith("/api/auth") && !"DELETE".equals(method)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Authorization 헤더에서 Bearer 토큰 추출
        String bearerJwt = httpServletRequest.getHeader("Authorization");
        log.info("Authorization Header: {}",bearerJwt);

        if (bearerJwt == null) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 존재하지 않습니다.");
            return;
        }

        // Bearer 접두사를 제거한 JWT 추출
        String jwt = jwtProvider.substringToken(bearerJwt).trim();
        log.info("JWT: '{}'", jwt);

        try {
            Claims claims = jwtProvider.extractClaims(jwt);
            if (claims == null) {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                return;
            }

            String userId = claims.getSubject();
//            String email = claims.get("email",String.class);
            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));
            log.info("claims: {}",claims);
            // 인증 객체 생성 @
            httpServletRequest.setAttribute("userId", Long.parseLong(userId));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + userRole))
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            log.info("다음 필터 호출");
            filterChain.doFilter(httpServletRequest, httpServletResponse);

        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (Exception e) {
            log.error("Invalid JWT token, 유효하지 않는 JWT 토큰 입니다.", e);
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않는 JWT 토큰입니다.");
        }
    }


}
