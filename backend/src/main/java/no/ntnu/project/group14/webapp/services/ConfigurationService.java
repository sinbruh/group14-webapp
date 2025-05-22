package no.ntnu.project.group14.webapp.services;

import java.util.Optional;

import no.ntnu.project.group14.webapp.entities.Configuration;
import no.ntnu.project.group14.webapp.repositories.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The ConfigurationService class represents the service for configurations.
 */
@Service
@Transactional(readOnly = true)
public class ConfigurationService {

  @Autowired
  private ConfigurationRepository configurationRepository;

  /**
   * Get all configurations in storage.
   *
   * @return All configurations
   */
  public Iterable<Configuration> getAll() {
    return this.configurationRepository.findAll();
  }
  
  /**
   * Get the configuration in storage with the specified ID. If the configuration does not exist,
   * it is <code>null</code> in the returned object.
   *
   * @param id The specified ID
   * @return The configuration with the specified ID
   */
  public Optional<Configuration> get(Long id) {
    return this.configurationRepository.findById(id);
  }
}
