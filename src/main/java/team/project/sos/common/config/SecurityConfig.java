package team.project.sos.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import team.project.sos.common.security.SecurityConstants;

/**
 * Spring Security 설정을 구성하는 클래스
 * Jwt 기반 인증 방식을 사용하며 세션을 비활성화
 * 사용자 권한 기반으로 접근 제어를 설정했습니다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Jwt 토큰을 생성, 검증하는 컴포넌트
     **/
    private final JwtProvider jwtProvider;

    /**
     * 권한이 없는 사용자가 요청할 경우, 처리할 AccessDeniedHandler 구현체
     */
    private final AccessDeniedHandler accessDeniedHandler;

    /**
     * Jwt 인증 필터 빈 등록
     *
     * @return JwtAuthFilter 객체
     */
    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtProvider);
    }

    /**
     * Spring Security 필터 체인을 설정
     * - CSRF 비활성화
     * - 인증 없이 접근 가능한 URL 지정 (WHITE_LIST)
     * - 역할(Role) 기반의 URL 접근 제어 설정  ( ADMIN > USER, OWNER )
     * - 세션 미사용 (Stateless)
     * - JWT 필터 등록
     * - 인증 및 인가 실패 시 커스텀 핸들러 등록
     *
     * @param http                     HttpSecurity 객체
     * @param jwtAuthFilter            JWT 인증 필터
     * @param authenticationEntryPoint 인증되지 않은 요청 처리 핸들러
     * @return SecurityFilterChain
     * @throws Exception 예외 발생 시
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter, AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        http

                .csrf(AbstractHttpConfigurer::disable)

                // 요청에 대한 인증, 인가 설정
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 접근 가능한 URL 설정 (화이트리스트), 누구나 접근 가능
                        .requestMatchers(SecurityConstants.WHITE_LIST).permitAll()
                        // 관리자 권한이 필요한 경로
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN", "OWNER")
                        // 그 외 모든 요청은 인증된 사용자만 접근 허용
                        .anyRequest().authenticated()
                )

                // 세션을 사용하지 않음 (Jwt 기반 인증은 무상태)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // JWT 인증 필터 등록 (Spring Security 기본 로그인 필터 전 등록)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 인증 실패, 권한 실패 시 핸들러 등록
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }
}
