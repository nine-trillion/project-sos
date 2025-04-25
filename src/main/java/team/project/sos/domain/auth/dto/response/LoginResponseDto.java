package team.project.sos.domain.auth.dto.response;

import lombok.Getter;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;

@Getter
public class LoginResponseDto {
    private final Long userId;
    private final String email;
    private final String nickName;
    private final UserRole role;
    private final Grade grade;
    private final String token;

    public LoginResponseDto(Long userId, String email, String nickName, UserRole role, Grade grade, String token) {
        this.userId = userId;
        this.email = email;
        this.nickName = nickName;
        this.role = role;
        this.grade = grade;
        this.token = token;
    }

    public static LoginResponseDto from(User user, String token) {
        return new LoginResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                user.getGrade(),
                token
        );
    }
}
