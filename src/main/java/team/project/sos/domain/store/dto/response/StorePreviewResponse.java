package team.project.sos.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.project.sos.domain.store.entity.Store;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StorePreviewResponse {

    private final Long id;
    private final String name;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final int minOrderPrice;

    public static StorePreviewResponse from(Store store) {
        return new StorePreviewResponse(
                store.getId(),
                store.getName(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinOrderPrice()
        );
    }
}
