package team.project.sos.domain.cart.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateCartItemsRequestDto {

    @NotEmpty
    private List<CartItemRequestDto> items;

}