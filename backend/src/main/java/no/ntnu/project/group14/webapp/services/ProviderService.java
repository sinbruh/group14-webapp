package no.ntnu.project.group14.webapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.ntnu.project.group14.webapp.entities.Provider;
import no.ntnu.project.group14.webapp.repositories.ProviderRepository;

/**
 * The ProviderService class represents the service for {@link Provider providers}.
 */
@Service
@Transactional(readOnly = true)
public class ProviderService {

  @Autowired
  private ProviderRepository providerRepository;

  /**
   * Gets all providers.
   * 
   * @return All providers
   */
  public Iterable<Provider> getAll() {
    return this.providerRepository.findAll();
  }

  /**
   * Gets the provider with the specified ID.
   * 
   * @param id The specified ID
   * @return The provider
   */
  public Optional<Provider> get(Long id) {
    return this.providerRepository.findById(id);
  }

  /**
   * Adds the specified provider. The specified provider is only added if it is valid.
   * 
   * @param provider The specified provider
   * @return The generated ID if the specified provider is valid
   * @throws IllegalArgumentException If the specified provider is invalid
   */
  @Transactional
  public Long add(Provider provider) {
    if (!provider.isValid()) {
      throw new IllegalArgumentException("Provider is invalid");
    }
    this.providerRepository.save(provider);
    return provider.getId();
  }

  /**
   * Updates the provider with the specified ID with the specified provider. The provider is only
   * updated if a provider with the specified ID exists and the specified provider is valid.
   * 
   * @param id The specified ID
   * @param provider The specified provider
   * @return True if the provider exists and is updated or false otherwise
   * @throws IllegalArgumentException If the specified provider is invalid
   */
  @Transactional
  public boolean update(Long id, Provider provider) {
    if (!provider.isValid()) {
      throw new IllegalArgumentException("Provider is invalid");
    }
    Optional<Provider> existingProvider = this.providerRepository.findById(id);
    boolean exist = existingProvider.isPresent();
    if (exist) {
      Provider storedProvider = existingProvider.get();
      storedProvider.setName(provider.getName());
      this.providerRepository.save(storedProvider);
    }
    return exist;
  }

  /**
   * Deletes the provider with the specified ID. The provider is only deleted if a provider with
   * the specified ID exists.
   * 
   * @param id The specified ID
   * @return True if the provider exists and is deleted or false otherwise
   */
  @Transactional
  public boolean delete(Long id) {
    Optional<Provider> provider = this.providerRepository.findById(id);
    boolean exist = provider.isPresent();
    if (exist) {
      this.providerRepository.deleteById(id);
    }
    return exist;
  }
}
