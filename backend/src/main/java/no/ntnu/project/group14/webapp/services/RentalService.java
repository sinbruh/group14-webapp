package no.ntnu.project.group14.webapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.ntnu.project.group14.webapp.entities.Rental;
import no.ntnu.project.group14.webapp.repositories.RentalRepository;

/**
 * The RentalService class represents the service for {@link Rental rentals}.
 */
@Service
@Transactional(readOnly = true)
public class RentalService {

  @Autowired
  private RentalRepository rentalRepository;

  /**
   * Gets all rentals.
   * 
   * @return All rentals
   */
  public Iterable<Rental> getAll() {
    return this.rentalRepository.findAll();
  }

  /**
   * Gets the rental with the specified ID.
   * 
   * @param id The specified ID
   * @return The rental
   */
  public Optional<Rental> get(Long id) {
    return this.rentalRepository.findById(id);
  }

  /**
   * Adds the specified rental. The specified rental is only added if it is valid.
   * 
   * @param rental The specified rental
   * @return The generated ID if the specified rental is valid
   * @throws IllegalArgumentException If the specified rental is invalid
   */
  @Transactional
  public Long add(Rental rental) {
    if (!rental.isValid()) {
      throw new IllegalArgumentException("Rental is invalid");
    }
    this.rentalRepository.save(rental);
    return rental.getId();
  }

  /**
   * Updates the rental with the specified ID with the specified rental. The rental is only updated
   * if a rental with the specified ID exists and the specified rental is valid.
   * 
   * @param id The specified ID
   * @param rental The specified rental
   * @return True if the rental exists and is updated or false otherwise
   * @throws IllegalArgumentException If the specified rental is invalid
   */
  @Transactional
  public boolean update(Long id, Rental rental) {
    if (!rental.isValid()) {
      throw new IllegalArgumentException("Rental is invalid");
    }
    Optional<Rental> existingRental = this.rentalRepository.findById(id);
    boolean exist = existingRental.isPresent();
    if (exist) {
      Rental storedRental = existingRental.get();
      storedRental.setStartTime(rental.getStartTime());
      storedRental.setEndTime(rental.getEndTime());
      this.rentalRepository.save(storedRental);
    }
    return exist;
  }

  /**
   * Deletes the rental with the specified ID. The rental is only deleted if a rental with the
   * specified ID exists.
   * 
   * @param id The specified ID
   * @return True if the rental exists and is deleted or false otherwise
   */
  @Transactional
  public boolean delete(Long id) {
    Optional<Rental> rental = this.rentalRepository.findById(id);
    boolean exist = rental.isPresent();
    if (exist) {
      this.rentalRepository.deleteById(id);
    }
    return exist;
  }
}
