package team.project.sos.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long storeId;

    @NotNull
    private List<OrderItemRequestDto> items;

    @NotNull
    @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
    private int price;

    @NotNull
    private LocalDateTime requestedAt;

}