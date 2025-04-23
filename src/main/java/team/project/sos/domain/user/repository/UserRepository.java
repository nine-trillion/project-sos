package team.project.sos.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.project.sos.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);
}
