package team.project.sos.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.project.sos.common.response.ApiResponse;
import team.project.sos.domain.user.dto.response.UserResponseDto;
import team.project.sos.domain.user.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    private ResponseEntity<ApiResponse<UserResponseDto>> findUser(
            @AuthenticationPrincipal String userIdstr
    ) {
        log.info("유저 조회 {}","UserController");
        long userId = Long.parseLong(userIdstr);
        UserResponseDto findUser = userService.findUser(userId);
        return ResponseEntity.ok().body(ApiResponse.of("유저 정보를 조회합니다.", findUser));
    }
}
