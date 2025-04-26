package team.project.sos.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class FindPasswordResponseDto {
    private String password;

    public FindPasswordResponseDto(String tempPassword) {
        this.password = tempPassword;
    }
}
