package no.ntnu.project.group14.webapp.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The Configuration class represents the entity for configurations. Configurations represent the
 * different variations of a {@link Car car}. Each configuration is provided in the application
 * through the distribution of {@link RentalObject rental objects}.
 */
@Entity
@Table(name = "configuration")
@Schema(description = "Configuration entity representing a car variation")
public class Configuration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "configuration_id")
  @Schema(description = "Unique ID")
  private Long id;

  @Column(name = "name")
  @Schema(description = "Configuration name")
  private String name;

  @Column(name = "fuel_type")
  @Schema(description = "Configuration fuel type")
  private String fuelType;

  @Column(name = "transmission_type")
  @Schema(description = "Configuration transmission type")
  private String transmissionType;

  @Column(name = "number_of_seats")
  @Schema(description = "Number of seats in the configuration")
  private int numberOfSeats;

  @ManyToOne
  @JoinColumn(name = "car_id")
  @Schema(description = "Car providing configuration")
  private Car car;

  @OneToMany(mappedBy = "configuration")
  @Schema(description = "Extra features in configuration")
  private Set<ExtraFeature> extraFeatures = new LinkedHashSet<>();

  @OneToMany(mappedBy = "configuration")
  @Schema(description = "Rental objects distributing configuration")
  private Set<RentalObject> rentalObjects = new LinkedHashSet<>();

  @OneToMany(mappedBy = "configuration")
  @Schema(description = "Reviews placed on configuration")
  private Set<Review> reviews = new LinkedHashSet<>();

  @ManyToMany(mappedBy = "favorites")
  @Schema(description = "Users favorited configuration")
  private Set<User> favoritedUsers = new LinkedHashSet<>();

  /**
   * Constructor for the Configuration class. This default constructor is required by JPA.
   */
  public Configuration() {
    // Intentionally left blank
  }

  /**
   * Constructor for the Configuration class.
   *
   * @param name             The specified name
   * @param fuelType         The specified fuel type
   * @param transmissionType The specified transmission type
   * @param numberOfSeats    The specified number of seats
   */
  public Configuration(String name, String fuelType, String transmissionType, int numberOfSeats) {
    this.name = name;
    this.fuelType = fuelType;
    this.transmissionType = transmissionType;
    this.numberOfSeats = numberOfSeats;
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
   * Getter for name.
   *
   * @return Name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Setter for name.
   * 
   * @param name The specified name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getter for fuel type.
   *
   * @return Fuel type
   */
  public String getFuelType() {
    return this.fuelType;
  }

  /**
   * Setter for fuel type.
   * 
   * @param fuelType Fuel type
   */
  public void setFuelType(String fuelType) {
    this.fuelType = fuelType;
  }

  /**
   * Getter for transmission type.
   *
   * @return Transmission type
   */
  public String getTransmissionType() {
    return this.transmissionType;
  }

  /**
   * Setter for transmission type.
   * 
   * @param transmissionType The specified transmission type
   */
  public void setTransmissionType(String transmissionType) {
    this.transmissionType = transmissionType;
  }

  /**
   * Getter for number of seats.
   *
   * @return Number of seats
   */
  public int getNumberOfSeats() {
    return this.numberOfSeats;
  }

  /**
   * Setter for number of seats.
   * 
   * @param numberOfSeats The specified number of seats
   */
  public void setNumberOfSeats(int numberOfSeats) {
    this.numberOfSeats = numberOfSeats;
  }

  /**
   * Getter for car.
   *
   * @return Car
   */
  public Car getCar() {
    return this.car;
  }

  /**
   * Getter for extra features.
   *
   * @return Extra features
   */
  public Set<ExtraFeature> getExtraFeatures() {
    return this.extraFeatures;
  }

  /**
   * Getter for rental objects.
   *
   * @return Rental objects
   */
  public Set<RentalObject> getRentalObjects() {
    return this.rentalObjects;
  }

  /**
   * Getter for reviews.
   * 
   * @return Reviews
   */
  public Set<Review> getReviews() {
    return this.reviews;
  }

  /**
   * Getter for favorited users.
   * 
   * @return Favorited users
   */
  public Set<User> getFavoritedUsers() {
    return this.favoritedUsers;
  }

  /**
   * Checks if configuration is valid.
   *
   * @return True if configuration is valid or false otherwise
   */
  public boolean isValid() {
    return this.name != null && !this.name.isBlank() && this.fuelType != null
        && !this.fuelType.isBlank() && this.transmissionType != null
        && !this.transmissionType.isBlank() && this.numberOfSeats > 0;
  }
}
