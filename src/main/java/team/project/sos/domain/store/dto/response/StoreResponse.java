package team.project.sos.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.project.sos.domain.store.entity.Store;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreResponse {
    /**
     * 가게 명
     */
    private final String name;

    /**
     * 가게 오픈 시간
     */
    private final LocalTime openTime;

    /**
     * 가게 마감 시간
     */
    private final LocalTime closeTime;

    /**
     * 최소 주문 금액
     */
    private final int minOrderPrice;

    /**
     * 가게 영업 상태
     */
    private final String status;

    /**
     * 가게 공지사항
     */
    private final String notice;

    /**
     * 가게 사장 식별자
     */
    private final Long ownerId;

    /**
     * 가게 사장 이름
     */
    private final String ownerName;

    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getName(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getMinOrderPrice(),
                store.getStatus().name(),
                store.getNotice(),
                store.getOwner().getId(),
                store.getOwner().getNickname()
        );
    }
}
