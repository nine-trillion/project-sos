package team.project.sos.domain.store.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import team.project.sos.domain.menu.dto.response.MenuResponseDto;
import team.project.sos.domain.menu.entity.Menu;
import team.project.sos.domain.menu.service.MenuService;
import team.project.sos.domain.store.dto.response.StoreDetailResponse;
import team.project.sos.domain.store.dto.response.StorePreviewResponse;
import team.project.sos.domain.store.dto.response.StoreResponse;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.store.enums.StoreStatus;
import team.project.sos.domain.store.repository.StoreRepository;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.enums.UserRole;
import team.project.sos.domain.user.service.UserService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserService userService;
    @Mock
    private MenuService menuService;
    @InjectMocks
    private StoreServiceImpl storeService;

    @Test
    @DisplayName("가게 등록 성공")
    void saveStore_success() {
        // given
        Long loginId = 1L;
        String name = "Test Store";
        LocalTime openTime = LocalTime.of(12, 0);
        LocalTime closeTime = LocalTime.of(20, 0);
        int minOrderPrice = 15000;

        User owner = new User();
        ReflectionTestUtils.setField(owner, "id", loginId);
        ReflectionTestUtils.setField(owner, "role", UserRole.OWNER);

        Store store = new Store(name, openTime, closeTime, minOrderPrice, owner);

        given(userService.findByIdOrElseThrow(anyLong())).willReturn(owner);
        given(storeRepository.countByOwner_Id(anyLong())).willReturn(1L);
        given(storeRepository.save(any(Store.class))).willReturn(store);

        // when
        StoreResponse response = storeService.saveStore(loginId, name, openTime, closeTime, minOrderPrice);

        // then
        assertNotNull(response);

        assertEquals(store.getName(), response.getName());
        assertEquals(store.getOpenTime(), response.getOpenTime());
        assertEquals(store.getCloseTime(), response.getCloseTime());
        assertEquals(store.getMinOrderPrice(), response.getMinOrderPrice());
    }

    @Test
    @DisplayName("조건 기반 가게 목록 조회 성공")
    void findAllByNameContains_success() {
        // given
        String name = "test";

        List<Store> storeList = List.of(
                new Store("test1", LocalTime.of(10, 0), LocalTime.of(11, 0), 5000, new User()),
                new Store("test2", LocalTime.of(10, 0), LocalTime.of(11, 0), 5000, new User()),
                new Store("test3", LocalTime.of(10, 0), LocalTime.of(11, 0), 5000, new User())
        );

        given(storeRepository.findAllByNameContains(anyString())).willReturn(storeList);

        // when
        List<StorePreviewResponse> responses = storeService.findAllByNameContains(name);

        // then
        IntStream.range(0, responses.size()).forEach(i -> {
            assertEquals(storeList.get(i).getName(), responses.get(i).getName());
        });
    }

    @Test
    @DisplayName("가게 상세 조회 성공")
    void findStoreWithMenu_success() {
        // given
        Long storeId = 1L;

        Store store = new Store("Test Store", LocalTime.of(10, 0), LocalTime.of(11, 0), 10000, new User());
        ReflectionTestUtils.setField(store, "id", storeId);

        List<Menu> menuList = List.of(
                new Menu(storeId, "menu1", 10000, "식사류"),
                new Menu(storeId, "menu2", 20000, "식사류"),
                new Menu(storeId, "menu3", 30000, "식사류")
        );

        List<MenuResponseDto> menuDtoList = menuList.stream()
                .map(MenuResponseDto::from)
                .toList();

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
        given(menuService.getMenusByStore(anyLong())).willReturn(menuDtoList);

        // when
        StoreDetailResponse responses = storeService.findStoreWithMenu(storeId);

        // then
        assertEquals(store.getName(), responses.getStore().getName());

        IntStream.range(0, responses.getMenus().size()).forEach(i -> {
            assertEquals(menuDtoList.get(i).getName(), responses.getMenus().get(i).getName());
            assertEquals(menuDtoList.get(i).getPrice(), responses.getMenus().get(i).getPrice());
        });
    }

    @Test
    @DisplayName("가게 정보 변경 성공")
    void updateStore_success() {
        // given
        Long loginId = 1L;
        Long storeId = 2L;
        String name = "Test Store";
        LocalTime openTime = LocalTime.of(10, 0);
        LocalTime closeTime = LocalTime.of(11, 0);
        int minOrderPrice = 15000;
        String notice = "Hello!";

        User owner = new User();
        ReflectionTestUtils.setField(owner, "id", loginId);

        Store store = new Store("before", LocalTime.of(9, 0), LocalTime.of(10, 0), 10000, owner);

        given(userService.findByIdOrElseThrow(anyLong())).willReturn(owner);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        // when
        StoreResponse response =
                storeService.updateStore(loginId, storeId, name, openTime, closeTime, minOrderPrice, notice);

        // then
        assertEquals(name, response.getName());
        assertEquals(openTime, response.getOpenTime());
        assertEquals(closeTime, response.getCloseTime());
        assertEquals(minOrderPrice, response.getMinOrderPrice());
        assertEquals(notice, response.getNotice());
    }

    @Test
    @DisplayName("가게 삭제 성공 - 폐업 처리")
    void removeStore_success() {
        // given
        Long loginId = 1L;
        Long storeId = 2L;

        User owner = new User();
        ReflectionTestUtils.setField(owner, "id", loginId);

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);
        ReflectionTestUtils.setField(store, "owner", owner);
        ReflectionTestUtils.setField(store, "status", StoreStatus.OPERATING);

        given(userService.findByIdOrElseThrow(anyLong())).willReturn(owner);
        given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

        doAnswer(invocation -> {
            Store deleted = invocation.getArgument(0);
            ReflectionTestUtils.setField(deleted, "status", StoreStatus.CLOSED);
            return null;
        }).when(storeRepository).delete(any(Store.class));

        // when
        storeService.removeStore(loginId, storeId);

        // then
        assertEquals(StoreStatus.CLOSED, store.getStatus());
    }

    @Test
    @DisplayName("사장별 가게 목록 조회 성공 - 폐업한 가게 포함")
    void findStoresByOwner_success() {
        // given
        Long ownerId = 1L;

        User owner = new User();
        ReflectionTestUtils.setField(owner, "id", ownerId);

        List<Store> storeList = new ArrayList<>();

        Store store1 = new Store("store 1", LocalTime.of(9, 0), LocalTime.of(10, 0), 10000, owner);
        Store store2 = new Store("store 2", LocalTime.of(9, 0), LocalTime.of(10, 0), 10000, owner);
        Store store3 = new Store("store 3", LocalTime.of(9, 0), LocalTime.of(10, 0), 10000, owner);
        ReflectionTestUtils.setField(store3, "status", StoreStatus.CLOSED);

        storeList.add(store1);
        storeList.add(store2);
        storeList.add(store3);

        given(storeRepository.findStoresByOwner(anyLong())).willReturn(storeList);

        // when
        List<StoreResponse> responses = storeService.findStoresByOwner(ownerId);

        // then
        assertEquals(responses.get(0).getName(), store1.getName());
        assertEquals(responses.get(1).getName(), store2.getName());
        assertEquals(responses.get(2).getName(), store3.getName());
        assertEquals(responses.get(0).getStatus(), store1.getStatus().name());
        assertEquals(responses.get(1).getStatus(), store2.getStatus().name());
        assertEquals(responses.get(2).getStatus(), store3.getStatus().name());
    }
}