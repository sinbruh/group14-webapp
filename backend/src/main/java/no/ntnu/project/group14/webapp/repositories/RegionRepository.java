package no.ntnu.project.group14.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.project.group14.webapp.entities.Region;

/**
 * The RegionRepository class represents the repository for regions.
 */
@Repository
public interface RegionRepository extends CrudRepository<Region, Long> {
}
