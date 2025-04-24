package team.project.sos.domain.store.service;

import team.project.sos.domain.store.dto.response.StoreDetailResponse;
import team.project.sos.domain.store.dto.response.StorePreviewResponse;
import team.project.sos.domain.store.dto.response.StoreResponse;
import team.project.sos.domain.store.entity.Store;

import java.time.LocalTime;
import java.util.List;

public interface StoreService {

    StoreResponse saveStore(Long loginId, String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice);

    List<StorePreviewResponse> findAllByNameContains(String name);

    StoreDetailResponse findStoreWithMenu(Long storeId);

    StoreResponse updateStore(Long loginId, Long storeId, String name, LocalTime openTime, LocalTime closeTime, String notice);

    void removeStore(Long loginId, Long storeId);

    List<StoreResponse> findStoresByOwner(Long ownerId);

    Store findStoreByIdOrElseThrow(Long storeId);


}
