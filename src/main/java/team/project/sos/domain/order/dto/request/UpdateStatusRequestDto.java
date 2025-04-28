package team.project.sos.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import team.project.sos.domain.order.enums.OrderStatus;

@Getter
public class UpdateStatusRequestDto {

    @NotNull(message = "변경할 주문 상태를 입력하세요.")
    @Pattern(
            regexp = "^(COOKING|DELIVERING|COMPLETED|CANCELLED)$",
            message = "변경할 주문 상태는 COOKING, DELIVERING, COMPLETED, CANCELLED 중 하나여야 합니다."
    )
    private OrderStatus status;
}
