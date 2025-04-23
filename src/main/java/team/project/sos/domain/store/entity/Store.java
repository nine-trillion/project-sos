package team.project.sos.domain.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import team.project.sos.common.config.BaseTimeEntity;
import team.project.sos.domain.store.enums.StoreStatus;
import team.project.sos.domain.user.entity.User;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "store")
@SQLDelete(sql = "update store set status = 'CLOSED' where id = ?") // 가게 삭제 시 폐업 처리
@SQLRestriction("status = 'OPERATING'") // 영업 중인 가게만 조회
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    @Column(name = "min_order_price", nullable = false)
    private int minOrderPrice = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status;

    @Column(nullable = false)
    private String notice;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public void updateStoreInfo(String name, LocalTime openTime, LocalTime closeTime, String notice) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.notice = notice;
    }

    public Store(String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice, User owner) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
        this.owner = owner;
        this.status = StoreStatus.OPERATING;
        this.notice = "";
    }
}
