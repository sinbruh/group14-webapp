package no.ntnu.project.group14.webapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.ntnu.project.group14.webapp.entities.RentalObject;
import no.ntnu.project.group14.webapp.repositories.RentalObjectRepository;

/**
 * The RentalObjectService class represents the service for {@link RentalObject rental objects}.
 */
@Service
@Transactional(readOnly = true)
public class RentalObjectService {

  @Autowired
  private RentalObjectRepository rentalObjectRepository;

  /**
   * Gets all rental objects.
   * 
   * @return All rental objects
   */
  public Iterable<RentalObject> getAll() {
    return this.rentalObjectRepository.findAll();
  }

  /**
   * Gets the rental object with the specified ID.
   * 
   * @param id The specified ID
   * @return The rental object
   */
  public Optional<RentalObject> get(Long id) {
    return this.rentalObjectRepository.findById(id);
  }

  /**
   * Adds the specified rental object. The specified rental object is only added if it is valid.
   * 
   * @param rentalObject The specified rental object
   * @return The generated ID if the specified rental object is valid
   * @throws IllegalArgumentException If the specified rental object is invalid
   */
  @Transactional
  public Long add(RentalObject rentalObject) {
    if (!rentalObject.isValid()) {
      throw new IllegalArgumentException("Rental object is invalid");
    }
    this.rentalObjectRepository.save(rentalObject);
    return rentalObject.getId();
  }

  /**
   * Updates the rental object with the specified ID with the specified rental object. The rental
   * object is only updated if a rental object with the specified ID exists and the specified
   * rental object is valid.
   * 
   * @param id The specified ID
   * @param rentalObject The specified rental object
   * @return True if the rental object exists and is updated or false otherwise
   * @throws IllegalArgumentException If the specified rental object is invalid
   */
  @Transactional
  public boolean update(Long id, RentalObject rentalObject) {
    if (!rentalObject.isValid()) {
      throw new IllegalArgumentException("Rental object is invalid");
    }
    Optional<RentalObject> existingRentalObject = this.rentalObjectRepository.findById(id);
    boolean exist = existingRentalObject.isPresent();
    if (exist) {
      RentalObject storedRentalObject = existingRentalObject.get();
      storedRentalObject.setPrice(rentalObject.getPrice());
      storedRentalObject.setAvailable(rentalObject.isAvailable());
      storedRentalObject.setVisible(rentalObject.isVisible());
      this.rentalObjectRepository.save(storedRentalObject);
    }
    return exist;
  }

  /**
   * Deletes the rental object with the specified ID. The rental object is only deleted if a rental
   * object with the specified ID exists.
   * 
   * @param id The specified ID
   * @return True if the rental object exists and is deleted or false otherwise
   */
  @Transactional
  public boolean delete(Long id) {
    Optional<RentalObject> rentalObject = this.rentalObjectRepository.findById(id);
    boolean exist = rentalObject.isPresent();
    if (exist) {
      this.rentalObjectRepository.deleteById(id);
    }
    return exist;
  }
}
