package no.ntnu.project.group14.webapp.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * The Region class represents the entity for regions. Regions represents areas containing one or
 * more locations.
 */
@Entity
@Table(name = "region")
@Schema(description = "Region entity representing location region")
public class Region {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "region_id")
  @Schema(description = "Unique ID")
  private Long id;

  @Column(name = "name")
  @Schema(description = "Region name")
  private String name;

  @ManyToMany(mappedBy = "regions")
  @JsonBackReference
  @Schema(description = "Providers operating in region")
  private Set<Provider> providers = new LinkedHashSet<>();

  /**
   * Constructor for the Region class. This default constructor is required by JPA.
   */
  public Region() {
    // Intentionally left blank
  }

  /**
   * Constructor for the Region class.
   * 
   * @param name The specified name
   */
  public Region(String name) {
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
   * Checks if region is valid.
   * 
   * @return True if region is valid or false otherwise
   */
  public boolean isValid() {
    return this.name != null && !this.name.isBlank();
  }
}
