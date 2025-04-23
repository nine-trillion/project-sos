package team.project.sos.domain.menu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.project.sos.domain.menu.dto.request.CreateMenuRequestDto;
import team.project.sos.domain.menu.dto.request.UpdateMenuRequestDto;
import team.project.sos.domain.menu.dto.response.MenuResponseDto;
import team.project.sos.domain.menu.service.MenuService;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 생성
     * TODO: [권한] 현재 role을 요청 DTO로 받지만, 추후 인증 정보에서 권한 확인 예정, ❗❗❗️DTO파일도 수정 필요❗❗❗
     */
    @PostMapping
    public ResponseEntity<MenuResponseDto> saveMenu(@RequestBody CreateMenuRequestDto requestDto) {
        MenuResponseDto response = menuService.save(requestDto);
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
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long menuId, @RequestBody UpdateMenuRequestDto requestDto) {
        MenuResponseDto response = menuService.update(menuId, requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 메뉴 삭제 (Soft Delete)
     */
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> removeMenu(@PathVariable Long menuId) {
        menuService.remove(menuId);
        return ResponseEntity.noContent().build();
    }
}