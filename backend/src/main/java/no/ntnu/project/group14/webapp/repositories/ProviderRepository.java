package no.ntnu.project.group14.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.project.group14.webapp.entities.Provider;

/**
 * The ProviderRepository class represents the repository for providers.
 */
@Repository
public interface ProviderRepository extends CrudRepository<Provider, Long> {
}
