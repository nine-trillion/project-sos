package team.project.sos.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.project.sos.common.config.BaseTimeEntity;
import team.project.sos.domain.auth.dto.request.SignUpRequestDto;
import team.project.sos.domain.user.enums.Grade;
import team.project.sos.domain.user.enums.UserRole;

@Entity
@Table(name = "User")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public User(String email, String password, String nickname, String phoneNumber, UserRole role, Grade grade) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.grade = grade;
    }

    public static User createUser(SignUpRequestDto requestDto, UserRole userRole, String password, Grade grade) {
        return new User(
                requestDto.getEmail(),
                password,
                requestDto.getNickname(),
                requestDto.getPhoneNumber(),
                userRole,
                grade
        );
    }

    public void update(String nickname, String phoneNumber) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

}
