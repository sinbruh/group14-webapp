package no.ntnu.project.group14.webapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.ntnu.project.group14.webapp.entities.Region;
import no.ntnu.project.group14.webapp.repositories.RegionRepository;

/**
 * The RegionService class represents the service for {@link Region regions}.
 */
@Service
@Transactional(readOnly = true)
public class RegionService {

  @Autowired
  private RegionRepository regionRepository;

  /**
   * Gets all regions.
   * 
   * @return All regions
   */
  public Iterable<Region> getAll() {
    return this.regionRepository.findAll();
  }

  /**
   * Gets the region with the specified ID.
   * 
   * @param id The specified ID
   * @return The region
   */
  public Optional<Region> get(Long id) {
    return this.regionRepository.findById(id);
  }

  /**
   * Adds the specified region. The specified region is only added if it is valid.
   * 
   * @param region The specified region
   * @return The generated ID if the specified region is valid
   * @throws IllegalArgumentException If the specified region is invalid
   */
  @Transactional
  public Long add(Region region) {
    if (!region.isValid()) {
      throw new IllegalArgumentException("Region is invalid");
    }
    this.regionRepository.save(region);
    return region.getId();
  }

  /**
   * Updates the region with the specified ID with the specified region. The region is only updated
   * if a region with the specified ID exists and the specified region is valid.
   * 
   * @param id The specified ID
   * @param region The specified region
   * @return True if the region exists and is updated or false otherwise
   * @throws IllegalArgumentException If the specified region is invalid
   */
  @Transactional
  public boolean update(Long id, Region region) {
    if (!region.isValid()) {
      throw new IllegalArgumentException("Region is invalid");
    }
    Optional<Region> existingRegion = this.regionRepository.findById(id);
    boolean exist = existingRegion.isPresent();
    if (exist) {
      Region storedRegion = existingRegion.get();
      storedRegion.setName(region.getName());
      this.regionRepository.save(storedRegion);
    }
    return exist;
  }

  /**
   * Deletes the region with the specified ID. The region is only deleted if a region with the
   * specified ID exists.
   * 
   * @param id The specified ID
   * @return True if the region exists and is deleted or false otherwise
   */
  @Transactional
  public boolean delete(Long id) {
    Optional<Region> region = this.regionRepository.findById(id);
    boolean exist = region.isPresent();
    if (exist) {
      this.regionRepository.deleteById(id);
    }
    return exist;
  }
}
