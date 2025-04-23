
package team.project.sos.domain.menu.service;

import team.project.sos.domain.menu.dto.request.CreateMenuRequestDto;
import team.project.sos.domain.menu.dto.request.UpdateMenuRequestDto;
import team.project.sos.domain.menu.dto.response.MenuResponseDto;
import team.project.sos.domain.menu.entity.Menu;

public interface MenuService {
    MenuResponseDto save(CreateMenuRequestDto requestDto);
    MenuResponseDto find(Long menuId);
    MenuResponseDto update(Long menuId, UpdateMenuRequestDto requestDto);
    void remove(Long menuId);
    team.project.sos.domain.menu.entity.Menu findByIdOrElseThrow(Long menuId);
    Menu findMenuIncludeDeleted(Long menuId);
}
