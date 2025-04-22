package team.project.sos.domain.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpRequestDto {

    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{}|;':\",.<>/?]).{8,}$",
            message = "비밀번호는 8자 이상, 대소문자/숫자/특수문자를 각각 1자 이상 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값 입니다.")
    private String nickname;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(
            regexp = "^01[016789]-?[0-9]{3,4}-?[0-9]{4}$",
            message = "유효하지 않은 전화번호 형식입니다. 예: 010-1234-5678 또는 01012345678"
    )
    private String phoneNumber;

    public UserSignUpRequestDto(String email, String password, String nickname, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
}
