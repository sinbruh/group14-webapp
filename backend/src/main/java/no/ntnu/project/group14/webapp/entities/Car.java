package no.ntnu.project.group14.webapp.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The Car class represents the entity for cars. Each car is further specified by a set of one or
 * more {@link Configuration configurations}.
 */
@Entity(name = "car")
@Schema(description = "Car entity representing car")
public class Car {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Unique ID")
  private Long id;

  @Schema(description = "Car make")
  private String make;

  @Schema(description = "Car model")
  private String model;

  @Schema(description = "Car model release year")
  private int year;

  @Schema(description = "Car configurations")
  @OneToMany(mappedBy = "car")
  private Set<Configuration> configurations = new LinkedHashSet<>();

  /**
   * Constructs an instance of the Car class.
   *
   * <p>Empty constructor needed for JPA.</p>
   */
  public Car() {
    // Intentionally left blank
  }

  /**
   * Constructs an instance of the Car class.
   *
   * @param make  The specified make
   * @param model The specified model
   * @param year  The specified year
   */
  public Car(String make, String model, int year) {
    this.make = make;
    this.model = model;
    this.year = year;
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
   * Getter for make.
   *
   * @return Make
   */
  public String getMake() {
    return this.make;
  }

  /**
   * Getter for model.
   *
   * @return Model
   */
  public String getModel() {
    return this.model;
  }

  /**
   * Getter for year.
   *
   * @return Year
   */
  public int getYear() {
    return this.year;
  }

  /**
   * Getter for configurations.
   *
   * @return Configurations
   */
  public Set<Configuration> getConfigurations() {
    return this.configurations;
  }

  /**
   * Returns true if the car is valid or false otherwise.
   *
   * @return True if the car is valid or false otherwise
   */
  public boolean isValid() {
    return !this.make.isBlank() && !this.model.isBlank() && this.year >= 0;
  }
}
