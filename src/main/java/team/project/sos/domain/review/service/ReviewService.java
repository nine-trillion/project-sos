package team.project.sos.domain.review.service;

import team.project.sos.domain.review.dto.CreateReviewRequestDto;
import team.project.sos.domain.review.dto.CreateReviewResponseDto;
import team.project.sos.domain.review.dto.UpdateReviewResponseDto;

import java.util.List;

public interface ReviewService  {
    CreateReviewResponseDto saveReview(Long orderId, Long loggedInUserId, CreateReviewRequestDto createReviewRequestDto);
    List<CreateReviewResponseDto> findReviewsByRating(Long storeId, int rating);
    UpdateReviewResponseDto updateReview(Long orderId, Long loggedInUserId, String newContent, int newRating);
    void removeReview(Long orderId,Long loggedInUserId);

}
