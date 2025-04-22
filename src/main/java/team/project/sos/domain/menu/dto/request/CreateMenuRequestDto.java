package team.project.sos.domain.menu.dto.request;

import lombok.Getter;

@Getter
public class CreateMenuRequestDto {
    private Long storeId;
    private String name;
    private int price;
    private String category;
}