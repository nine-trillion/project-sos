package team.project.sos.domain.menu.exception;

import team.project.sos.common.exception.BaseException;

public class MenuException extends BaseException {
    public MenuException(MenuError errorCode) {
        super(errorCode);
    }
}