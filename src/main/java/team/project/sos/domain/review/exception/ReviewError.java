package team.project.sos.domain.review.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.project.sos.common.exception.ErrorCode;


@Getter
@AllArgsConstructor
public enum ReviewError implements ErrorCode {

    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_001", "리뷰를 찾을 수 없습니다."),
    UNAUTHORIZED_REVIEW_ACCESS(HttpStatus.FORBIDDEN, "REVIEW_002", "해당 리뷰에 대한 접근 권한이 없습니다."),
    ORDER_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "REVIEW_006", "배송이 완료된 주문만 리뷰를 작성할 수 있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}