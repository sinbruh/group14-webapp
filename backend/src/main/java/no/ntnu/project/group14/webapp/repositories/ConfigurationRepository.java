package no.ntnu.project.group14.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.project.group14.webapp.entities.Configuration;

/**
 * The ConfigurationRepository class represents the repository for configurations.
 */
@Repository
public interface ConfigurationRepository extends CrudRepository<Configuration, Long> {
}
