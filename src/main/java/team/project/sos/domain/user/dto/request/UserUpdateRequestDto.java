package team.project.sos.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    private String nickName;

    @Pattern(
            regexp = "^01[016789]-?[0-9]{3,4}-?[0-9]{4}$",
            message = "유효하지 않은 전화번호 형식입니다. 예: 010-1234-5678 또는 01012345678"
    )
    private String phoneNumber;

//    public UserUpdateRequestDto(String nickName, String phoneNumber) {
//        this.nickName = nickName;
//        this.phoneNumber = phoneNumber;
//    }
}
