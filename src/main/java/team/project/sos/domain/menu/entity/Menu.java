package team.project.sos.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long storeId;

    private String name;

    private int price;

    private String category;

    private boolean isDeleted = false;

    public Menu(Long storeId, String name, int price, String category) {
        this.storeId = storeId;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public void update(String name, int price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public void delete() {
        this.isDeleted = true;
    }
}