package no.ntnu.project.group4.webapp.repositories;

import java.util.Optional;
import no.ntnu.project.group4.webapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The UserRepository class represents the repository class for the user entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
