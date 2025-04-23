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
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private int price;

    @CreationTimestamp
    private LocalDateTime requestedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * INSERT 전에 자동 실행되는 메서드입니다.
     * 주문 상태 기본값을 PENDING으로 설정합니다.
     */
    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
    }

    /**
     * 주문을 취소하기 위해 서비스에서 호출하는 메서드입니다.
     */
    public void cancel() {
        // 이미 취소된 주문인 경우 예외 발생
        if (this.status == OrderStatus.CANCELLED) {
            throw new OrderException(OrderError.ALREADY_CANCELLED);
        }

        // 이미 조리가 시작되거나 완료된 주문인 경우 예외 발생
        if (this.status == OrderStatus.COOKING || this.status == OrderStatus.COMPLETED) {
            throw new OrderException(OrderError.ALREADY_COOKING);
        }

        this.status = OrderStatus.CANCELLED;
    }

}