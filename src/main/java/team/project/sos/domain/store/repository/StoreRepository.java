package team.project.sos.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.project.sos.domain.store.entity.Store;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByNameContains(String name);

    @Query("select s from Store s where s.owner.id = :ownerId")
    List<Store> findStoresByOwner(@Param("ownerId") Long ownerId);
}
