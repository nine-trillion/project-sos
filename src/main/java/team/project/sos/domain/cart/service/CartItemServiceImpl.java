package team.project.sos.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.project.sos.domain.cart.entity.CartItem;
import team.project.sos.domain.cart.exception.CartItemError;
import team.project.sos.domain.cart.exception.CartItemException;
import team.project.sos.domain.cart.repository.CartItemRepository;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItem findByIdOrElseThrow(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemException(CartItemError.ITEM_NOT_FOUND));
    }

}