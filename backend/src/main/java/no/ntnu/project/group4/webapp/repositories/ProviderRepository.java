package no.ntnu.project.group4.webapp.repositories;

import no.ntnu.project.group4.webapp.models.Provider;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The ProviderRepository class represents the repository class for the provider entity.
 */
@Repository
public interface ProviderRepository extends CrudRepository<Provider, Long> {
}
