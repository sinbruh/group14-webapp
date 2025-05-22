package no.ntnu.project.group14.webapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * The Rental class represents the entity for rentals. Each rental represents is of a
 * {@link RentalObject rental object}.
 */
@Entity
@Table(name = "rental")
@Schema(description = "Rental entity representing rental object purchases")
public class Rental {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rental_id")
  @Schema(description = "Unique ID")
  private Long id;

  @Column(name = "start_time")
  @Schema(description = "Rental start time")
  private long startTime;

  @Column(name = "end_time")
  @Schema(description = "Rental end time")
  private long endTime;

  @ManyToOne
  @JsonManagedReference
  @JoinColumn(name = "pickup_location_id")
  @Schema(description = "Rental pick up location")
  private Location pickUpLocation;

  @ManyToOne
  @JsonManagedReference
  @JoinColumn(name = "dropoff_location_id")
  @Schema(description = "Rental drop off location")
  private Location dropOffLocation;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "rental_object_id")
  @Schema(description = "Rental object rented")
  private RentalObject rentalObject;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "user_id")
  @Schema(description = "User renting rental object")
  private User user;

  /**
   * Constructor for the Rental class. This default constructor is required by JPA.
   */
  public Rental() {
    // Intentionally left blank
  }

  /**
   * Constructor for the rental class.
   *
   * @param startTime The specified start time
   * @param endTime   The specified end time
   */
  public Rental(long startTime, long endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
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
   * Getter for start time.
   *
   * @return Start time
   */
  public long getStartTime() {
    return this.startTime;
  }

  /**
   * Getter for end time.
   *
   * @return End time
   */
  public long getEndTime() {
    return this.endTime;
  }

  /**
   * Getter for pick up location.
   * 
   * @return Pick up location
   */
  public Location getPickUpLocation() {
    return this.pickUpLocation;
  }

  /**
   * Getter for drop off location.
   * 
   * @return Drop off location
   */
  public Location getDropOffLocation() {
    return this.dropOffLocation;
  }

  /**
   * Getter for rental object.
   *
   * @return Rental object
   */
  public RentalObject getRentalObject() {
    return this.rentalObject;
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
   * Checks if rental is valid.
   *
   * @return True if the rental is valid or false otherwise
   */
  public boolean isValid() {
    return this.startTime >= 0 && this.endTime >= 0;
  }
}
