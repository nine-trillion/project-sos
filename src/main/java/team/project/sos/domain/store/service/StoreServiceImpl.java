package team.project.sos.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.project.sos.domain.store.dto.response.StoreDetailResponse;
import team.project.sos.domain.store.dto.response.StorePreviewResponse;
import team.project.sos.domain.store.dto.response.StoreSaveResponse;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.store.repository.StoreRepository;
import team.project.sos.domain.user.entity.User;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
//    private final UserService userService;

    @Override
    public StoreSaveResponse saveStore(Long loginId, String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice) {
//         TODO: 사용자 권한이 owner인지 체크
//        User owner = new User();

//        Store store = new Store(name, openTime, closeTime, minOrderPrice, owner);

        return null;
    }

    @Override
    public List<StorePreviewResponse> findStoresByName(String name) {
        return List.of();
    }

    @Override
    public StoreDetailResponse findStoreWithMenu(Long storeId) {
        return null;
    }

    @Override
    public StoreSaveResponse updateStore(Long loginId, Long storeId, String name, LocalTime openTime, LocalTime closeTime, String notice) {
        return null;
    }

    @Override
    public void removeStore(Long ownerId, Long storeId) {

    }
}
