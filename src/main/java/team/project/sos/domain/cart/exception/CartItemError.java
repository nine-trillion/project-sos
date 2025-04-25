package team.project.sos.domain.cart.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.project.sos.common.excepion.ErrorCode;

@Getter
@AllArgsConstructor
public enum CartItemError implements ErrorCode {

    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_ITEM_ERROR_001", "항목이 존재하지 않습니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "CART_ITEM_ERROR_002", "권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
