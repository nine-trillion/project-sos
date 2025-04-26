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
import team.project.sos.domain.store.exception.StoreException;
import team.project.sos.domain.store.service.StoreService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 리뷰 관련 비즈니스 로직을 담당하는 서비스 클래스입니다.
 * <p>
 * 리뷰 생성, 조회, 수정, 삭제 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final OrderService orderService;

    private final StoreService storeService;

    /**
     * 주문에 대한 리뷰 생성
     *
     * @param orderId 주문 ID
     * @param loggedInUserId 로그인한 사용자 ID
     * @param createReviewRequestDto 리뷰 작성 요청 DTO
     * @return 생성된 리뷰 응답 DTO
     * @throws ReviewException 권한 없음, 주문 완료되지 않음 등의 경우
     */
    @Transactional
    public CreateReviewResponseDto saveReview(Long orderId, Long loggedInUserId, CreateReviewRequestDto createReviewRequestDto) {

        Order order = orderService.findByIdOrElseThrow(orderId);

        if (!order.getUser().getId().equals(loggedInUserId)) {
            throw new ReviewException(ReviewError.UNAUTHORIZED_REVIEW_ACCESS);
        }

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new ReviewException(ReviewError.ORDER_NOT_COMPLETED);
        }

        if (order.getRequestedAt().isBefore(LocalDateTime.now().minusDays(5))) {
            throw new ReviewException(ReviewError.ORDER_NOT_COMPLETED);
        }

        Review review = Review.builder()
                .content(createReviewRequestDto.getContent())
                .rating(createReviewRequestDto.getRating())
                .order(order)
                .store(order.getStore())
                .build();

        reviewRepository.save(review);
        return CreateReviewResponseDto.from(review);
    }

    /**
     * 특정 가게의 평점별 리뷰 조회
     *
     * @param storeId 가게 ID
     * @param rating 평점 (1~5)
     * @return 평점에 해당하는 리뷰 리스트
     * @throws StoreException 가게가 존재하지 않는 경우
     */
    public List<CreateReviewResponseDto> findReviewsByRating(Long storeId, int rating) {
        storeService.findStoreByIdOrElseThrow(storeId);

        return reviewRepository.findAllByOrderStoreIdAndRatingOrderByCreatedAtDesc(storeId, rating)
                .stream()
                .map(CreateReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 주문에 대한 리뷰 수정
     *
     * @param orderId 주문 ID
     * @param loggedInUserId 로그인한 사용자 ID
     * @param newContent 새로운 리뷰 내용
     * @param newRating 새로운 평점
     * @return 수정된 리뷰 응답 DTO
     * @throws ReviewException 권한 없음, 리뷰 없음
     */
    @Transactional
    public UpdateReviewResponseDto updateReview(Long orderId, Long loggedInUserId, String newContent, int newRating) {

        Order order = orderService.findByIdOrElseThrow(orderId);

        if (!order.getUser().getId().equals(loggedInUserId)) {
            throw new ReviewException(ReviewError.UNAUTHORIZED_REVIEW_ACCESS);
        }

        Review review = reviewRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ReviewException(ReviewError.REVIEW_NOT_FOUND));

        review.updateReview(newContent, newRating);
        return UpdateReviewResponseDto.of(review);
    }

    /**
     * 주문에 대한 리뷰 삭제
     *
     * @param orderId 주문 ID
     * @param loggedInUserId 로그인한 사용자 ID
     * @throws ReviewException 권한 없음, 리뷰 없음
     */
    public void removeReview(Long orderId, Long loggedInUserId) {

        Order order = orderService.findByIdOrElseThrow(orderId);

        if (!order.getUser().getId().equals(loggedInUserId)) {
            throw new ReviewException(ReviewError.UNAUTHORIZED_REVIEW_ACCESS);
        }

        Review review = reviewRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ReviewException(ReviewError.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);
    }
}

