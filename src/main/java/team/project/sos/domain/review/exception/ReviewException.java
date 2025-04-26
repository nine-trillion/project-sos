package team.project.sos.domain.review.exception;

import team.project.sos.common.exception.BaseException;
import team.project.sos.common.exception.ErrorCode;

public class ReviewException extends BaseException {

    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }

}