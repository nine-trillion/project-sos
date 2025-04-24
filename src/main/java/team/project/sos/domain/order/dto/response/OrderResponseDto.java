package team.project.sos.domain.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.project.sos.domain.order.entity.Order;
import team.project.sos.domain.order.enums.OrderStatus;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponseDto {

    private Long id;
    private Long userId;
    private Long storeId;
    private OrderStatus status;
    private int price;
    private LocalDateTime requestedAt;

    // 정적 팩토리 메서드
    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .storeId(order.getStore().getId())
                .status(order.getStatus())
                .price(order.getPrice())
                .requestedAt(order.getRequestedAt())
                .build();
    }

}