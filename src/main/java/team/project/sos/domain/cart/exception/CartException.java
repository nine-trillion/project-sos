package team.project.sos.domain.cart.exception;

import team.project.sos.common.excepion.BaseException;
import team.project.sos.common.excepion.ErrorCode;

public class CartException extends BaseException {

    public CartException(ErrorCode errorCode) {
        super(errorCode);
    }

}