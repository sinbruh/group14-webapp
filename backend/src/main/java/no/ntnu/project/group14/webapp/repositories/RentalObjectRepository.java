package no.ntnu.project.group14.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.project.group14.webapp.entities.RentalObject;

/**
 * The RentalObjectRepository class represents the repository for rental objects.
 */
@Repository
public interface RentalObjectRepository extends CrudRepository<RentalObject, Long> {
}
