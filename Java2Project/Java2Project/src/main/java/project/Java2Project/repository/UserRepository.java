package project.Java2Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.Java2Project.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
