package team.project.sos.domain.dashboard.repository;

import team.project.sos.domain.dashboard.dto.response.DashboardResponse;

import java.time.LocalDateTime;

public interface DashboardRepository {

    DashboardResponse getStoreDashboard(Long storeId, LocalDateTime start, LocalDateTime end, String basis);

    DashboardResponse getOwnerDashboard(Long OwnerId, LocalDateTime start, LocalDateTime end, String basis);
}
