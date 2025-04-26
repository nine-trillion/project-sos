package team.project.sos.domain.cart.exception;

import team.project.sos.common.exception.BaseException;
import team.project.sos.common.exception.ErrorCode;

public class CartException extends BaseException {

    public CartException(ErrorCode errorCode) {
        super(errorCode);
    }

}