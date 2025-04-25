package team.project.sos.domain.dashboard.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import team.project.sos.domain.store.dto.response.StoreResponse;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardResponse {

    StoreResponse store;
    long customerCount;
    long orderCount;
    long totalSales;
    String basis;
}
