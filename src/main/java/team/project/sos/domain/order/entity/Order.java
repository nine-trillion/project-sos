package team.project.sos.domain.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import team.project.sos.domain.menu.entity.Menu;
import team.project.sos.domain.order.enums.OrderStatus;
import team.project.sos.domain.order.exception.OrderError;
import team.project.sos.domain.order.exception.OrderException;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // 외래키 컬럼 지정
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id") // 외래키 컬럼 지정
    private Store store;

    @ManyToOne
    @JoinColumn(name = "menu_id") // 외래키 컬럼 지정
    private Menu menu;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private int price;

    private LocalDateTime requestedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void cancel() {
        // 이미 취소된 주문
        if (this.status == OrderStatus.CANCELLED) {
            throw new OrderException(OrderError.ALREADY_CANCELLED);
        }

        // 이미 조리가 시작되거나 완료된 주문
        if (this.status == OrderStatus.COOKING || this.status == OrderStatus.COMPLETED) {
            throw new OrderException(OrderError.ALREADY_COOKING);
        }

        this.status = OrderStatus.CANCELLED;
    }

}