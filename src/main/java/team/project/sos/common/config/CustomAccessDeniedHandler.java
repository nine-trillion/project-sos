package team.project.sos.common.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import team.project.sos.common.exception.BaseException;
import team.project.sos.domain.auth.exception.AuthError;

import java.io.IOException;

/**
 * 인증 되었지만 , 권한이 없는 사용자가 권한이 필요한 엔드포인트로 접근하려 할 때 발생하는 Forbidden 예외 처리 핸들러
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver resolver;

    /**
     * @param resolver 예외를 전역 예외 처리기로 위임하기 위한 인스턴스
     */
    public CustomAccessDeniedHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
    ) {
        this.resolver = resolver;
    }

    /**
     * 권한이 없는 사용자가 보호된 리소스에 접근하려 할 때 호출됩니다.
     * 예외를 {@link HandlerExceptionResolver}에 위임하여 공통 예외 응답 형식을 유지합니다.
     *
     * @param request               HTTP 요청
     * @param response              HTTP 응답
     * @param accessDeniedException 접근 거부 예외
     * @throws IOException      입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        resolver.resolveException(request, response, null, new BaseException(AuthError.FORBIDDEN_ACCESS));
    }
}
