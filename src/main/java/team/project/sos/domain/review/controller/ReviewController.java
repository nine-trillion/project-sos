package team.project.sos.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.project.sos.domain.review.service.ReviewServiceImpl;


//return ResponseEntity.status(HttpStatus.CREATED).body(response); // 생성
//return ResponseEntity.ok(response); // 조회 및 수정
//return ResponseEntity.noContent().build(); // 삭제

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewServiceImpl reviewService;
//
//    @PostMapping("reviews/{orderId}")
//    ResponseEntity<CreateReviewResponseDto> saveReview(@RequestBody CreateReviewRequestDto createReviewRequestDto, @PathVariable Long orderId) {
//        CreateReviewResponseDto createReviewResponseDto =
//                reviewService.saveReview(user,createReviewRequestDto);
//
//       return ResponseEntity.status(HttpStatus.CREATED).body(createReviewResponseDto);
//    }
//
//    @GetMapping("stores/{storeId}/reviews")
//    ResponseEntity<List<CreateReviewResponseDto>> findAllReviews(@PathVariable Long storeId) {
//        List<CreateReviewResponseDto> responseList = reviewService.findAllReviews(storeId);
//
//        return ResponseEntity.ok(responseList);
//    }
//
//    @PutMapping("{orderId}/reviews")
//    ResponseEntity<CreateReviewResponseDto> updateReviews(@PathVariable Long orderId) {
//        CreateReviewResponseDto createReviewResponseDto =
//                reviewService.updateReview(reviewId, wishContent);
//
//        return ResponseEntity.ok(createReviewResponseDto);
//    }
//
//    @DeleteMapping("reviews/{orderId}")
//    ResponseEntity<String> deleteReviews(@PathVariable Long orderId) {
//        reviewService.removeReview(user,reivewId);
//
//        return ResponseEntity.noContent().build();
//    }

}
