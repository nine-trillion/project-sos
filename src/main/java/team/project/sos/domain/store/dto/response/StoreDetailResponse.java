package team.project.sos.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.project.sos.domain.menu.dto.response.MenuResponseDto;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreDetailResponse {

    private final StoreResponse store;
    private final List<MenuResponseDto> menus;

    public static StoreDetailResponse from(StoreResponse store, List<MenuResponseDto> menus) {
        return new StoreDetailResponse(store, menus);
    }
}
