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

    //댓글 다 , 유저 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //한 개의 가게에 리뷰 여러개
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    //주문 한번에 리뷰 1개
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String content;

    //도메인 객체의 상태를 바꾸는 책임은 도메인 자신이 갖는게 자연스럽다.
    public void updateReview(String newContent, int newRating) {
        this.content = newContent;
        this.rating = newRating;
    }
}