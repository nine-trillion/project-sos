package team.project.sos.domain.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreUpdateRequest {

    @NotBlank(message = "가게 명은 필수 입력 값입니다.")
    @Size(max = 30, message = "가게 명은 30자를 초과할 수 없습니다.")
    private String name;

    @NotNull(message = "영업 시작 시간은 필수 입력 값입니다.")
    private LocalTime openTime;

    @NotNull(message = "영업 마감 시간은 필수 입력 값입니다.")
    private LocalTime closeTime;

    @NotNull(message = "최소 주문 금액은 필수 입력 값입니다.")
    private int minOrderPrice;

    @NotNull(message = "공지사항은 필수 항목입니다.") // 빈 문자열 허용
    @Size(max = 1000, message = "공지사항은 1000자를 초과할 수 없습니다.")
    private String notice;
}
