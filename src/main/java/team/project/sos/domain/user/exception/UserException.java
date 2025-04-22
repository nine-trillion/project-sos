package team.project.sos.domain.user.exception;

import team.project.sos.common.excepion.BaseException;
import team.project.sos.common.excepion.ErrorCode;

public class UserException extends BaseException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
