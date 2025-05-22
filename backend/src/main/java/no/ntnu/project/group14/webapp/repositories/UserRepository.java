package no.ntnu.project.group14.webapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.project.group14.webapp.entities.User;

/**
 * The UserRepository class represents the repository class for the user entity.
 *
 * @author Group 4
 * @version v1.0 (2024.05.22)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
