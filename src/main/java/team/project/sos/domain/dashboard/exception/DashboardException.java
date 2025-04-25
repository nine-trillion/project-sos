package team.project.sos.domain.dashboard.exception;

import team.project.sos.common.exception.BaseException;

public class DashboardException extends BaseException {

    public DashboardException(DashboardError errorCode) {
        super(errorCode);
    }
}
