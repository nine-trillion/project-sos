package team.project.sos.domain.dashboard.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.project.sos.domain.dashboard.dto.response.DashboardResponse;
import team.project.sos.domain.dashboard.service.DashboardService;

import java.time.LocalDate;
import java.time.YearMonth;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/store/{storeId}")
    public ResponseEntity<DashboardResponse> getStoreDashboard(
            @AuthenticationPrincipal String userId,
            @NotNull @PathVariable Long storeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        Long loginId = Long.parseLong(userId);
        return ResponseEntity.ok(dashboardService.getStoreDashboard(loginId, storeId, date, month));
    }

    @GetMapping("/owner")
    public ResponseEntity<DashboardResponse> getOwnerDashboard(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        Long loginId = Long.parseLong(userId);
        return ResponseEntity.ok(dashboardService.getOwnerDashboard(loginId, date, month));
    }
}
