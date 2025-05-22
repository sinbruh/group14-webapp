package no.ntnu.project.group14.webapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.ntnu.project.group14.webapp.entities.Review;
import no.ntnu.project.group14.webapp.repositories.ReviewRepository;

/**
 * The ReviewService class represents the service for {@link Review reviews}.
 */
@Service
@Transactional(readOnly = true)
public class ReviewService {

  @Autowired
  private ReviewRepository reviewRepository;

  /**
   * Gets all reviews.
   * 
   * @return All reviews
   */
  public Iterable<Review> getAll() {
    return this.reviewRepository.findAll();
  }

  /**
   * Gets the review with the specified ID.
   * 
   * @param id The specified ID
   * @return The review
   */
  public Optional<Review> get(Long id) {
    return this.reviewRepository.findById(id);
  }

  /**
   * Adds the specified review. The specified review is only added if it is valid.
   * 
   * @param review The specified review
   * @return The generated ID if the specified review is valid
   * @throws IllegalArgumentException If the specified review is invalid
   */
  @Transactional
  public Long add(Review review) {
    if (!review.isValid()) {
      throw new IllegalArgumentException("Review is invalid");
    }
    this.reviewRepository.save(review);
    return review.getId();
  }

  /**
   * Updates the review with the specified ID with the specified review. The review is only updated
   * if a review with the specified ID exists and the specified review is valid.
   * 
   * @param id The specified ID
   * @param review The specified review
   * @return True if the review exists and is updated or false otherwise
   * @throws IllegalArgumentException If the specified review is invalid
   */
  @Transactional
  public boolean update(Long id, Review review) {
    if (!review.isValid()) {
      throw new IllegalArgumentException("Review is invalid");
    }
    Optional<Review> existingReview = this.reviewRepository.findById(id);
    boolean exist = existingReview.isPresent();
    if (exist) {
      Review storedReview = existingReview.get();
      storedReview.setRating(review.getRating());
      storedReview.setText(review.getText());
      this.reviewRepository.save(storedReview);
    }
    return exist;
  }

  /**
   * Deletes the review with the specified ID. The review is only deleted if a review with the
   * specified ID exists.
   * 
   * @param id The specified ID
   * @return True if the review exists and is deleted or false otherwise
   */
  @Transactional
  public boolean delete(Long id) {
    Optional<Review> review = this.reviewRepository.findById(id);
    boolean exist = review.isPresent();
    if (exist) {
      this.reviewRepository.deleteById(id);
    }
    return exist;
  }
}
