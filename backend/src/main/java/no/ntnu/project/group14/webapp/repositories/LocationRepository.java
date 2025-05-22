package no.ntnu.project.group14.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.project.group14.webapp.entities.Location;

/**
 * The LocationRepository class represents the repository for locations.
 */
@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
}
