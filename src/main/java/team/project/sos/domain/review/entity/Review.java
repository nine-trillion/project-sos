package team.project.sos.domain.review.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import team.project.sos.common.config.BaseTimeEntity;
import team.project.sos.domain.order.entity.Order;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.user.entity.User;

@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Getter
@Table(name = "review")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //댓글 다 , 유저 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //한 개의 가게에 리뷰 여러개
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Min(1)
    @Max(5)
    private int rating;

    @Column(nullable = false)
    private String content;


    public void updateReview(String newContent) {
        this.content = newContent;
    }
}