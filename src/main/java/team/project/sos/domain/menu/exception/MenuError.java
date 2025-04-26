package team.project.sos.domain.menu.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.project.sos.common.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum MenuError implements ErrorCode {

    NO_PERMISSION(HttpStatus.FORBIDDEN, "MENU_001", "사장님만 사용할 수 있는 기능입니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU_002", "메뉴가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}