package no.ntnu.project.group14.webapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.ntnu.project.group14.webapp.entities.Configuration;
import no.ntnu.project.group14.webapp.repositories.ConfigurationRepository;

/**
 * The ConfigurationService class represents the service for {@link Configuration configurations}.
 */
@Service
@Transactional(readOnly = true)
public class ConfigurationService {

  @Autowired
  private ConfigurationRepository configurationRepository;

  /**
   * Gets all configurations.
   * 
   * @return All configurations
   */
  public Iterable<Configuration> getAll() {
    return this.configurationRepository.findAll();
  }

  /**
   * Gets the configuration with the specified ID.
   * 
   * @param id The specified ID
   * @return The configuration
   */
  public Optional<Configuration> get(Long id) {
    return this.configurationRepository.findById(id);
  }

  /**
   * Adds the specified configuration. The specified configuration is only added if it is valid.
   * 
   * @param configuration The specified configuration
   * @return The generated ID if the specified configuration is valid
   * @throws IllegalArgumentException If the specified configuration is invalid
   */
  @Transactional
  public Long add(Configuration configuration) {
    if (!configuration.isValid()) {
      throw new IllegalArgumentException("Configuration is invalid");
    }
    this.configurationRepository.save(configuration);
    return configuration.getId();
  }

  /**
   * Updates the configuration with the specified ID with the specified configuration. The
   * configuration is only updated if a configuration with the specified ID exists and the
   * specified configuration is valid.
   * 
   * @param id The specified ID
   * @param configuration The specified configuration
   * @return True if the configuration exists and is updated or false otherwise
   * @throws IllegalArgumentException If the specified configuration is invalid
   */
  @Transactional
  public boolean update(Long id, Configuration configuration) {
    if (!configuration.isValid()) {
      throw new IllegalArgumentException("Configuration is invalid");
    }
    Optional<Configuration> existingConfiguration = this.configurationRepository.findById(id);
    boolean exist = existingConfiguration.isPresent();
    if (exist) {
      Configuration storedConfiguration = existingConfiguration.get();
      storedConfiguration.setName(configuration.getName());
      storedConfiguration.setFuelType(configuration.getFuelType());
      storedConfiguration.setTransmissionType(configuration.getTransmissionType());
      storedConfiguration.setNumberOfSeats(configuration.getNumberOfSeats());
      this.configurationRepository.save(storedConfiguration);
    }
    return exist;
  }

  /**
   * Deletes the configuration with the specified ID. The configuration is only deleted if a
   * configuration with the specified ID exists.
   * 
   * @param id The specified ID
   * @return True if the configuration exists and is deleted or false otherwise
   */
  @Transactional
  public boolean delete(Long id) {
    Optional<Configuration> configuration = this.configurationRepository.findById(id);
    boolean exist = configuration.isPresent();
    if (exist) {
      this.configurationRepository.deleteById(id);
    }
    return exist;
  }
}
