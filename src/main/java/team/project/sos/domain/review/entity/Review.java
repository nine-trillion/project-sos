package team.project.sos.domain.review.entity;


import jakarta.persistence.*;
import lombok.*;
import team.project.sos.common.config.BaseTimeEntity;
import team.project.sos.domain.order.entity.Order;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.user.entity.User;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Table(name = "review")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String content;

    public void updateReview(String newContent, int newRating) {
        this.content = newContent;
        this.rating = newRating;
    }
}