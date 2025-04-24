package team.project.sos.domain.store.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.project.sos.common.excepion.ErrorCode;

@Getter
@AllArgsConstructor
public enum StoreError implements ErrorCode {

    NOT_FOUND_STORE(HttpStatus.BAD_REQUEST, "STORE_ERROR_001", "가게가 존재하지 않습니다."),
    UNAUTHORIZED_STORE_OWNER(HttpStatus.FORBIDDEN, "STORE_ERROR_002", "접근 권한이 없는 사용자입니다."),
    INVALID_OPEN_TIME_AFTER_CLOSE(HttpStatus.BAD_REQUEST, "STORE_ERROR_003", "오픈 시간은 마감 시간보다 늦을 수 없습니다."),
    EXCEEDED_STORE_LIMIT(HttpStatus.BAD_REQUEST, "STORE_ERROR_00r", "가게는 최대 3개까지만 운영할 수 있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
