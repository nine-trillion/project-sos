package team.project.sos.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.project.sos.domain.menu.entity.Menu;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {

    @NotNull
    private Menu menu;

    // TODO: MenuOption 추가

    @NotNull
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private int quantity;

}