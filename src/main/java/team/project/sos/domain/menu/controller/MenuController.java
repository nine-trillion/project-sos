package team.project.sos.domain.menu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.project.sos.domain.menu.dto.request.CreateMenuRequestDto;
import team.project.sos.domain.menu.dto.request.UpdateMenuRequestDto;
import team.project.sos.domain.menu.dto.response.MenuResponseDto;
import team.project.sos.domain.menu.service.MenuService;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 생성
     */
    @PostMapping
    public ResponseEntity<MenuResponseDto> saveMenu(@RequestBody CreateMenuRequestDto requestDto,
                                                    @AuthenticationPrincipal String userId) {
        Long loginId = Long.parseLong(userId);
        MenuResponseDto response = menuService.save(loginId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 메뉴 단건 조회 (테스트용)
     * TODO: 실제 서비스에선 가게 조회 시 포함되므로, 이 엔드포인트는 테스트용으로만 사용
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> findMenu(@PathVariable Long menuId) {
        MenuResponseDto response = menuService.find(menuId);
        return ResponseEntity.ok(response);
    }

    /**
     * 메뉴 수정
     */
    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long menuId,
                                                      @RequestBody UpdateMenuRequestDto requestDto,
                                                      @AuthenticationPrincipal String userId) {
        Long loginId = Long.parseLong(userId);
        MenuResponseDto response = menuService.update(loginId, menuId, requestDto);

        return ResponseEntity.ok(response);
    }

    /**
     * 메뉴 삭제 (Soft Delete)
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> removeMenu(@PathVariable Long menuId,
                                           @AuthenticationPrincipal String userId) {
        Long loginId = Long.parseLong(userId);
        menuService.remove(loginId, menuId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 카테고리별 메뉴 조회
     */
    @GetMapping("/store/{storeId}/category/{category}")
    public List<MenuResponseDto> getMenusByCategory(@PathVariable Long storeId, @PathVariable String category) {
        return menuService.findMenusByCategory(storeId, category);
    }
}