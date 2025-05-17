package no.ntnu.project.group14.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.project.group14.webapp.entities.ExtraFeature;

/**
 * The ExtraFeatureRepository class represents the repository class for the extra feature entity.
 *
 * @author Group 4
 * @version v1.0 (2024.05.22)
 */
@Repository
public interface ExtraFeatureRepository extends CrudRepository<ExtraFeature, Long> {
}
