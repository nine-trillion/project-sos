package team.project.sos.domain.auth.dto.response;

import lombok.Getter;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;

import java.time.LocalDateTime;

@Getter
public class SignUpResponseDto {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final String phoneNumber;
    private final UserRole role;
    private final Grade grade;
    private final LocalDateTime createdAt;

    public SignUpResponseDto(Long userId,
                             String email,
                             String nickname,
                             String phoneNumber,
                             UserRole role,
                             Grade grade,
                             LocalDateTime createdAt) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.grade = grade;
        this.createdAt = createdAt;
    }

    public SignUpResponseDto(Long userId,
                             String email,
                             String nickname,
                             String phoneNumber,
                             UserRole role,
                             Grade grade) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.grade = grade;
        this.createdAt = LocalDateTime.now();

    }

    public static SignUpResponseDto from(User user) {
        return new SignUpResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getGrade(),
                user.getCreatedAt()
        );
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