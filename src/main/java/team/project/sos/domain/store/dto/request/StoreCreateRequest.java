package team.project.sos.domain.store.dto.request;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreCreateRequest {

    private String name;

    private LocalTime openTime;

    private LocalTime closeTime;

    private int minOrderPrice;
}
