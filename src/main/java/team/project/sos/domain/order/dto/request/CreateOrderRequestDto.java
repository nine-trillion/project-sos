package team.project.sos.domain.order.dto.request;

import lombok.*;
import team.project.sos.domain.menu.entity.Menu;
import team.project.sos.domain.order.enums.OrderStatus;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {

    private User user;
    private Store store;
    private Menu menu;
    private OrderStatus status;
    private int price;
    private LocalDateTime requestedAt;

}