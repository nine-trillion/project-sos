package team.project.sos.domain.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.project.sos.domain.cart.entity.CartItem;

@Getter
@Builder
@AllArgsConstructor
public class CartItemRequestDto {

    @NotNull
    private Long cartId;

    @NotNull
    private Long menuId;

    @NotNull
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private int quantity;

    // 정적 팩토리 메서드
    public static CartItemRequestDto from(CartItem cartItem) {
        return CartItemRequestDto.builder()
                .cartId(cartItem.getCart().getId())
                .menuId(cartItem.getMenu().getId())
                .quantity(cartItem.getQuantity())
                .build();
    }

}