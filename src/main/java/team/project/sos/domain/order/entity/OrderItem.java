package team.project.sos.domain.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.project.sos.common.config.BaseTimeEntity;
import team.project.sos.domain.menu.entity.Menu;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id") // 외래키 컬럼 지정
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id") // 외래키 컬럼 지정
    private Menu menu;

    // TODO: MenuOption 추가

    private int quantity;

}