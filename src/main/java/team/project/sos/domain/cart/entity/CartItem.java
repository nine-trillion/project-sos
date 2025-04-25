package team.project.sos.domain.cart.entity;

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
@Table(name = "cart_item")
public class CartItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id") // 외래키 컬럼 지정
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "menu_id") // 외래키 컬럼 지정
    private Menu menu;

    private int quantity;

    // TODO: MenuOption 추가 ?

    public void update(int quantity) {
        this.quantity = quantity;
    }

}