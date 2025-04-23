package team.project.sos.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import team.project.sos.domain.auth.controller.AuthController;
import team.project.sos.domain.auth.dto.request.SignUpRequestDto;
import team.project.sos.domain.auth.dto.response.SignUpResponseDto;
import team.project.sos.domain.auth.service.AuthService;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@SpringBootTest
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    void signup_o() throws Exception {
        // given
        String email = "test@example.com";
        String password = "Abcde123!";
        String nickName = "테스트";
        String phone = "010-1234-5678";

        SignUpRequestDto requestDto = new SignUpRequestDto(email, password, nickName, phone);
        SignUpResponseDto responseDto = new SignUpResponseDto(1L, email, nickName, phone, UserRole.USER, Grade.BASIC);

        given(authService.save(any(SignUpRequestDto.class), any(UserRole.class)))
                .willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."))
                .andExpect(jsonPath("$.data.nickname").value("테스트"));
    }

    @Test
    @DisplayName("관리자 회원가입 성공")
    void signup_o_admin() throws Exception {
        // given
        String email = "test@example.com";
        String password = "Abcde123!";
        String nickName = "테스트";
        String phone = "010-1234-5678";

        SignUpRequestDto requestDto = new SignUpRequestDto(email, password, nickName, phone);
        SignUpResponseDto responseDto = new SignUpResponseDto(1L, email, nickName, phone, UserRole.ADMIN, Grade.BASIC);

        given(authService.save(any(SignUpRequestDto.class), any(UserRole.class)))
                .willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."))
                .andExpect(jsonPath("$.data.nickname").value("테스트"))
                .andExpect(jsonPath("$.data.role").value("ADMIN"));

    }

    @Test
    @DisplayName("비밀번호 형식이 잘못되었을 때 회원가입 실패")
    void signUp_x_PasswordInvalid() throws Exception {
        // given
        String email = "test@example.com";
        String password = "1234";
        String nickName = "테스트";
        String phone = "010-1234-5678";
        SignUpRequestDto requestDto = new SignUpRequestDto(email, password, nickName, phone);
        SignUpResponseDto responseDto = new SignUpResponseDto(1L, email, nickName, phone, UserRole.USER, Grade.BASIC);

        given(authService.save(any(SignUpRequestDto.class), any(UserRole.class)))
                .willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("비밀번호는 8자 이상, 대소문자/숫자/특수문자를 각각 1자 이상 포함해야 합니다."));
    }

}
