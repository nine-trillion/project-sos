package team.project.sos.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.project.sos.domain.order.entity.Order;
import team.project.sos.domain.user.entity.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
