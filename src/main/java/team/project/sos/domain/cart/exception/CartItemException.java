package team.project.sos.domain.cart.exception;

import team.project.sos.common.exception.BaseException;
import team.project.sos.common.exception.ErrorCode;

public class CartItemException extends BaseException {

    public CartItemException(ErrorCode errorCode) {
        super(errorCode);
    }

}