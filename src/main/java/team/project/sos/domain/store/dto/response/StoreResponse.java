package team.project.sos.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.project.sos.domain.store.entity.Store;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreResponse {

    private final String name;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final int minOrderPrice;
    private final String status;
    private final boolean isOperating;
    private final String notice;
    private final Long ownerId;
    private final String ownerName;

    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getName(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinOrderPrice(),
                store.getStatus().name(),
                store.isOperating(),
                store.getNotice(),
                store.getOwner().getId(),
                store.getOwner().getNickname()
        );
    }
}
