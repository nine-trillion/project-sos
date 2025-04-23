package team.project.sos.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.project.sos.common.response.ApiResponse;
import team.project.sos.domain.auth.dto.request.SignUpRequestDto;
import team.project.sos.domain.auth.dto.response.SignUpResponseDto;
import team.project.sos.domain.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {
    private final AuthService authService;


    public ResponseEntity<ApiResponse<SignUpResponseDto>> save(
            @RequestBody SignUpRequestDto signUpRequestDto) {
            return ResponseEntity.ok().build();//authService.save(signUpRequestDto);
    }
}
