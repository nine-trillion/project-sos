package team.project.sos.domain.review.service;

import team.project.sos.domain.review.dto.CreateReviewRequestDto;
import team.project.sos.domain.review.dto.CreateReviewResponseDto;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.user.entity.User;

import java.util.List;

public interface ReviewService  {
    CreateReviewResponseDto saveReview(User user, CreateReviewRequestDto createReviewRequestDto);
    List<CreateReviewResponseDto> findAllReviews(Long storeId);
    CreateReviewResponseDto updateReview(Long reviewId, String wishContent, User user);
    void removeReview(User user, Long reviewId, Store store);


}
