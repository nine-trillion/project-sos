package team.project.sos.domain.menu.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.project.sos.domain.menu.dto.request.CreateMenuRequestDto;
import team.project.sos.domain.menu.dto.request.UpdateMenuRequestDto;
import team.project.sos.domain.menu.dto.response.MenuResponseDto;
import team.project.sos.domain.menu.entity.Menu;
import team.project.sos.domain.menu.exception.MenuError;
import team.project.sos.domain.menu.exception.MenuException;
import team.project.sos.domain.menu.repository.MenuRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

    @InjectMocks
    private MenuServiceImpl menuService;

    @Mock
    private MenuRepository menuRepository;

    @Test
    @DisplayName("메뉴 생성 성공")
    void saveMenuSuccess() {
        // given
        CreateMenuRequestDto requestDto = CreateMenuRequestDto.builder()
                .storeId(1L)
                .name("비빔밥")
                .price(9000)
                .category("한식")
                .role("OWNER")
                .build();

        Menu savedMenu = new Menu(1L, "비빔밥", 9000, "한식");
        when(menuRepository.save(any(Menu.class))).thenReturn(savedMenu);

        // when
        MenuResponseDto responseDto = menuService.save(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("비빔밥", responseDto.getName());
    }

    @Test
    @DisplayName("메뉴 생성 실패 - 권한 없음")
    void saveMenuFail_NoPermission() {
        // given
        CreateMenuRequestDto requestDto = CreateMenuRequestDto.builder()
                .storeId(1L)
                .name("비빔밥")
                .price(9000)
                .category("한식")
                .role("USER")  // 사장님 권한 아님
                .build();

        // when & then
        MenuException exception = assertThrows(MenuException.class, () -> menuService.save(requestDto));
        assertEquals(MenuError.NO_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("메뉴 조회 성공")
    void findMenuSuccess() {
        // given
        Menu menu = new Menu(1L, "된장찌개", 8500, "한식");
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // when
        MenuResponseDto responseDto = menuService.find(1L);

        // then
        assertNotNull(responseDto);
        assertEquals("된장찌개", responseDto.getName());
    }

    @Test
    @DisplayName("메뉴 조회 실패 - 존재하지 않는 메뉴")
    void findMenuFail_NotFound() {
        // given
        when(menuRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        MenuException exception = assertThrows(MenuException.class, () -> menuService.find(99L));
        assertEquals(MenuError.MENU_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("메뉴 수정 성공")
    void updateMenuSuccess() {
        // given
        Menu menu = new Menu(1L, "김치찌개", 8000, "한식");
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        UpdateMenuRequestDto updateDto = UpdateMenuRequestDto.builder()
                .name("순두부찌개")
                .price(8500)
                .category("한식")
                .role("OWNER")
                .build();

        // when
        MenuResponseDto responseDto = menuService.update(1L, updateDto);

        // then
        assertEquals("순두부찌개", responseDto.getName());
        assertEquals(8500, responseDto.getPrice());
    }

    @Test
    @DisplayName("메뉴 삭제 성공")
    void removeMenuSuccess() {
        // given
        Menu menu = new Menu(1L, "비빔밥", 9000, "한식");
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // when
        menuService.remove(1L);

        // then
        assertTrue(menu.isDeleted());
    }
}