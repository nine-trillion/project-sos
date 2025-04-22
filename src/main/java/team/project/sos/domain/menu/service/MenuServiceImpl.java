package team.project.sos.domain.menu.service;

import team.project.sos.domain.menu.dto.request.CreateMenuRequestDto;
import team.project.sos.domain.menu.dto.request.UpdateMenuRequestDto;
import team.project.sos.domain.menu.entity.Menu;
import team.project.sos.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public void createMenu(CreateMenuRequestDto requestDto) {
        Menu menu = new Menu(
                requestDto.getStoreId(),
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getCategory()
        );
        menuRepository.save(menu);
    }

    @Override
    public void updateMenu(Long menuId, UpdateMenuRequestDto requestDto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("메뉴 없음"));
        menu.update(requestDto.getName(), requestDto.getPrice(), requestDto.getCategory());
    }

    @Override
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("메뉴 없음"));
        menu.delete();
    }
}