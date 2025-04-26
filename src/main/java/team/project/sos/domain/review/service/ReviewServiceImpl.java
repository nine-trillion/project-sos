package team.project.sos.domain.review.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.project.sos.domain.order.entity.Order;
import team.project.sos.domain.order.enums.OrderStatus;
import team.project.sos.domain.order.service.OrderService;
import team.project.sos.domain.review.dto.CreateReviewRequestDto;
import team.project.sos.domain.review.dto.CreateReviewResponseDto;
import team.project.sos.domain.review.dto.UpdateReviewResponseDto;
import team.project.sos.domain.review.entity.Review;
import team.project.sos.domain.review.exception.ReviewError;
import team.project.sos.domain.review.exception.ReviewException;
import team.project.sos.domain.review.repository.ReviewRepository;
import team.project.sos.domain.store.service.StoreService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final OrderService orderService;

    private final StoreService storeService;


    @Transactional
    public CreateReviewResponseDto saveReview(Long orderId, Long loggedInUserId, CreateReviewRequestDto createReviewRequestDto) {
        // 주문 ID로 주문 조회
        Order order = orderService.findByIdOrElseThrow(orderId);

        //로그인한 유저Id와 일치하는지
        if (!order.getUser().getId().equals(loggedInUserId)) {
            throw new ReviewException(ReviewError.UNAUTHORIZED_REVIEW_ACCESS);
        }

        //주문 상태 확인 (배송 완료인지)
        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new ReviewException(ReviewError.ORDER_NOT_COMPLETED);
        }

        //배송이 5일 지나면 리뷰 작성이 불가능.
        if (order.getRequestedAt().isBefore(LocalDateTime.now().minusDays(5))) {
            throw new ReviewException(ReviewError.ORDER_NOT_COMPLETED);
        }

        //리뷰에 필요한 필드만.
        Review review = Review.builder()
                .content(createReviewRequestDto.getContent())
                .rating(createReviewRequestDto.getRating())
                .order(order)
                .store(order.getStore())
                .build();

        // Review 정적 팩토리 메서드로 생성
        reviewRepository.save(review);
        return CreateReviewResponseDto.from(review);
    }

    public List<CreateReviewResponseDto> findReviewsByRating(Long storeId, int rating) {
        storeService.findStoreByIdOrElseThrow(storeId);

        return reviewRepository.findAllByOrderStoreIdAndRatingOrderByCreatedAtDesc(storeId, rating)
                .stream()
                .map(CreateReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public UpdateReviewResponseDto updateReview(Long orderId, Long loggedInUserId, String newContent, int newRating) {

        Order order = orderService.findByIdOrElseThrow(orderId);

        //로그인한 유저Id와 일치하는지
        if (!order.getUser().getId().equals(loggedInUserId)) {
            throw new ReviewException(ReviewError.UNAUTHORIZED_REVIEW_ACCESS);
        }

        Review review = reviewRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ReviewException(ReviewError.REVIEW_NOT_FOUND));

        //리뷰 엔티티에 메서드 하나 만들어서 수정반영 되도록. update.
        review.updateReview(newContent, newRating);
        return UpdateReviewResponseDto.of(review);
    }

    public void removeReview(Long orderId, Long loggedInUserId) {

        Order order = orderService.findByIdOrElseThrow(orderId);

        if (!order.getUser().getId().equals(loggedInUserId)) {
            throw new ReviewException(ReviewError.UNAUTHORIZED_REVIEW_ACCESS);
        }
        //리뷰가 없어요
        Review review = reviewRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ReviewException(ReviewError.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);
    }

}

