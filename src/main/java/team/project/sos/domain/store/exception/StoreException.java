package team.project.sos.domain.store.exception;

import team.project.sos.common.excepion.BaseException;
import team.project.sos.common.excepion.ErrorCode;

public class StoreException extends BaseException {

    public StoreException(ErrorCode errorCode) {
        super(errorCode);
    }

}
