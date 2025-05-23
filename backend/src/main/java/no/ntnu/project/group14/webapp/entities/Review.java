package no.ntnu.project.group14.webapp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The Review class represents the entity for reviews. Reviews are placed on configurations and
 * each configuration as well as {@link User user} can have zero or more reviews.
 * 
 * @see Configuration
 */
@Entity
@Table(name = "review")
@Schema(description = "Review entity representing configuration review")
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @Schema(description = "Unique ID")
  public Long id;
  
  @Column(name = "rating")
  @Schema(description = "Review rating")
  public int rating;

  @Column(name = "text")
  @Schema(description = "Review text")
  public String text;

  @ManyToOne
  @JsonManagedReference
  @JoinColumn(name = "user_id")
  @Schema(description = "Review author")
  private User user;

  @ManyToOne
  @JsonManagedReference
  @JoinColumn(name = "configuration_id")
  @Schema(description = "Review object")
  private Configuration configuration;

  /**
   * Constructor for the Review class. This default constructor is required by JPA.
   */
  public Review() {
    // Intentionally left blank
  }

  /**
   * Constructor for the Review class.
   * 
   * @param rating The specified rating
   * @param text The specified text
   */
  public Review(int rating, String text) {
    this.rating = rating;
    this.text = text;
  }

  /**
   * Getter for ID.
   * 
   * @return ID
   */
  public Long getId() {
    return this.id;
  }

  /**
   * Getter for rating.
   * 
   * @return Rating
   */
  public int getRating() {
    return this.rating;
  }

  /**
   * Setter for rating.
   * 
   * @param rating The specified rating
   */
  public void setRating(int rating) {
    this.rating = rating;
  }

  /**
   * Getter for text.
   * 
   * @return Text
   */
  public String getText() {
    return this.text;
  }

  /**
   * Setter for text.
   * 
   * @param text The specified text
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Getter for user.
   * 
   * @return User
   */
  public User getUser() {
    return this.user;
  }

  /**
   * Setter for user.
   * 
   * @param user The specified user
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Getter for configuraiton.
   * 
   * @return Configuration
   */
  public Configuration getConfiguration() {
    return this.configuration;
  }

  /**
   * Setter for configuration.
   * 
   * @param configuration The specified configuration
   */
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  /**
   * Checks if review is valid.
   * 
   * @return True if review is valid or false otherwise
   */
  public boolean isValid() {
    return this.rating > 0 && this.rating <= 5 && this.text != null && !this.text.isBlank();
  }
}
