package no.ntnu.project.group14.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.project.group14.webapp.entities.Rental;

/**
 * The RentalRepository class represents the repository for rentals.
 */
@Repository
public interface RentalRepository extends CrudRepository<Rental, Long> {
}
