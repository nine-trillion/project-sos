package team.project.sos.domain.store.service;

import team.project.sos.domain.store.dto.response.StoreDetailResponse;
import team.project.sos.domain.store.dto.response.StorePreviewResponse;
import team.project.sos.domain.store.dto.response.StoreSaveResponse;

import java.time.LocalTime;
import java.util.List;

public interface StoreService {

    StoreSaveResponse saveStore(Long loginId, String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice);

    List<StorePreviewResponse> findStoresByName(String name);

    StoreDetailResponse findStoreWithMenu(Long storeId);

    StoreSaveResponse updateStore(Long loginId, Long storeId, String name, LocalTime openTime, LocalTime closeTime, String notice);

    void removeStore(Long loginId, Long storeId);
}
