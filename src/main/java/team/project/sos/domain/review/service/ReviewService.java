package team.project.sos.domain.review.service;

import team.project.sos.domain.review.dto.CreateReviewRequestDto;
import team.project.sos.domain.review.dto.CreateReviewResponseDto;
import team.project.sos.domain.review.dto.UpdateReviewResponseDto;

import java.util.List;

public interface ReviewService  {
    CreateReviewResponseDto saveReview(Long orderId,Long userId, CreateReviewRequestDto createReviewRequestDto);
    List<CreateReviewResponseDto> findAllReviews(Long storeId);
    UpdateReviewResponseDto updateReview(Long orderId, String newContent, int newRating, Long user);
    void removeReview(Long user, Long orderId);

}
