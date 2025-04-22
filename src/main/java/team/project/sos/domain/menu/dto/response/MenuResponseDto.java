package team.project.sos.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuResponseDto {
    private Long id;
    private String name;
    private int price;
    private String category;
}