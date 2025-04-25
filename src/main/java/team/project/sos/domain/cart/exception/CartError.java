package team.project.sos.domain.cart.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.project.sos.common.excepion.ErrorCode;

@Getter
@AllArgsConstructor
public enum CartError implements ErrorCode {

    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "CART_ERROR_001", "재고가 부족합니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_ERROR_002", "장바구니가 존재하지 않습니다."),
    NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "CART_ERROR_003", "로그인이 필요합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}