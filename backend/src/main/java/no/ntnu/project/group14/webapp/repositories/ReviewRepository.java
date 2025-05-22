package no.ntnu.project.group14.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.project.group14.webapp.entities.Review;

/**
 * The ReviewRepository class represents the repository for reviews.
 */
@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
}
