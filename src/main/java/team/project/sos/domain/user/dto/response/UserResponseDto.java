package team.project.sos.domain.user.dto.response;

import lombok.Getter;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final String phoneNumber;
    private final UserRole role;
    private final Grade grade;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserResponseDto(Long userId,
                           String email,
                           String nickname,
                           String phoneNumber,
                           UserRole role,
                           Grade grade,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.grade = grade;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getGrade(),
                user.getCreatedAt(),
                user.getUpdatedAt()
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