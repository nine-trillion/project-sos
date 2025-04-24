
package team.project.sos.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.project.sos.domain.menu.entity.Menu;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByStoreIdAndIsDeletedFalse(Long storeId);
    List<Menu> findAllByStoreIdAndCategoryAndIsDeletedFalse(Long storeId, String category);
}

