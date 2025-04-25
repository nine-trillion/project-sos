package team.project.sos.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.project.sos.domain.review.dto.CreateReviewRequestDto;
import team.project.sos.domain.review.dto.CreateReviewResponseDto;
import team.project.sos.domain.review.dto.UpdateReviewRequestDto;
import team.project.sos.domain.review.dto.UpdateReviewResponseDto;
import team.project.sos.domain.review.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews/{orderId}")
    public ResponseEntity<CreateReviewResponseDto> saveReview(@RequestBody CreateReviewRequestDto dto,
                                                              @PathVariable Long orderId,
                                                              @RequestAttribute("userId") Long userId) {
        CreateReviewResponseDto response = reviewService.saveReview(orderId, userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<List<CreateReviewResponseDto>> findAllReviews(@PathVariable Long storeId) {

        List<CreateReviewResponseDto> response = reviewService.findAllReviews(storeId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/reviews")
    public ResponseEntity<UpdateReviewResponseDto> updateReviews(
            @PathVariable Long orderId,
            @RequestAttribute("userId") Long userId,
            @RequestBody @Valid UpdateReviewRequestDto dto) {
        UpdateReviewResponseDto response = reviewService.updateReview(orderId, dto.getContent(), dto.getRating(), userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reviews/{orderId}")
    public ResponseEntity<String> deleteReviews(@PathVariable Long orderId, @RequestAttribute("userId") Long userId){
        reviewService.removeReview(userId,orderId);

        return ResponseEntity.noContent().build();
    }

}
