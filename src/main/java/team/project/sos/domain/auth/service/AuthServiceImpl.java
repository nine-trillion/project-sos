package team.project.sos.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.sos.common.config.JwtProvider;
import team.project.sos.common.excepion.BaseException;
import team.project.sos.domain.auth.dto.request.LoginRequestDto;
import team.project.sos.domain.auth.dto.request.SignUpRequestDto;
import team.project.sos.domain.auth.dto.response.LoginResponseDto;
import team.project.sos.domain.auth.dto.response.SignUpResponseDto;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.repository.UserRepository;
import team.project.sos.domain.user.security.PasswordEncoder;

import java.util.Optional;

import static team.project.sos.domain.auth.exception.AuthError.*;
import static team.project.sos.domain.user.exception.UserError.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public SignUpResponseDto save(SignUpRequestDto requestDto, UserRole userRole) {
        Optional<User> exist = userRepository.findByEmail(requestDto.getEmail());
        if (exist.isPresent()) {
            User user = exist.get();
            if (user.isDeleted()) {
                // 탙퇴한 계정
                throw new BaseException(USER_DEACTIVATED);
            }
            // 이메일 중복
            throw new BaseException(DUPLICATE_EMAIL);
        }
        if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
            throw new BaseException(DUPLICATE_PHONE_NUMBER);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Grade grade = getGrade(userRole);
        User newUser = User.createUser(requestDto, userRole, encodedPassword, grade);

        User savedUser = userRepository.save(newUser);
        return SignUpResponseDto.from(savedUser);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
                () -> new BaseException(USER_NOT_FOUND)
        );
        if (user.isDeleted()) {
            throw new BaseException(DEACTIVATED_ACCOUNT_LOGIN);
        }
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new BaseException(INVALID_PASSWORD);
        }
        String token = jwtProvider.createToken(user.getId(), user.getEmail(), user.getRole());
        return LoginResponseDto.from(user, token);
    }

    private Grade getGrade(UserRole userRole) {
        return switch (userRole) {
            case ADMIN -> Grade.VIP;
            case USER, OWNER -> Grade.BASIC;
        };
    }
}
