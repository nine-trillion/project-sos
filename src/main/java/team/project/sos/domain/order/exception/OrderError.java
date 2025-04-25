package team.project.sos.domain.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.project.sos.common.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum OrderError implements ErrorCode {

    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "ORDER_ERROR_001", "재고가 부족합니다."),
    ALREADY_COOKING(HttpStatus.BAD_REQUEST, "ORDER_ERROR_002", "조리가 시작되어 취소가 불가능합니다."),
    NO_SUCH_USER(HttpStatus.NOT_FOUND, "ORDER_ERROR_003", "해당하는 사용자가 없습니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "ORDER_ERROR_004", "수량은 1 이상이어야 합니다."),
    NO_SUCH_ORDER(HttpStatus.NOT_FOUND, "ORDER_ERROR_005", "해당하는 주문이 없습니다."),
    ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "ORDER_ERROR_006", "이미 취소된 주문입니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "ORDER_ERROR_006", "권한이 없습니다."),
    NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "ORDER_ERROR_007", "로그인이 필요합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}