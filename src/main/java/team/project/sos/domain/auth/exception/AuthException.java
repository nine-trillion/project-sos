package team.project.sos.domain.auth.exception;

import team.project.sos.common.excepion.BaseException;
import team.project.sos.common.excepion.ErrorCode;

public class AuthException extends BaseException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
