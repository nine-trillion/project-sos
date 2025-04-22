package team.project.sos.domain.menu.service;

import team.project.sos.domain.menu.dto.request.CreateMenuRequestDto;
import team.project.sos.domain.menu.dto.request.UpdateMenuRequestDto;

public interface MenuService {
    void createMenu(CreateMenuRequestDto requestDto);
    void updateMenu(Long menuId, UpdateMenuRequestDto requestDto);
    void deleteMenu(Long menuId);
}