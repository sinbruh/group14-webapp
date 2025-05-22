package no.ntnu.project.group14.webapp.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The Provider class represents the entity for providers. Providers represent the product
 * distributors in the application. Each provider distributes a set of one or more
 * {@link RentalObject rental objects}.
 */
@Entity
@Table(name = "provider")
@Schema(description = "Provider entity representing product distributor")
public class Provider {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "provider_id")
  @Schema(description = "Unique ID")
  private Long id;

  @Column(name = "name")
  @Schema(description = "Provider name")
  private String name;

  @OneToMany(mappedBy = "provider")
  @Schema(description = "Rental objects distributed by provider")
  private Set<RentalObject> rentalObjects = new LinkedHashSet<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "provider_region",
    joinColumns = @JoinColumn(name = "provider_id"),
    inverseJoinColumns = @JoinColumn(name = "region_id")
  )
  @Schema(description = "Provider operating regions")
  private Set<Region> regions = new LinkedHashSet<>();

  /**
   * Constructor for the Provider class. This default constructor is required by JPA.
   */
  public Provider() {
    // Intentionally left blank
  }

  /**
   * Constructor for the Provider class.
   *
   * @param name The specified name
   */
  public Provider(String name) {
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
   * Setter for name.
   * 
   * @param name The specified name
   */
  public void setName(String name) {
    this.name = name;
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
   * Checks if provider is valid.
   *
   * @return True if provider is valid or false otherwise
   */
  public boolean isValid() {
    return this.name != null && !this.name.isBlank();
  }
}
