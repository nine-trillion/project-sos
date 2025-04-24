package team.project.sos.domain.review.exception;

import team.project.sos.common.excepion.BaseException;
import team.project.sos.common.excepion.ErrorCode;

public class ReviewException extends BaseException {

    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }

}