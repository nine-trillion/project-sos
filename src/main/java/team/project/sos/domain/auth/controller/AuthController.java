package team.project.sos.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.project.sos.common.exception.BaseException;
import team.project.sos.common.response.ApiResponse;
import team.project.sos.common.response.MessageResponse;
import team.project.sos.domain.auth.dto.request.FindPasswordRequestDto;
import team.project.sos.domain.auth.dto.request.LoginRequestDto;
import team.project.sos.domain.auth.dto.request.SignUpRequestDto;
import team.project.sos.domain.auth.dto.response.FindPasswordResponseDto;
import team.project.sos.domain.auth.dto.response.LoginResponseDto;
import team.project.sos.domain.auth.dto.response.SignUpResponseDto;
import team.project.sos.domain.auth.exception.AuthError;
import team.project.sos.domain.auth.service.AuthService;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    /**
     * 일반 유저 회원가입 API
     *
     * @param signUpRequestDto
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> save(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto saveSignUp = authService.save(signUpRequestDto, UserRole.USER);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of("[USER] 회원가입이 완료되었습니다.", saveSignUp));
    }

    /**
     * 사장 권한 회원가입 API
     *
     * @param signUpRequestDto
     * @return
     */
    @PostMapping("/owner/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> saveOwner(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto saveSignUp = authService.save(signUpRequestDto, UserRole.OWNER);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of("[OWNER] 회원가입이 완료되었습니다.", saveSignUp));
    }

    /**
     * 로그인 API
     * 토큰을 body에 담아 전송
     *
     * @param loginRequestDto
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto) {

        LoginResponseDto loginUser = authService.login(loginRequestDto);
        return ResponseEntity.ok().body(ApiResponse.of("로그인이 완료되었습니다.", loginUser));

    }

    /**
     * 회원 탈퇴 API
     * pwdConfirm 쿠키 필요
     *
     * @param pwdConfirm
     * @param userIdStr  토큰 값 필요
     * @return
     */
    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteUser(
            @CookieValue(name = "pwdConfirm", required = false) String pwdConfirm,
            @AuthenticationPrincipal String userIdStr
    ) {
        if (!"true".equalsIgnoreCase(pwdConfirm)) {
            throw new BaseException(AuthError.AUTH_PWD_CONFIRM_REQUIRED);
        }

        Long userId = Long.parseLong(userIdStr);
        MessageResponse msg = userService.delete(userId);

        ResponseCookie deleteCookie = ResponseCookie.from("pwdConfirm", "")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).body(msg);
    }

    /**
     * 임시 비밀번호 발급 API
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/password")
    public ResponseEntity<ApiResponse<FindPasswordResponseDto>> findPassword(
            @Valid @RequestBody FindPasswordRequestDto requestDto
    ) {
        FindPasswordResponseDto responseDto = userService.findPassword(requestDto);
        return ResponseEntity.ok().body(ApiResponse.of("임시 비밀번호를 발급합니다.", responseDto));
    }
}
