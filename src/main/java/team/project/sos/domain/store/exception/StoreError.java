package team.project.sos.domain.store.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.project.sos.common.excepion.ErrorCode;

@Getter
@AllArgsConstructor
public enum StoreError implements ErrorCode {

    ERROR_CODE_NAME(HttpStatus.BAD_REQUEST, "STORE_ERROR_001", "가게 오류 발생 예외 메시지");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
