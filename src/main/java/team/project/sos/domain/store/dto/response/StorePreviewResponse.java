package team.project.sos.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorePreviewResponse {

    private final String name;
    private final int minOrderPrice;
}
