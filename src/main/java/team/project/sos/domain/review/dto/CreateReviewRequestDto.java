package team.project.sos.domain.review.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class CreateReviewRequestDto {
    private final Long storeId;

    private final String content;

    private final int rating;

    public CreateReviewRequestDto(Long storeId, String content, int rating) {
        this.storeId = storeId;
        this.content = content;
        this.rating = rating;
    }
}
