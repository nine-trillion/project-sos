package team.project.sos.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.project.sos.domain.review.entity.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByOrderStoreIdOrderByCreatedAtDesc(Long storeId);

    Optional<Review> findByOrderId(Long orderId);

}