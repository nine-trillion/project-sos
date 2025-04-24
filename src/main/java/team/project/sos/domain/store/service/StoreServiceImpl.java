package team.project.sos.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.sos.domain.menu.dto.response.MenuResponseDto;
import team.project.sos.domain.menu.service.MenuService;
import team.project.sos.domain.store.dto.response.StoreDetailResponse;
import team.project.sos.domain.store.dto.response.StorePreviewResponse;
import team.project.sos.domain.store.dto.response.StoreResponse;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.store.exception.StoreError;
import team.project.sos.domain.store.exception.StoreException;
import team.project.sos.domain.store.repository.StoreRepository;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.service.UserService;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    private final UserService userService;
    private final MenuService menuService;

    /**
     * 가게 등록
     * - 사용자 권한이 owner 인 경우만 등록 가능
     * - 운영 중인 가게가 3개 미만인 경우만 등록 가능
     *
     * @param loginId 로그인된 사용자 식별자
     * @param name 등록할 가게 명
     * @param openTime 영업 시작 시간
     * @param closeTime 영업 마감 시간
     * @param minOrderPrice 최소 주문 금액
     * @return 등록된 가게 정보
     */
    @Override
    public StoreResponse saveStore(Long loginId, String name, LocalTime openTime, LocalTime closeTime, int minOrderPrice) {
        User owner = userService.findByIdOrElseThrow(loginId);

        if (!UserRole.OWNER.equals(owner.getRole())) {
            throw new StoreException(StoreError.UNAUTHORIZED_STORE_OWNER);
        }

        long storeCount = storeRepository.countByOwner_Id(owner.getId());

        if (storeCount >= 3) {
            throw new StoreException(StoreError.EXCEEDED_STORE_LIMIT);
        }

        Store store = new Store(name, openTime, closeTime, minOrderPrice, owner);
        Store saved = storeRepository.save(store);

        return StoreResponse.from(saved);
    }

    /**
     * 조건 기반 가게 목록 조회
     * - 필수 조건: 가게 명
     * - 조건이 포함된 가게 목록 조회 가능
     * - 운영 중인 가게만 조회 가능
     *
     * @param name 검색어
     * @return 조회된 가게 목록
     */
    @Override
    public List<StorePreviewResponse> findAllByNameContains(String name) {
        return storeRepository.findAllByNameContains(name)
                .stream()
                .map(StorePreviewResponse::from)
                .toList();
    }

    /**
     * 가게 단건 조회
     *
     * @param storeId 가게 식별자
     * @return 조회된 가게 정보 및 가게의 메뉴 목록
     */
    @Override
    public StoreDetailResponse findStoreWithMenu(Long storeId) {
        Store store = findStoreByIdOrElseThrow(storeId);
        List<MenuResponseDto> menus = menuService.getMenusByStore(storeId);

        return StoreDetailResponse.from(StoreResponse.from(store), menus);
    }

    /**
     * 가게 정보 변경
     *
     * @param loginId 로그인된 사용자 식별자
     * @param storeId 가게 식별자
     * @param name 변경할 가게 명
     * @param openTime 변경할 영업 시작 시간
     * @param closeTime 변경할 영업 마감 시간
     * @param notice 변경할 공지사항
     * @return 변경된 가게 정보
     */
    @Override
    @Transactional
    public StoreResponse updateStore(
            Long loginId, Long storeId, String name, LocalTime openTime,
            LocalTime closeTime, int minOrderPrice, String notice
    ) {
        userService.findByIdOrElseThrow(loginId);

        Store store = findStoreByIdOrElseThrow(storeId);
        Long storeOwnerId = store.getOwner().getId();

        if (!loginId.equals(storeOwnerId)) {
            throw new StoreException(StoreError.UNAUTHORIZED_STORE_OWNER);
        }
        if (!openTime.isBefore(closeTime)) {
            throw new StoreException(StoreError.INVALID_OPEN_TIME_AFTER_CLOSE);
        }

        store.updateStoreInfo(name, openTime, closeTime, minOrderPrice, notice);

        return StoreResponse.from(store);
    }

    /**
     * 가게 삭제
     * - 폐업 상태로 변경
     *
     * @param loginId 로그인된 사용자 식별자
     * @param storeId 가게 식별자
     */
    @Override
    @Transactional
    public void removeStore(Long loginId, Long storeId) {
        userService.findByIdOrElseThrow(loginId);

        Store store = findStoreByIdOrElseThrow(storeId);
        Long storeOwnerId = store.getOwner().getId();

        if (!loginId.equals(storeOwnerId)) {
            throw new StoreException(StoreError.UNAUTHORIZED_STORE_OWNER);
        }

        storeRepository.delete(store);
    }

    /**
     * 사용자의 가게 목록 조회
     * - 폐업한 가게 정보 포함
     *
     * @param ownerId 사용자 식별자
     * @return 조회된 가게 목록
     */
    @Override
    public List<StoreResponse> findStoresByOwner(Long ownerId) {
        List<Store> stores = storeRepository.findStoresByOwner(ownerId);

        return stores.stream()
                .map(StoreResponse::from)
                .toList();
    }

    /**
     * 가게 단건 조회
     * - 운영 중인 가게만 조회 가능
     *
     * @param storeId 가게 식별자
     * @return 조회된 가게 정보
     */
    @Override
    public Store findStoreByIdOrElseThrow(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreError.NOT_FOUND_STORE));
    }
}
