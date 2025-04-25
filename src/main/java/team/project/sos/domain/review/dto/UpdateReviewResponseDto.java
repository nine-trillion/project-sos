package team.project.sos.domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import team.project.sos.domain.review.entity.Review;

import java.time.LocalDateTime;

@Builder
@Getter
public class UpdateReviewResponseDto {

    private final String content;

    private final int rating;

    private final LocalDateTime updatedAt;

    public static UpdateReviewResponseDto of(Review review) {
        return UpdateReviewResponseDto.builder()
                .content(review.getContent())
                .rating(review.getRating())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
