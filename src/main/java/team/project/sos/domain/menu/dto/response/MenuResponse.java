
package team.project.sos.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.project.sos.domain.menu.entity.Menu;

@Getter
@AllArgsConstructor
public class MenuResponse {
    private Long id;
    private String name;
    private int price;
    private String category;

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getCategory()
        );
    }
}
