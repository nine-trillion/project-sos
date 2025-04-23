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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AdminAuthController {

    private final AuthService authService;

    @PostMapping("/admin/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> saveAdmin(
            @Valid @RequestBody SignUpRequestDto requestDto
    ) {
        SignUpResponseDto saveAdmin = authService.save(requestDto, UserRole.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of("관리자 등록이 완료되었습니다.",saveAdmin));
    }
}
