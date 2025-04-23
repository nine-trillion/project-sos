package team.project.sos.domain.auth.dto.response;

import lombok.Getter;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;

import java.time.LocalDateTime;

@Getter
public class SignUpResponseDto {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final UserRole role;
    private final Grade grade;
    private final LocalDateTime createdAt;

    public SignUpResponseDto(Long userId, String email, String nickname, UserRole role, Grade grade, LocalDateTime createdAt) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.grade = grade;
        this.createdAt = createdAt;
    }
}
/**
 * {
 * "message": "회원가입이 완료되었습니다.",
 * "data": {
 * "user_id": 42,
 * "email": "example@domain.com",
 * "nickname": "김르탄",
 * "role": "USER",
 * "grade": "일반",
 * “created_at” :”…”
 * }
 * }
 */