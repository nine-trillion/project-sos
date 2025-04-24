package team.project.sos.domain.order.exception;

import team.project.sos.common.excepion.BaseException;
import team.project.sos.common.excepion.ErrorCode;

public class OrderException extends BaseException {

    public OrderException(ErrorCode errorCode) {
        super(errorCode);
    }

}