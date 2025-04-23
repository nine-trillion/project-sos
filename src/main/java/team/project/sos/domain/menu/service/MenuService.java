
package team.project.sos.domain.menu.service;

import team.project.sos.domain.menu.dto.request.CreateMenuRequestDto;
import team.project.sos.domain.menu.dto.request.UpdateMenuRequestDto;
import team.project.sos.domain.menu.dto.response.MenuResponse;

public interface MenuService {
    MenuResponse save(CreateMenuRequestDto requestDto);
    MenuResponse find(Long menuId);
    MenuResponse update(Long menuId, UpdateMenuRequestDto requestDto);
    void remove(Long menuId);
    team.project.sos.domain.menu.entity.Menu findByIdOrElseThrow(Long menuId);
}
