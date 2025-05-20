package no.ntnu.project.group14.webapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// TODO Update JavaDoc for rentals
/**
 * The Location class represents the entity for locations. Locations are included in zero or more
 * rentals.
 */
@Entity
@Table(name = "location")
@Schema(description = "Location entity representing location in region")
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "location_id")
  @Schema(description = "Unique ID")
  private Long id;

  @Column(name = "name")
  @Schema(description = "Location name")
  private String name;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "region_id")
  @Schema(description = "Region containing location")
  private Region region;

  /**
   * Constructor for the Location class. This default constructor is required by JPA.
   */
  public Location() {
    // Intentionally left blank
  }

  /**
   * Constructor for the Location class.
   * 
   * @param name The specified name
   */
  public Location(String name) {
    this.name = name;
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
   * Getter for region.
   * 
   * @return Region
   */
  public Region getRegion() {
    return this.region;
  }

  /**
   * Checks if location is valid.
   * 
   * @return True if location is valid or false otherwise
   */
  public boolean isValid() {
    return this.name != null && !this.name.isBlank();
  }
}
