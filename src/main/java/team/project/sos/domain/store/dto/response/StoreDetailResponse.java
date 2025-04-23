package team.project.sos.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.project.sos.domain.menu.dto.response.MenuResponse;
import team.project.sos.domain.menu.dto.response.MenuResponse;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreDetailResponse {

    private final StoreSaveResponse store;
    private final List<MenuResponse> menus;
}
