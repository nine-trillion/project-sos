package team.project.sos.domain.store.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.project.sos.domain.store.entity.Store;
import team.project.sos.domain.store.exception.StoreError;
import team.project.sos.domain.store.exception.StoreException;
import team.project.sos.domain.store.repository.StoreRepository;

@Component
@RequiredArgsConstructor
public class StoreValidator {

    private final StoreRepository storeRepository;

    public void validateOwner(Long userId, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreError.NOT_FOUND_STORE));

        if (!store.getOwner().getId().equals(userId)) {
            throw new StoreException(StoreError.UNAUTHORIZED_STORE_OWNER);
        }
    }
}
