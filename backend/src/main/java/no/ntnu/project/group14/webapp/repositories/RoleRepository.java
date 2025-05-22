package no.ntnu.project.group14.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.project.group14.webapp.entities.Role;

/**
 * The RoleRepository class represents the repository for roles.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findOneByName(String name);
}
