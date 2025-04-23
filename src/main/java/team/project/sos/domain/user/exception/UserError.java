package team.project.sos.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.project.sos.common.excepion.ErrorCode;

@Getter
@AllArgsConstructor
public enum UserError implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_ERROR_001", "회원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER_ERROR_002", "이미 존재하는 이메일입니다."),
    USER_DEACTIVATED(HttpStatus.CONFLICT, "USER_ERROR_003", "해당 이메일은 탈퇴한 계정입니다. 재가입할 수 없습니다."),
    INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "USER_ERROR_004", "전화번호 형식이 올바르지 않습니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "USER_ERROR_005", "이메일 형식이 올바르지 않습니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST,"USER_ERROR_006","비밀번호는 최소 8자 이상, 대소문자/숫자/특수문자 각각 1자 이상 포함되어야합니다."),
    DUPLICATE_PHONE_NUMBER(HttpStatus.CONFLICT,"USER_ERROR_007","이미 존재하는 휴대폰 번호입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
