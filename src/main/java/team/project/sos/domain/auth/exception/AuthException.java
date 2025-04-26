package team.project.sos.domain.auth.exception;

import team.project.sos.common.exception.BaseException;
import team.project.sos.common.exception.ErrorCode;

public class AuthException extends BaseException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
