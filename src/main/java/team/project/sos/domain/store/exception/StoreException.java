package team.project.sos.domain.store.exception;

import team.project.sos.common.exception.BaseException;
import team.project.sos.common.exception.ErrorCode;

public class StoreException extends BaseException {

    public StoreException(ErrorCode errorCode) {
        super(errorCode);
    }

}
