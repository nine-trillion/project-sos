package team.project.sos.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.project.sos.domain.review.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByOrderStoreIdOrderByCreatedAtDesc(Long storeId);
    // 여기에 커스텀 쿼리 메서드 추가 가능
}