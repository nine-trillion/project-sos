package team.project.sos.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.project.sos.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
