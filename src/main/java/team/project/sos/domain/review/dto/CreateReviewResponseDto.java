package team.project.sos.domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import team.project.sos.domain.review.entity.Review;

import java.time.LocalDateTime;

@Builder
@Getter
public class CreateReviewResponseDto {

    private final String content;

    private final int rating;

    private final Long storeId;

    private final LocalDateTime createdAt;

    public static CreateReviewResponseDto from(Review review) {
            return CreateReviewResponseDto.builder()
                    .storeId(review.getStore().getId())
                    .content(review.getContent())
                    .rating(review.getRating())
                    .createdAt(review.getCreatedAt())
                    .build();
    }

}