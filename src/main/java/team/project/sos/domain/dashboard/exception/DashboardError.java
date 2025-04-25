package team.project.sos.domain.dashboard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.project.sos.common.excepion.ErrorCode;

@Getter
@AllArgsConstructor
public enum DashboardError implements ErrorCode {

    DATE_REQUIRED(HttpStatus.BAD_REQUEST, "DASHBOARD_ERROR_001", "날짜 또는 월은 필수입니다."),
    DATE_PARAM_CONFLICT(HttpStatus.BAD_REQUEST, "DASHBOARD_ERROR_001", "날짜 또는 월 중 하나만 입력 가능합니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "DASHBOARD_ERROR_002", "유효하지 않은 날짜 형식입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
