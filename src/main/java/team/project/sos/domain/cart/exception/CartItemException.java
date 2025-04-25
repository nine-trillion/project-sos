package team.project.sos.domain.cart.exception;

import team.project.sos.common.excepion.BaseException;
import team.project.sos.common.excepion.ErrorCode;

public class CartItemException extends BaseException {

    public CartItemException(ErrorCode errorCode) {
        super(errorCode);
    }

}