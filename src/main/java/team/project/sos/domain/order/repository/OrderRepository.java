package team.project.sos.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.project.sos.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
