package team.project.sos.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class FindPasswordRequestDto {

    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(
            regexp = "^01[016789]-?[0-9]{3,4}-?[0-9]{4}$",
            message = "유효하지 않은 전화번호 형식입니다. 예: 010-1234-5678 또는 01012345678"
    )
    private String phoneNumber;

    public FindPasswordRequestDto(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
