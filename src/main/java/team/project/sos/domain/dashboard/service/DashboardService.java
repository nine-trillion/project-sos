package team.project.sos.domain.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.project.sos.domain.dashboard.dto.response.DashboardResponse;
import team.project.sos.domain.dashboard.exception.DashboardError;
import team.project.sos.domain.dashboard.exception.DashboardException;
import team.project.sos.domain.dashboard.repository.DashboardRepository;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.store.exception.StoreError;
import team.project.sos.domain.store.exception.StoreException;
import team.project.sos.domain.store.service.StoreService;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    private final UserService userService;
    private final StoreService storeService;

    public DashboardResponse getStoreDashboard(Long loginId, Long storeId, LocalDate date, YearMonth month) {
        validatedDateParam(date, month);

        userService.findByIdOrElseThrow(loginId);

        Store store = storeService.findStoreByIdOrElseThrow(storeId);
        Long storeOwnerId = store.getOwner().getId();

        if (!loginId.equals(storeOwnerId)) {
            throw new StoreException(StoreError.UNAUTHORIZED_STORE_OWNER);
        }

        LocalDateTime start = getStartDateTime(date, month);
        LocalDateTime end = getEndDateTime(date, month);
        String basis = getBasis(month);

        return dashboardRepository.getStoreDashboard(storeId, start, end, basis);
    }

    public DashboardResponse getOwnerDashboard(Long ownerId, LocalDate date, YearMonth month) {
        validatedDateParam(date, month);
        User owner = userService.findByIdOrElseThrow(ownerId);

        if (!UserRole.OWNER.equals(owner.getRole())) {
            throw new StoreException(StoreError.UNAUTHORIZED_STORE_OWNER);
        }

        LocalDateTime start = getStartDateTime(date, month);
        LocalDateTime end = getEndDateTime(date, month);
        String basis = getBasis(month);

        return dashboardRepository.getOwnerDashboard(ownerId, start, end, basis);
    }

    private void validatedDateParam(LocalDate date, YearMonth month) {
        if (date == null && month == null) {
            throw new DashboardException(DashboardError.DATE_REQUIRED);
        }
        if (date != null && month != null) {
            throw new DashboardException(DashboardError.DATE_PARAM_CONFLICT);
        }
    }

    private LocalDateTime getStartDateTime(LocalDate date, YearMonth month) {
        return date != null ? date.atStartOfDay() : month.atDay(1).atStartOfDay();
    }

    private LocalDateTime getEndDateTime(LocalDate date, YearMonth month) {
        return date != null ? date.atTime(LocalTime.MAX) : month.atEndOfMonth().atTime(LocalTime.MAX);
    }

    private String getBasis(YearMonth month) {
        return month != null ? "MONTH" : "DATE";
    }
}
