package team.project.sos.domain.cart.service;

import team.project.sos.domain.cart.dto.request.CartItemRequestDto;
import team.project.sos.domain.cart.dto.request.UpdateCartItemRequestDto;
import team.project.sos.domain.cart.dto.response.CartItemResponseDto;

import java.util.List;

public interface CartService {

    List<CartItemResponseDto> saveCartItems(List<CartItemRequestDto> requestDtos, Long currentUserId);

    List<CartItemResponseDto> findCartItems(Long userId);

    CartItemResponseDto updateCartItem(Long cartItemId,
                                       UpdateCartItemRequestDto requestDto,
                                       Long currentUserId);

    void deleteCartItem(Long cartItemId, Long currentUserId);

}