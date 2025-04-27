package team.project.sos.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.sos.domain.menu.dto.request.CreateMenuRequestDto;
import team.project.sos.domain.menu.dto.request.UpdateMenuRequestDto;
import team.project.sos.domain.menu.dto.response.MenuResponseDto;
import team.project.sos.domain.menu.entity.Menu;
import team.project.sos.domain.menu.exception.MenuError;
import team.project.sos.domain.menu.exception.MenuException;
import team.project.sos.domain.menu.repository.MenuRepository;
import team.project.sos.domain.store.validator.StoreValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final StoreValidator storeValidator;

    @Override
    public MenuResponseDto save(Long loginId, CreateMenuRequestDto requestDto) {
        storeValidator.validateOwner(loginId, requestDto.getStoreId());

        Menu menu = new Menu(
                requestDto.getStoreId(),
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getCategory()
        );

        return MenuResponseDto.from(menuRepository.save(menu));
    }

    @Override
    public MenuResponseDto find(Long menuId) {
        return MenuResponseDto.from(findByIdOrElseThrow(menuId));
    }

    @Override
    @Transactional
    public MenuResponseDto update(Long loginId, Long menuId, UpdateMenuRequestDto requestDto) {
        Menu menu = findByIdOrElseThrow(menuId);
        storeValidator.validateOwner(loginId, menu.getStoreId());

        menu.update(requestDto.getName(), requestDto.getPrice(), requestDto.getCategory());

        return MenuResponseDto.from(menu);
    }

    @Override
    @Transactional
    public void remove(Long loginId, Long menuId) {
        Menu menu = findByIdOrElseThrow(menuId);
        storeValidator.validateOwner(loginId, menu.getStoreId());

        menu.delete();
    }

    @Override
    public Menu findByIdOrElseThrow(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuError.MENU_NOT_FOUND));
    }

    @Override
    public List<MenuResponseDto> getMenusByStoreAndCategory(Long storeId, String category) {
        return menuRepository.findAllByStoreIdAndCategoryAndIsDeletedFalse(storeId, category)
                .stream()
                .map(MenuResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuResponseDto> findMenusByCategory(Long storeId, String category) {
        return menuRepository.findAllByStoreIdAndCategoryAndIsDeletedFalse(storeId, category)
                .stream()
                .map(MenuResponseDto::from)
                .collect(Collectors.toList());
    }

    // NOTE: [가게 조회 연동]
    // 가게 단건 조회 시, 이 메서드를 호출해서 메뉴 리스트를 포함하세요.
    public List<MenuResponseDto> getMenusByStore(Long storeId) {
        return menuRepository.findAllByStoreIdAndIsDeletedFalse(storeId)
                .stream()
                .map(MenuResponseDto::from)
                .collect(Collectors.toList());
    }

    // NOTE: [주문 파트 연동]
    // 주문 내역 조회 시 삭제된 메뉴 포함 조회가 필요할 경우 사용하세요.
    public Menu findMenuIncludeDeleted(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuError.MENU_NOT_FOUND));
    }
}