package team.project.sos.domain.cart.service;

import team.project.sos.domain.cart.entity.CartItem;

public interface CartItemService {

    CartItem findByIdOrElseThrow(Long cartItemId);

}