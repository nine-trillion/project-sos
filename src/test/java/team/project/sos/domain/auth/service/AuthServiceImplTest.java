package team.project.sos.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static team.project.sos.domain.user.exception.UserError.*;
import static team.project.sos.domain.auth.exception.AuthError.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.jupiter.MockitoExtension;

import team.project.sos.common.config.JwtProvider;
import team.project.sos.common.excepion.BaseException;
import team.project.sos.domain.auth.dto.request.LoginRequestDto;
import team.project.sos.domain.auth.dto.request.SignUpRequestDto;
import team.project.sos.domain.auth.dto.response.LoginResponseDto;
import team.project.sos.domain.auth.dto.response.SignUpResponseDto;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.exception.UserError;
import team.project.sos.domain.user.repository.UserRepository;
import team.project.sos.domain.user.security.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("이메일/전화번호 중복 없고 정상 요청이면 회원가입 성공_USER")
    void save_success() {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "test@example.com",
                "Password1!",
                "테스트닉네임",
                "01012345678"
        );
        // 이메일 중복 체크 통과
        given(userRepository.findByEmail(requestDto.getEmail()))
                .willReturn(Optional.empty());
        // 전화번호 중복 체크 통과
        given(userRepository.existsByPhoneNumber(requestDto.getPhoneNumber()))
                .willReturn(false);
        // 비밀번호 인코딩
        given(passwordEncoder.encode(requestDto.getPassword()))
                .willReturn("encodedPassword");
        UserRole role = UserRole.USER;
        Grade grade = authService.getGrade(role);

        User savedUser = User.createUser(
                requestDto,
                role,
                "encodedPassword",
                grade
        );
        // 리플렉션으로 ID 세팅
        ReflectionTestUtils.setField(savedUser, "id", 1L);

        given(userRepository.save(any(User.class)))
                .willReturn(savedUser);

        // when
        SignUpResponseDto response = authService.save(requestDto, UserRole.USER);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getNickname()).isEqualTo("테스트닉네임");
        assertThat(response.getPhoneNumber()).isEqualTo("01012345678");
        assertThat(response.getRole()).isEqualTo(UserRole.USER);
        assertThat(response.getGrade()).isEqualTo(Grade.BASIC);
    }

    @Test
    @DisplayName("이메일/전화번호 중복 없고 정상 요청이면 회원가입 성공_관리자")
    void save_success_admin() {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "test@example.com",
                "Password1!",
                "관리자",
                "01012345678"
        );
        // 이메일 중복 체크 통과
        given(userRepository.findByEmail(requestDto.getEmail()))
                .willReturn(Optional.empty());
        // 전화번호 중복 체크 통과
        given(userRepository.existsByPhoneNumber(requestDto.getPhoneNumber()))
                .willReturn(false);
        // 비밀번호 인코딩
        given(passwordEncoder.encode(requestDto.getPassword()))
                .willReturn("encodedPassword");

        UserRole role = UserRole.ADMIN;
        Grade grade = authService.getGrade(role);

        User savedUser = User.createUser(
                requestDto,
                role,
                "encodedPassword",
                grade
        );
        // 리플렉션으로 ID 세팅
        ReflectionTestUtils.setField(savedUser, "id", 2L);

        given(userRepository.save(any(User.class)))
                .willReturn(savedUser);

        // when
        SignUpResponseDto response = authService.save(requestDto, UserRole.USER);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(2L);
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getNickname()).isEqualTo("관리자");
        assertThat(response.getPhoneNumber()).isEqualTo("01012345678");
        assertThat(response.getRole()).isEqualTo(UserRole.ADMIN);
        assertThat(response.getGrade()).isEqualTo(Grade.VIP);
    }

    @Test
    @DisplayName("이메일 중복 요청이면 회원가입 실패")
    void save_fail() {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "duplicate@example.com",
                "Password1!",
                "이메일중복",
                "01012345678"
        );
        // 이미 같은 이메일을 가진 User가 DB에 존재하고, isDeleted == false
        User existing = User.createUser(
                requestDto,
                UserRole.USER,
                "encoded",
                Grade.BASIC
        );
        given(userRepository.findByEmail(requestDto.getEmail()))
                .willReturn(Optional.of(existing));

        // when & then
        BaseException ex = assertThrows(
                BaseException.class,
                () -> authService.save(requestDto, UserRole.USER)
        );
        // 중복 이메일 에러 코드 확인
        assertThat(ex.getErrorCode()).isEqualTo(UserError.DUPLICATE_EMAIL);

    }

    @Test
    @DisplayName("핸드폰 번호 중복 요청이면 회원가입 실패")
    void save_fail_duplicate_phone_number() {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "duplicate@example.com",
                "Password1!",
                "핸드폰 번호 중복",
                "01012345678"
        );

        given(userRepository.existsByPhoneNumber(requestDto.getPhoneNumber()))
                .willReturn(true);

        // when & then
        BaseException ex = assertThrows(
                BaseException.class,
                () -> authService.save(requestDto, UserRole.USER)
        );
        // 중복 이메일 에러 코드 확인
        assertThat(ex.getErrorCode()).isEqualTo(UserError.DUPLICATE_PHONE_NUMBER);

    }

    @Test
    @DisplayName("탈퇴한 계정의 이메일로 요청하면 회원가입 실패")
    void save_fail_deactivated() {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "duplicate@example.com",
                "Password1!",
                "이메일중복",
                "01012345678"
        );

        User existing = User.createUser(
                requestDto,
                UserRole.USER,
                "encoded",
                Grade.BASIC
        );

        // soft-delete 처리
        existing.deleteUser();

        given(userRepository.findByEmail(requestDto.getEmail()))
                .willReturn(Optional.of(existing));

        // when & then
        BaseException ex = assertThrows(
                BaseException.class,
                () -> authService.save(requestDto, UserRole.USER)
        );
        // 탈퇴한 이메일 에러 코드 확인
        assertThat(ex.getErrorCode()).isEqualTo(UserError.USER_DEACTIVATED);

    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        //given
        User user = new User(
                "test@abc.com",
                "encoded",
                "테스트",
                "01012341234",
                UserRole.USER,
                Grade.BASIC
        );
        LoginRequestDto requestDto = new LoginRequestDto("test@abc.com", "Password!");

        given(userRepository.findByEmail(requestDto.getEmail()))
                .willReturn(Optional.of(user));

        given(passwordEncoder.matches(requestDto.getPassword(), user.getPassword()))
                .willReturn(true);
        given(jwtProvider.createToken(user.getId(), user.getEmail(), user.getRole()))
                .willReturn("token");

        //when
        LoginResponseDto responseDto = authService.login(requestDto);

        //then
        assertThat(responseDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(responseDto.getToken()).isEqualTo("token");
    }


    @Test
    @DisplayName("등록되지 않은 이메일로 로그인 시 USER_NOT_FOUND 예외")
    void login_fail_userNotFound() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto("test@abc.com", "Password!");

        given(userRepository.findByEmail(requestDto.getEmail()))
                .willReturn(Optional.empty());

        // when & then
        BaseException ex = assertThrows(
                BaseException.class,
                () -> authService.login(requestDto)
        );
        assertThat(ex.getErrorCode()).isEqualTo(USER_NOT_FOUND);
    }

    @Test
    @DisplayName("탈퇴된 계정으로 로그인 시 DEACTIVATED_ACCOUNT_LOGIN 예외")
    void login_fail_deactivated() {
        // given
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        user.deleteUser();  // soft-delete flag

        LoginRequestDto requestDto = new LoginRequestDto("test@abc.com", "Password!");

        given(userRepository.findByEmail(requestDto.getEmail()))
                .willReturn(Optional.of(user));

        // when & then
        BaseException ex = assertThrows(
                BaseException.class,
                () -> authService.login(requestDto)
        );
        assertThat(ex.getErrorCode()).isEqualTo(DEACTIVATED_ACCOUNT_LOGIN);
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시 INVALID_PASSWORD 예외")
    void login_fail_invalidPassword() {
        // given
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        LoginRequestDto requestDto = new LoginRequestDto("test@abc.com", "Password!");

        given(userRepository.findByEmail(requestDto.getEmail()))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(requestDto.getPassword(), user.getPassword()))
                .willReturn(false);

        // when & then
        BaseException ex = assertThrows(
                BaseException.class,
                () -> authService.login(requestDto)
        );
        assertThat(ex.getErrorCode()).isEqualTo(INVALID_PASSWORD);
    }
}

