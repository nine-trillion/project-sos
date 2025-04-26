package team.project.sos.domain.review.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import team.project.sos.domain.order.entity.Order;
import team.project.sos.domain.order.enums.OrderStatus;
import team.project.sos.domain.order.service.OrderService;
import team.project.sos.domain.review.dto.CreateReviewRequestDto;
import team.project.sos.domain.review.dto.CreateReviewResponseDto;
import team.project.sos.domain.review.entity.Review;
import team.project.sos.domain.review.exception.ReviewException;
import team.project.sos.domain.review.repository.ReviewRepository;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.store.service.StoreService;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private StoreService storeService;

    @Mock
    private UserService userService;

    private Store createMockStore() {
        Store store = new Store();
        //값 세팅 객체/필드 이름/세팅 값
        ReflectionTestUtils.setField(store, "id", 1L);
        ReflectionTestUtils.setField(store, "name", "Sos");
        return store;
    }
    private User createMockUser() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "nickname", "Sos");
        ReflectionTestUtils.setField(user, "email", "sos@example.com");
        return user;
    }

    private Order createMockOrder(User user, Store store, OrderStatus status, int price, LocalDateTime requestedAt) {
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(order, "user", user);
        ReflectionTestUtils.setField(order, "store", store);
        ReflectionTestUtils.setField(order, "status", status);
        ReflectionTestUtils.setField(order, "price", price);
        ReflectionTestUtils.setField(order, "requestedAt", requestedAt);
        return order;
    }

    private Review createMockReview(User user, Store store, Order order) {
        Review review = Review.builder()
                .content("테스트 리뷰 내용")
                .rating(5)
                .order(order)
                .store(store)
                .build();
        ReflectionTestUtils.setField(review, "order", order);
        ReflectionTestUtils.setField(review, "user", order.getUser());
        ReflectionTestUtils.setField(review, "id", 1L);
        ReflectionTestUtils.setField(review, "user", user);
        return review;
    }

    //로그인한 유저를 찾기위해 필요.
    private User createMockUserWithId(Long id) {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", id);
        ReflectionTestUtils.setField(user, "nickname", "Sos");
        ReflectionTestUtils.setField(user, "email", "sos@example.com");
        return user;
    }

    private Order createMockOrderWithId(Long id) {
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", id); // id 필드에 값 세팅
        return order;
    }

    @Test
    @DisplayName("리뷰 생성 실패 예외처리")
    void 리뷰_생성_실패_배송완료_후_5일_초과() {
        // given 주문 상태가 completed
        User user = createMockUser();
        Store store = createMockStore();
        Order order = createMockOrder(
                user,
                store,
                OrderStatus.COMPLETED,
                15000,
                LocalDateTime.now().minusDays(6) // 2일 전 주문
        );

        CreateReviewRequestDto dto = new CreateReviewRequestDto("맛있다.", 5);

        // when
        when(orderService. findByIdOrElseThrow(anyLong()))
                .thenReturn(order);

        // then: review객체 반환
        assertThrows(ReviewException.class, () -> {
            reviewService.saveReview(order.getId(), user.getId(), dto);
        });
    }

    @Test
    @DisplayName("리뷰 생성 성공")
    void 리뷰_생성_성공() {
        User user = createMockUser();
        Store store = createMockStore();
        Order order = createMockOrder(
                user,
                store,
                OrderStatus.COMPLETED,
                15000,
                LocalDateTime.now().minusDays(3) // 2일 전 주문
        );
        CreateReviewRequestDto dto = new CreateReviewRequestDto("맛있다.", 5);
        when(orderService. findByIdOrElseThrow(anyLong()))
                .thenReturn(order);

        //when
        CreateReviewResponseDto result = reviewService.saveReview(order.getId(), user.getId(), dto);

        //then
        assertNotNull(result);
        assertEquals(dto.getContent(), result.getContent());
    }

    @Test
    @DisplayName("리뷰 조회 성공")
    void 리뷰_조회_성공() {
        int rating = 3;

        Store store = createMockStore();
        User user = createMockUser();
        Order order = createMockOrder(
                user,
                store,
                OrderStatus.COMPLETED,
                15000,
                LocalDateTime.now().minusDays(3) // 2일 전 주문
        );

        //재료를 생각한다고 보면 편함. 테스트 하기 전에, 테스트에서 반환될 건 뭘까..
        Review review = createMockReview(user,store,order);
        Review review2 = createMockReview(user,store,order);
        List<Review> mockReviews = List.of(review, review2);

        when(storeService.findStoreByIdOrElseThrow(store.getId()))
                .thenReturn(store);

        //mock동작과정
        when(reviewRepository.findAllByOrderStoreIdAndRatingOrderByCreatedAtDesc(store.getId(),rating))
                .thenReturn(mockReviews);
        //when
        List<CreateReviewResponseDto> result = reviewService.findReviewsByRating(store.getId(), rating);

        //then
        assertNotNull(result);
        assertEquals(mockReviews.size(), result.size());

    }

    @Test
    @DisplayName("리뷰 수정 성공")
    void 리뷰_수정_성공() {

        //given
        User owner = createMockUserWithId(1L);
        Store store = createMockStore();
        Order order = createMockOrder(owner, store, OrderStatus.COMPLETED, 15000, LocalDateTime.now());
        Review review = createMockReview(owner, store, order);

        when(orderService.findByIdOrElseThrow(anyLong())).thenReturn(order);

        when(reviewRepository.findByOrderId(anyLong())).thenReturn(Optional.of(review));

        var result = reviewService.updateReview(order.getId(), owner.getId(), "맛없음", 5);

        //then
        assertNotNull(result);
        assertEquals("맛없음", result.getContent());
        assertEquals(5, review.getRating());

    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void 리뷰_삭제_성공() {
        //given
        User owner = createMockUserWithId(1L);
        Store store = createMockStore();
        Order order = createMockOrder(owner, store, OrderStatus.COMPLETED, 15000, LocalDateTime.now());
        Review review = createMockReview(owner, store, order);

        when(orderService.findByIdOrElseThrow(anyLong())).thenReturn(order);

        when(reviewRepository.findByOrderId(anyLong())).thenReturn(Optional.of(review));

        reviewService.removeReview(order.getId(), owner.getId());


    }

}