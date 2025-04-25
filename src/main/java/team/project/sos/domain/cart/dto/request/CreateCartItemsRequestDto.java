package team.project.sos.domain.cart.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CreateCartItemsRequestDto {

    @NotEmpty
    private List<CartItemRequestDto> items;

}