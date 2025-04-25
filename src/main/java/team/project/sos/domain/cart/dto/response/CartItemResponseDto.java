package team.project.sos.domain.cart.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.project.sos.domain.cart.entity.CartItem;

@Getter
@Builder
public class CartItemResponseDto {

    private Long cartId;
    private Long menuId;
    private int quantity;

    // 정적 팩토리 메서드
    public static CartItemResponseDto from(CartItem cartItem) {
        return CartItemResponseDto.builder()
                .cartId(cartItem.getCart().getId())
                .menuId(cartItem.getMenu().getId())
                .quantity(cartItem.getQuantity())
                .build();
    }

}