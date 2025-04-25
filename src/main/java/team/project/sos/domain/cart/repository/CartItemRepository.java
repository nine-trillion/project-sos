package team.project.sos.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.project.sos.domain.cart.entity.Cart;
import team.project.sos.domain.cart.entity.CartItem;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

}