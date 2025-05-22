package no.ntnu.project.group14.webapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.ntnu.project.group14.webapp.entities.Location;
import no.ntnu.project.group14.webapp.repositories.LocationRepository;

/**
 * The LocationService class represents the service for {@link Location locations}.
 */
@Service
@Transactional(readOnly = true)
public class LocationService {

  @Autowired
  private LocationRepository locationRepository;

  /**
   * Gets all locations.
   * 
   * @return All locations
   */
  public Iterable<Location> getAll() {
    return this.locationRepository.findAll();
  }

  /**
   * Gets the location with the specified ID.
   * 
   * @param id The specified ID
   * @return The location
   */
  public Optional<Location> get(Long id) {
    return this.locationRepository.findById(id);
  }

  /**
   * Adds the specified location. The specified location is only added if it is valid.
   * 
   * @param location The specified location
   * @return The generated ID if the specified location is valid
   * @throws IllegalArgumentException If the specified location is invalid
   */
  @Transactional
  public Long add(Location location) {
    if (!location.isValid()) {
      throw new IllegalArgumentException("Location is invalid");
    }
    this.locationRepository.save(location);
    return location.getId();
  }

  /**
   * Updates the location with the specified ID with the specified location. The location is only
   * updated if a location with the specified ID exists and the specified location is valid.
   * 
   * @param id The specified ID
   * @param location The specified location
   * @return True if the location exists and is updated or false otherwise
   * @throws IllegalArgumentException If the specified location is invalid
   */
  @Transactional
  public boolean update(Long id, Location location) {
    if (!location.isValid()) {
      throw new IllegalArgumentException("Location is invalid");
    }
    Optional<Location> existingLocation = this.locationRepository.findById(id);
    boolean exist = existingLocation.isPresent();
    if (exist) {
      Location storedLocation = existingLocation.get();
      storedLocation.setName(location.getName());
      this.locationRepository.save(storedLocation);
    }
    return exist;
  }

  /**
   * Deletes the location with the specified ID. The location is only deleted if a location with
   * the specified ID exists.
   * 
   * @param id The specified ID
   * @return True if the location exists and is deleted or false otherwise
   */
  @Transactional
  public boolean delete(Long id) {
    Optional<Location> location = this.locationRepository.findById(id);
    boolean exist = location.isPresent();
    if (exist) {
      this.locationRepository.deleteById(id);
    }
    return exist;
  }
}
