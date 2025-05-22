package no.ntnu.project.group14.webapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.ntnu.project.group14.webapp.entities.ExtraFeature;
import no.ntnu.project.group14.webapp.repositories.ExtraFeatureRepository;

/**
 * The ExtraFeatureService class represents the service for {@link ExtraFeature extra features}.
 */
@Service
@Transactional(readOnly = true)
public class ExtraFeatureService {

  @Autowired
  private ExtraFeatureRepository extraFeatureRepository;

  /**
   * Gets all extra features.
   * 
   * @return All extra features
   */
  public Iterable<ExtraFeature> getAll() {
    return this.extraFeatureRepository.findAll();
  }

  /**
   * Gets the extra feature with the specified ID.
   * 
   * @param id The specified ID
   * @return The extra feature
   */
  public Optional<ExtraFeature> get(Long id) {
    return this.extraFeatureRepository.findById(id);
  }

  /**
   * Adds the specified extra feature. The specified extra feature is only added if it is valid.
   * 
   * @param extraFeature The specified extra feature
   * @return The generated ID if the specified extra feature is valid
   * @throws IllegalArgumentException If the specified extra feature is invalid
   */
  @Transactional
  public Long add(ExtraFeature extraFeature) {
    if (!extraFeature.isValid()) {
      throw new IllegalArgumentException("ExtraFeature is invalid");
    }
    this.extraFeatureRepository.save(extraFeature);
    return extraFeature.getId();
  }

  /**
   * Updates the extra feature with the specified ID with the specified extra feature. The extra
   * feature is only updated if a extra feature with the specified ID exists and the specified
   * extra feature is valid.
   * 
   * @param id The specified ID
   * @param extraFeature The specified extra feature
   * @return True if the extra feature exists and is updated or false otherwise
   * @throws IllegalArgumentException If the specified extra feature is invalid
   */
  @Transactional
  public boolean update(Long id, ExtraFeature extraFeature) {
    if (!extraFeature.isValid()) {
      throw new IllegalArgumentException("Extra feature is invalid");
    }
    Optional<ExtraFeature> existingExtraFeature = this.extraFeatureRepository.findById(id);
    boolean exist = existingExtraFeature.isPresent();
    if (exist) {
      ExtraFeature storedExtraFeature = existingExtraFeature.get();
      storedExtraFeature.setName(extraFeature.getName());
      this.extraFeatureRepository.save(storedExtraFeature);
    }
    return exist;
  }

  /**
   * Deletes the extra feature with the specified ID. The extra feature is only deleted if a extra
   * feature with the specified ID exists.
   * 
   * @param id The specified ID
   * @return True if the extra feature exists and is deleted or false otherwise
   */
  @Transactional
  public boolean delete(Long id) {
    Optional<ExtraFeature> extraFeature = this.extraFeatureRepository.findById(id);
    boolean exist = extraFeature.isPresent();
    if (exist) {
      this.extraFeatureRepository.deleteById(id);
    }
    return exist;
  }
}
