package team.project.sos.domain.menu.dto.request;

import lombok.Getter;

@Getter
public class UpdateMenuRequestDto {
    private String name;
    private int price;
    private String category;
}