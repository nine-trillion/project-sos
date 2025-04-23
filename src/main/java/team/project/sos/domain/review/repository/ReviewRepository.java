package team.project.sos.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.project.sos.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
