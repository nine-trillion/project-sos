package team.project.sos.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.project.sos.common.excepion.ErrorCode;

@Getter
@AllArgsConstructor
public enum UserError implements ErrorCode {

    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "USER_ERROR_001", "이미 존재하는 이메일입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
