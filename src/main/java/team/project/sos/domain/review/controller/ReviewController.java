package team.project.sos.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team.project.sos.domain.review.dto.CreateReviewRequestDto;
import team.project.sos.domain.review.dto.CreateReviewResponseDto;
import team.project.sos.domain.review.dto.UpdateReviewRequestDto;
import team.project.sos.domain.review.service.ReviewService;
import team.project.sos.domain.user.entity.User;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/api/stores/{storeId}/reviews/{orderId}")
    public ResponseEntity<CreateReviewResponseDto> saveReview(@RequestBody CreateReviewRequestDto dto, @PathVariable Long orderId, @PathVariable Long storeId,
                                                              @AuthenticationPrincipal(expression = "user") User user) {
        CreateReviewResponseDto response =
                reviewService.saveReview(orderId, storeId, user, dto);

       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("stores/{storeId}/reviews")
    public ResponseEntity<List<CreateReviewResponseDto>> findAllReviews(@PathVariable Long storeId) {

        List<CreateReviewResponseDto> response = reviewService.findAllReviews(storeId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("{orderId}/reviews")
    public ResponseEntity<CreateReviewResponseDto> updateReviews(@PathVariable Long orderId,
                                                                 @AuthenticationPrincipal(expression = "user") User user,
                                                                 @RequestBody @Valid UpdateReviewRequestDto dto) {
        CreateReviewResponseDto response =
                reviewService.updateReview(orderId,dto.getNewContent(),user);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("reviews/{orderId}")
    public ResponseEntity<String> deleteReviews(@PathVariable Long orderId,@AuthenticationPrincipal(expression = "user") User user) {
        reviewService.removeReview(user,orderId);

        return ResponseEntity.noContent().build();
    }

}
