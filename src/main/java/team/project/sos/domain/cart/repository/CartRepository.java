package team.project.sos.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.project.sos.domain.cart.entity.Cart;
import team.project.sos.domain.user.entity.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

}