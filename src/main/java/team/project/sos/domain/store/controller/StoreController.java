package team.project.sos.domain.store.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.project.sos.domain.store.dto.request.StoreCreateRequest;
import team.project.sos.domain.store.dto.request.StoreUpdateRequest;
import team.project.sos.domain.store.dto.response.StoreDetailResponse;
import team.project.sos.domain.store.dto.response.StorePreviewResponse;
import team.project.sos.domain.store.dto.response.StoreResponse;
import team.project.sos.domain.store.service.StoreService;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponse> saveStore(@Valid @RequestBody StoreCreateRequest storeRequest) {
        // TODO: 토큰에서 로그인된 사용자 정보 꺼내기
        Long loginId = 1L;

        StoreResponse response = storeService.saveStore(
                loginId,
                storeRequest.getName(),
                storeRequest.getOpenTime(),
                storeRequest.getCloseTime(),
                storeRequest.getMinOrderPrice()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StorePreviewResponse>> findStoresByName(@RequestParam String name) {
        List<StorePreviewResponse> response = storeService.findAllByNameContains(name);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailResponse> findStoreWithMenu(@NotNull @PathVariable Long storeId) {
        StoreDetailResponse response = storeService.findStoreWithMenu(storeId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponse> updateStore(@NotNull @PathVariable Long storeId,
                                                     @Valid @RequestBody StoreUpdateRequest updateRequest) {
        // TODO: 로그인된 사용자 아이디 꺼내기
        Long loginId = 1L;

        StoreResponse response = storeService.updateStore(
                loginId,
                storeId,
                updateRequest.getName(),
                updateRequest.getOpenTime(),
                updateRequest.getCloseTime(),
                updateRequest.getNotice()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> removeStore(@NotNull @PathVariable Long storeId) {
        // TODO: 로그인된 사용자 아이디 꺼내기
        Long loginUserId = 1L;
        storeService.removeStore(loginUserId, storeId);

        return ResponseEntity.noContent().build();
    }
}
