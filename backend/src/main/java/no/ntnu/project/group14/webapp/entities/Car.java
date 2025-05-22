package no.ntnu.project.group14.webapp.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The Car class represents the entity for cars. Each car is further specified by a set of one or
 * more {@link Configuration configurations}.
 */
@Entity
@Table(name = "car")
@Schema(description = "Car entity representing car")
public class Car {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "car_id")
  @Schema(description = "Unique ID")
  private Long id;

  @Column(name = "make")
  @Schema(description = "Car make")
  private String make;

  @Column(name = "model")
  @Schema(description = "Car model")
  private String model;

  @Column(name = "year")
  @Schema(description = "Car model release year")
  private int year;

  @OneToMany(mappedBy = "car")
  @Schema(description = "Car configurations")
  private Set<Configuration> configurations = new LinkedHashSet<>();

  /**
   * Constructor for the Car class. This default constructor is required by JPA.
   */
  public Car() {
    // Intentionally left blank
  }

  /**
   * Constructor for the Car class.
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
   * Setter for make.
   * 
   * @param make The specified make
   */
  public void setMake(String make) {
    this.make = make;
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
   * Setter for model.
   * 
   * @param model The specified model
   */
  public void setModel(String model) {
    this.model = model;
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
   * Setter for year.
   * 
   * @param year The specified year
   */
  public void setYear(int year) {
    this.year = year;
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
   * Checks if car is valid.
   *
   * @return True if car is valid or false otherwise
   */
  public boolean isValid() {
    return this.make != null && !this.make.isBlank() && this.model != null && !this.model.isBlank()
        && this.year >= 0;
  }
}
