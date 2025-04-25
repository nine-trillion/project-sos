package team.project.sos.common.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import team.project.sos.common.excepion.BaseException;
import team.project.sos.domain.auth.exception.AuthError;

import java.io.IOException;

/**
 * 인증되지 않은 사용자가 인증이 필요한 엔드포인트에 접근할 경우 발생하는 예외를 처리
 * 401 Unauthorized 예외
 * (@link HandlerExceptionResolver)를 통해 전역 예외 처리기로 위임해
 * 일관된 에러 응답 포멧 제공
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    private final HandlerExceptionResolver resolver;

    /**
     * @param resolver (@link HandlerExceptionResolver)로 예외 위임
     */
    public CustomAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * 인증되지 않은 요청이 발생했을 때 호출됩니다.
     * {@code AuthError.TOKEN_NOT_FOUND} 예외를 던져 전역 예외 핸들러로 위임합니다.
     *
     * @param request       HTTP 요청
     * @param response      HTTP 응답
     * @param authException 인증 예외
     * @throws IOException      입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void commence(@NonNull HttpServletRequest request,
                         @NonNull HttpServletResponse response,
                         @NonNull AuthenticationException authException) throws IOException, ServletException {
        resolver.resolveException(request, response, null, new BaseException(AuthError.TOKEN_NOT_FOUND));

    }
}