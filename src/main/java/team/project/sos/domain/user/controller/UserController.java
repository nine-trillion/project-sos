package team.project.sos.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.project.sos.common.response.ApiResponse;
import team.project.sos.common.response.MessageResponse;
import team.project.sos.domain.user.dto.request.UserPasswordRequestDto;
import team.project.sos.domain.user.dto.request.UserUpdateRequestDto;
import team.project.sos.domain.user.dto.response.UserResponseDto;
import team.project.sos.domain.user.service.UserService;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> findUser(
            @AuthenticationPrincipal String userIdStr
    ) {
        long userId = Long.parseLong(userIdStr);
        UserResponseDto findUser = userService.findUser(userId);
        return ResponseEntity.ok().body(ApiResponse.of("유저 정보를 조회합니다.", findUser));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> update(
            @AuthenticationPrincipal String userIdStr,
            @Valid @RequestBody UserUpdateRequestDto requestDto
    ) {
        long userId = Long.parseLong(userIdStr);
        UserResponseDto updateUser = userService.updateUser(userId, requestDto);
        return ResponseEntity.ok().body(ApiResponse.of("유저 정보를 수정합니다.", updateUser));
    }


    @PostMapping("/password")
    public ResponseEntity<MessageResponse> verifyPassword(
            @AuthenticationPrincipal String userIdStr,
            @Valid @RequestBody UserPasswordRequestDto requestDto,
            HttpServletResponse response
    ) {
        long userId = Long.parseLong(userIdStr);
        MessageResponse msg = userService.verifyPassword(userId, requestDto);

        ResponseCookie cookie = ResponseCookie.from("pwdConfirm", "true")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(300)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok().body(msg);
    }
}
