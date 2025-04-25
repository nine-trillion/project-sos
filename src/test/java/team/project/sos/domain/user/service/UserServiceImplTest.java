package team.project.sos.domain.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import team.project.sos.common.excepion.BaseException;
import team.project.sos.common.response.MessageResponse;
import team.project.sos.domain.user.dto.request.UserPasswordRequestDto;
import team.project.sos.domain.user.dto.request.UserUpdateRequestDto;
import team.project.sos.domain.user.dto.response.UserResponseDto;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.repository.UserRepository;
import team.project.sos.domain.user.security.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static team.project.sos.domain.auth.exception.AuthError.INVALID_PASSWORD;
import static team.project.sos.domain.user.exception.UserError.DUPLICATE_PHONE_NUMBER;
import static team.project.sos.domain.user.exception.UserError.USER_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
                "test@abc.com",
                "Password!",
                "테스트",
                "010-1234-1234",
                UserRole.USER,
                Grade.BASIC
        );
        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @Test
    @DisplayName("유저 조회 성공")
    void find_success() {
        //given
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));
        Long userId = user.getId();
        //when
        UserResponseDto dto = userService.findUser(userId);

        //then
        assertThat(dto.getUserId()).isEqualTo(1L);
        assertThat(dto.getEmail()).isEqualTo("test@abc.com");
        assertThat(dto.getNickname()).isEqualTo("테스트");
        assertThat(dto.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("Id로 유저 조회 실패")
    void find_failed() {
        //given
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());

        // when/ then
        BaseException ex = assertThrows(
                BaseException.class,
                () -> userService.findByIdOrElseThrow(user.getId())
        );
        assertThat(ex.getErrorCode()).isEqualTo(USER_NOT_FOUND);
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void update_o() {
        //given
        Long userId = user.getId();
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto("테스트수정", "010-1234-1234");
        given(userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())).willReturn(false);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        //when
        UserResponseDto responseDto = userService.updateUser(userId, requestDto);
        //then
        assertThat(responseDto.getNickname()).isEqualTo("테스트수정");
        assertThat(responseDto.getPhoneNumber()).isEqualTo("010-1234-1234");
    }

    @Test
    @DisplayName("중복된 핸드폰 번호 요청시 회원정보 수정 실패")
    void update_x() {
        //given
        Long userId = user.getId();
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto("테스트수정", "010-1234-1234");
        given(userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())).willReturn(true);
        //when
        BaseException ex = assertThrows(
                BaseException.class,
                () -> userService.updateUser(userId, requestDto)
        );
        //then
        assertThat(ex.getErrorCode()).isEqualTo(DUPLICATE_PHONE_NUMBER);
    }

    @Test
    @DisplayName("비밀번호 검증 성공")
    void verifyPassword_o() {
        //given
        Long userId = user.getId();
        String encoded = "encode";
        UserPasswordRequestDto requestDto = new UserPasswordRequestDto("Password!");
        ReflectionTestUtils.setField(user, "password", encoded);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(requestDto.getPassword(), encoded)).willReturn(true);

        // when/ then
        assertThatCode(() -> userService.verifyPassword(userId, requestDto))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("비밀번호 검증 실패")
    void verifyPassword_x() {
        // given
        Long userId = user.getId();
        UserPasswordRequestDto requestDto = new UserPasswordRequestDto("WrongPassword");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).willReturn(false);

        // when
        BaseException ex = assertThrows(
                BaseException.class,
                () -> userService.verifyPassword(userId, requestDto)
        );
        // then
        assertThat(ex.getErrorCode()).isEqualTo(INVALID_PASSWORD);

    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void delete_success() {
        //given
        Long userId = user.getId();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        //when
        userService.delete(userId);
        MessageResponse response = new MessageResponse("탈퇴되었습니다.");

        //then
        assertThat(response.getMessage()).isEqualTo("탈퇴되었습니다.");
    }

    @Test
    @DisplayName("회원 탈퇴 실패")
    void delete_failed() {
        //given
        Long userId = user.getId();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when
        BaseException ex = assertThrows(
                BaseException.class,
                () -> userService.delete(userId)
        );
        //then
        assertThat(ex.getErrorCode())
                .isEqualTo(USER_NOT_FOUND);
    }


}
