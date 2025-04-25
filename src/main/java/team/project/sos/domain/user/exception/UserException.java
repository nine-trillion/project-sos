package team.project.sos.domain.user.exception;

import team.project.sos.common.exception.BaseException;
import team.project.sos.common.exception.ErrorCode;

public class UserException extends BaseException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
