package team.project.sos.domain.auth.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.project.sos.common.response.ApiResponse;
import team.project.sos.domain.auth.dto.request.SignUpRequestDto;
import team.project.sos.domain.auth.dto.response.SignUpResponseDto;
import team.project.sos.domain.auth.service.AuthService;
import team.project.sos.domain.user.enums.UserRole;

/**
 * 관리자 전용 회원가입 API 입니다.
 * 엔드포인트 : POST /api/auth/admin/signup
 * WHITE_LIST 에 포함되어있습니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AdminAuthController {

    private final AuthService authService;

    /**
     * 관리자를 신규 등록하는 API
     *
     * @param requestDto 회원가입 요청 DTO (이메일, 비밀번호, 닉네임, 휴대폰 번호)
     * @return 등록 성공 시 201(CREATED) 상태 코드와 함께 성공 메시지 및 관리자 정보 반환
     *
     * <p><strong>예외:</strong>
     * <ul>
     *     <li>입력값이 유효하지 않은 경우 {@code MethodArgumentNotValidException}</li>
     *     <li>중복 이메일, 휴대폰 번호 등이 존재할 경우 {@code BaseException}</li>
     * </ul>
     * </p>
     */
    @PostMapping("/admin/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> saveAdmin(
            @Valid @RequestBody SignUpRequestDto requestDto
    ) {
        SignUpResponseDto saveAdmin = authService.save(requestDto, UserRole.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("관리자 등록이 완료되었습니다.", saveAdmin));
    }
}
