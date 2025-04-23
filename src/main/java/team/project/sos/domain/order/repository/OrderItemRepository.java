package team.project.sos.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.project.sos.domain.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}