package no.ntnu.project.group14.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.project.group14.webapp.entities.Car;

/**
 * The CarRepository class represents the repository for cars.
 */
@Repository
public interface CarRepository extends CrudRepository<Car, Long> {
}
