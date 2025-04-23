package team.project.sos.domain.review.dto;

import lombok.Getter;

@Getter
public class CreateReviewResponseDto {
    private final Long storeId;

    private final String content;

    private final int rating;

    public CreateReviewResponseDto(Long storeId, String content, int rating) {
        this.storeId = storeId;
        this.content = content;
        this.rating = rating;
    }
}
