
package team.project.sos.domain.menu.service;

import team.project.sos.domain.menu.dto.request.CreateMenuRequestDto;
import team.project.sos.domain.menu.dto.request.UpdateMenuRequestDto;
import team.project.sos.domain.menu.dto.response.MenuResponseDto;
import team.project.sos.domain.menu.entity.Menu;

import java.util.List;

public interface MenuService {

    MenuResponseDto save(Long loginId, CreateMenuRequestDto requestDto);

    MenuResponseDto find(Long menuId);

    MenuResponseDto update(Long loginId, Long menuId, UpdateMenuRequestDto requestDto);

    void remove(Long loginId, Long menuId);

    team.project.sos.domain.menu.entity.Menu findByIdOrElseThrow(Long menuId);

    Menu findMenuIncludeDeleted(Long menuId);

    List<MenuResponseDto> getMenusByStore(Long storeId);

    List<MenuResponseDto> getMenusByStoreAndCategory(Long storeId, String category);

    List<MenuResponseDto> findMenusByCategory(Long storeId, String category);
}
