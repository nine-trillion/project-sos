package team.project.sos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.project.sos.domain.menu.exception.MenuException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 사용자 정의 예외 처리
     *
     * @param e BaseException
     * @return 예외 응답
     */
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ExceptionResponse> handleCustomException(BaseException e) {
        return ExceptionResponse.dtoResponseEntity(e.getErrorCode());
    }

    /**
     * 유효성 검사 예외 처리
     *
     * @param e MethodArgumentNotValidException
     * @return 예외 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String code = "VALIDATION_FAILED";
        String message = e.getBindingResult().getFieldError().getDefaultMessage();

        return ResponseEntity.status(status.value())
                .body(ExceptionResponse.builder()
                        .status(status.value())
                        .code(code)
                        .message(message)
                        .build());
    }
    /**
     * 메뉴 관련 예외 처리
     */
    @ExceptionHandler(MenuException.class)
    protected ResponseEntity<ExceptionResponse> handleMenuException(MenuException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .code("MENU_ERROR")
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDenied(AccessDeniedException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status.value())
                .body(ExceptionResponse.builder()
                        .status(status.value())
                        .code("FORBIDDEN_ACCESS")
                        .message("접근 권한이 없습니다.")
                        .build());
    }

}

