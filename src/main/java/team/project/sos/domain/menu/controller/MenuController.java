package team.project.sos.domain.menu.controller;

import team.project.sos.domain.menu.dto.request.CreateMenuRequestDto;
import team.project.sos.domain.menu.dto.request.UpdateMenuRequestDto;
import team.project.sos.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<?> createMenu(@RequestBody CreateMenuRequestDto requestDto) {
        menuService.createMenu(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("메뉴가 등록되었습니다.");
    }

    @PatchMapping("/{menuId}")
    public ResponseEntity<?> updateMenu(@PathVariable Long menuId, @RequestBody UpdateMenuRequestDto requestDto) {
        menuService.updateMenu(menuId, requestDto);
        return ResponseEntity.ok("메뉴가 수정되었습니다.");
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok("메뉴가 삭제되었습니다.");
    }
}