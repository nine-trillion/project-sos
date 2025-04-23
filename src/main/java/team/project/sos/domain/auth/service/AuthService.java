package team.project.sos.domain.auth.service;


import jakarta.validation.Valid;
import team.project.sos.domain.auth.dto.request.LoginRequestDto;
import team.project.sos.domain.auth.dto.request.SignUpRequestDto;
import team.project.sos.domain.auth.dto.response.LoginResponseDto;
import team.project.sos.domain.auth.dto.response.SignUpResponseDto;
import team.project.sos.domain.user.enums.UserRole;

public interface AuthService {
    SignUpResponseDto save(SignUpRequestDto requestDto, UserRole userRole);

    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
