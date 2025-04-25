package team.project.sos.domain.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.project.sos.common.response.ApiResponse;
import team.project.sos.common.response.MessageResponse;
import team.project.sos.domain.auth.dto.request.LoginRequestDto;
import team.project.sos.domain.auth.dto.request.SignUpRequestDto;
import team.project.sos.domain.auth.dto.response.LoginResponseDto;
import team.project.sos.domain.auth.dto.response.SignUpResponseDto;
import team.project.sos.domain.auth.service.AuthService;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    // 일반 유저 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> save(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto saveSignUp = authService.save(signUpRequestDto, UserRole.USER);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of("[USER] 회원가입이 완료되었습니다.", saveSignUp));
    }

    // 사장 회원가입
    @PostMapping("/signup/owner")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> saveOwner(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto saveSignUp = authService.save(signUpRequestDto, UserRole.OWNER);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of("[OWNER] 회원가입이 완료되었습니다.", saveSignUp));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse response) {

        // 응답 헤더에 토큰 추가
        LoginResponseDto login = authService.login(loginRequestDto);
        response.setHeader("Authorization", "Bearer " + login.getToken());

        LoginResponseDto loginUser = authService.login(loginRequestDto);
        return ResponseEntity.ok().body(ApiResponse.of("로그인이 완료되었습니다.", loginUser));

    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteUser(
            @CookieValue(name = "pwdConfirm", required = false) String pwdConfirm,
            @AuthenticationPrincipal String userIdStr
    ) {
        if (!"true".equalsIgnoreCase(pwdConfirm)) {
            throw new IllegalArgumentException("비밀번호 확인이 필요합니다.");
        }

        Long userId = Long.parseLong(userIdStr);
        MessageResponse msg = userService.delete(userId);

        ResponseCookie deleteCookie = ResponseCookie.from("pwdConfirm", "")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).body(msg);
    }
}
