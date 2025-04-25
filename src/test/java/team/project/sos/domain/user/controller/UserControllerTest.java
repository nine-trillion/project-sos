package team.project.sos.domain.user.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import team.project.sos.common.config.JwtProvider;
import team.project.sos.common.config.SecurityConfig;
import team.project.sos.common.response.MessageResponse;
import team.project.sos.domain.user.dto.request.UserPasswordRequestDto;
import team.project.sos.domain.user.dto.request.UserUpdateRequestDto;
import team.project.sos.domain.user.dto.response.UserResponseDto;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.service.UserService;

import java.time.LocalDateTime;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtProvider.class})
@TestPropertySource(properties = "JWT_SECRET_KEY=+kC/IlyIJYd4FxhynnrEAzIosk3viw1f4IHgHoAnaIRzJDYQGyuTuZka49qGp/hS2LVkLm0OXF4cLtPxhk4Dsg==")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 조회 성공")
    void findUser_ok() throws Exception {
        //given
        long userId = 1L;
        UserResponseDto responseDto = new UserResponseDto(
                userId,
                "test@abc.com",
                "테스트조회",
                "010-1234-1234",
                UserRole.USER,
                Grade.BASIC,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        String token = jwtProvider.createToken(userId, responseDto.getEmail(), responseDto.getRole());

        given(userService.findUser(userId)).willReturn(responseDto);
        // when/ then
        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("유저 정보를 조회합니다."))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.email").value("test@abc.com"))
                .andExpect(jsonPath("$.data.nickname").value("테스트조회"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-1234-1234"));
    }

    @Test
    @DisplayName("회원 정보 수정")
    void update_o() throws Exception {

        long userId = 1L;
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto("수정", "010-1234-1234");
        UserResponseDto responseDto = new UserResponseDto(
                userId,
                "test@abc.com",
                "수정",
                "010-1234-1234",
                UserRole.USER,
                Grade.BASIC,
                LocalDateTime.now(),
                LocalDateTime.now());

        String token = jwtProvider.createToken(userId, responseDto.getEmail(), responseDto.getRole());
        String json = objectMapper.writeValueAsString(requestDto);
        given(userService.updateUser(eq(userId), any(UserUpdateRequestDto.class))).willReturn(responseDto);
        // when/ then
        mockMvc.perform(put("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("유저 정보를 수정합니다."))
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.email").value("test@abc.com"))
                .andExpect(jsonPath("$.data.nickname").value("수정"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-1234-1234"));
    }

    @Test
    @DisplayName("비밀번호 검증 성공")
    void verifyPassword_o() throws Exception {

        long userId = 1L;
        UserPasswordRequestDto requestDto = new UserPasswordRequestDto("Password!");
        MessageResponse response = new MessageResponse("비밀번호가 확인되었습니다.");
        String token = jwtProvider.createToken(userId, "test@abc.com", UserRole.USER);
        String json = objectMapper.writeValueAsString(requestDto);

        given(userService.verifyPassword(eq(userId), any(UserPasswordRequestDto.class))).willReturn(response);

        mockMvc.perform(post("/api/users/password")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                // 쿠키 확인
                .andExpect(cookie().exists("pwdConfirm"))
                .andExpect(cookie().value("pwdConfirm", "true"))
                .andExpect(cookie().maxAge("pwdConfirm", 300))
                // 응답 JSON 확인
                .andExpect(jsonPath("$.message").value("비밀번호가 확인되었습니다."));
    }

}
