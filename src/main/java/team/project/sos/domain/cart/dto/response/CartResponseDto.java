package team.project.sos.domain.cart.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartResponseDto {

    @NotNull
    private Long id;

    @NotNull
    private Long userId;

}