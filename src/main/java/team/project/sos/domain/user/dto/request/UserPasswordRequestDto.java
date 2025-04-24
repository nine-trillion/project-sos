package team.project.sos.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserPasswordRequestDto {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private final String password;

    public UserPasswordRequestDto(String password) {
        this.password = password;
    }
}
