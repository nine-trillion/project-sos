
package team.project.sos.domain.menu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateMenuRequestDto {

    @NotNull(message = "가게 ID를 입력하세요.")
    private Long storeId;

    @NotBlank(message = "메뉴 이름을 입력하세요.")
    private String name;

    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private int price;

    @NotBlank(message = "카테고리를 입력하세요.")
    private String category;

    // TODO: [임시] 권한 체크용 필드, 추후 삭제 예정
    private String role;
    
}