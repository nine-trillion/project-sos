package team.project.sos.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class CreateReviewRequestDto {
    private final Long storeId;

    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private final String content;


    @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5점 이하여야 합니다.")
    private final int rating;

    public CreateReviewRequestDto(Long storeId, String content, int rating) {
        this.storeId = storeId;
        this.content = content;
        this.rating = rating;
    }
}
