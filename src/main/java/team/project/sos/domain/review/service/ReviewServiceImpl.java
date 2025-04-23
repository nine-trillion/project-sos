package team.project.sos.domain.review.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.project.sos.domain.order.repository.OrderRepository;
import team.project.sos.domain.review.dto.CreateReviewRequestDto;
import team.project.sos.domain.review.dto.CreateReviewResponseDto;
import team.project.sos.domain.review.entity.Review;
import team.project.sos.domain.review.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewServiceImpl {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public CreateReviewResponseDto saveReview(CreateReviewRequestDto createReviewRequestDto) {
        // 주문 ID로 주문 조회
        // 로그인한 유저와 주문자 동일 여부 확인
        // 주문 상태 확인 (배송 완료인지)
        // Review 정적 팩토리 메서드로 생성

        return null;
    }

    @Transactional
    public List<CreateReviewResponseDto> findAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        //반환stream으로

        return null;
    }

    @Transactional
    public void updateReview(Long reviewId) {
        //수정권한 예외처리
        //리뷰가 없으면 없다고예외
    }

    public void deleteReview(Long reviewId) {
        //삭제 권한 잇는지.
        //리뷰없으면 없다고.

    }
    }

