package team.project.sos.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.project.sos.domain.menu.dto.request.CreateMenuRequestDto;
import team.project.sos.domain.menu.dto.request.UpdateMenuRequestDto;
import team.project.sos.domain.menu.dto.response.MenuResponse;
import team.project.sos.domain.menu.entity.Menu;
import team.project.sos.domain.menu.repository.MenuRepository;
import team.project.sos.domain.menu.exception.MenuException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    // TODO: [권한 체크] 현재는 임시 role 파라미터 사용.
    // 추후 Security 적용 시, 인증 정보에서 사용자 권한을 확인하도록 변경 예정.
    private void checkOwnerRole(String role) {
        if (!"OWNER".equals(role)) {
            throw new MenuException("사장님만 사용할 수 있는 기능입니다.");
        }
    }

    @Override
    public MenuResponse save(CreateMenuRequestDto requestDto) {
        checkOwnerRole(requestDto.getRole());  // 임시 권한 체크
        Menu menu = new Menu(
                requestDto.getStoreId(),
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getCategory()
        );
        return MenuResponse.from(menuRepository.save(menu));
    }

    @Override
    public MenuResponse find(Long menuId) {
        return MenuResponse.from(findByIdOrElseThrow(menuId));
    }

    @Override
    public MenuResponse update(Long menuId, UpdateMenuRequestDto requestDto) {
        checkOwnerRole(requestDto.getRole());  // 임시 권한 체크
        Menu menu = findByIdOrElseThrow(menuId);
        menu.update(requestDto.getName(), requestDto.getPrice(), requestDto.getCategory());
        return MenuResponse.from(menu);
    }

    @Override
    public void remove(Long menuId) {
        // TODO: [권한 체크] 삭제 기능에도 동일하게 적용
        checkOwnerRole("OWNER");  // 임시 값, 추후 수정
        Menu menu = findByIdOrElseThrow(menuId);
        menu.delete();
    }

    @Override
    public Menu findByIdOrElseThrow(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException("메뉴가 존재하지 않습니다."));
    }

    // NOTE: [가게 조회 연동]
    // 가게 단건 조회 시, 이 메서드를 호출해서 메뉴 리스트를 포함하세요.
    public List<MenuResponse> getMenusByStore(Long storeId) {
        return menuRepository.findAllByStoreIdAndIsDeletedFalse(storeId)
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    // NOTE: [주문 파트 연동]
    // 주문 내역 조회 시 삭제된 메뉴 포함 조회가 필요할 경우 사용하세요.
    public Menu findMenuIncludeDeleted(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException("메뉴가 존재하지 않습니다."));
    }
}