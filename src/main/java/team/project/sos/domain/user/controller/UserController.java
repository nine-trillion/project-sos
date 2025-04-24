package team.project.sos.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.project.sos.common.response.ApiResponse;
import team.project.sos.domain.user.dto.request.UserUpdateRequestDto;
import team.project.sos.domain.user.dto.response.UserResponseDto;
import team.project.sos.domain.user.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> findUser(
            @AuthenticationPrincipal String userIdStr
    ) {
        log.info("유저 조회 {}", "UserController");
        long userId = Long.parseLong(userIdStr);
        UserResponseDto findUser = userService.findUser(userId);
        return ResponseEntity.ok().body(ApiResponse.of("유저 정보를 조회합니다.", findUser));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> update(
            @AuthenticationPrincipal String userIdStr,
            @Valid @RequestBody UserUpdateRequestDto requestDto
    ) {
        log.info("정보 수정 UserController:{}", "/api/user");
        long userId = Long.parseLong(userIdStr);
        UserResponseDto updateUser = userService.updateUser(userId, requestDto);
        return ResponseEntity.ok().body(ApiResponse.of("유저 정보를 수정합니다.", updateUser));
    }

//    @GetMapping("/password")
//  비밀번호 확인 api 응답값은 그냥 메시지 주고 쿠키에 값을 넣어서 준다 ,,
}
