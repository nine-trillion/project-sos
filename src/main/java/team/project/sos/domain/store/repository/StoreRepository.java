package team.project.sos.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.project.sos.domain.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
