package no.ntnu.project.group14.webapp.entities;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The RentalObject class represents the entity for rental objects. Rental objects represents the
 * product being distributed in the application.
 */
@Entity
@Table(name = "rental_object")
@Schema(description = "Rental object entity representing distributed product")
public class RentalObject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rental_object_id")
  @Schema(description = "Unique ID")
  private Long id;

  @Column(name = "price")
  @Schema(description = "Rental object price")
  private double price;

  @Column(name = "avaiable")
  @Schema(description = "Rental object availability in UI")
  private boolean available;

  @Column(name = "visible")
  @Schema(description = "Rental object visibility in UI")
  private boolean visible;

  @ManyToOne
  @JoinColumn(name = "provider_id")
  @Schema(description = "Provider distributing rental object")
  private Provider provider;

  @ManyToOne
  @JoinColumn(name = "configuration_id")
  @Schema(description = "Configuration being distributed in rental object")
  private Configuration configuration;

  @OneToMany(mappedBy = "rentalObject")
  @Schema(description = "Rentals renting rental object")
  private Set<Rental> rentals;

  /**
   * Constructor for the RentalObject class. This default constructor is required by JPA.
   */
  public RentalObject() {
    // Intentionally left blank
  }

  /**
   * Constructor for the RentalObject class. In this constructor, availability and visibility are
   * set to <code>true</code> by default.
   * 
   * @param price The specified price
   */
  public RentalObject(double price) {
    this.price = price;
    this.available = true;
    this.visible = true;
  }

  /**
   * Constructor for the RentalObject class.
   * 
   * @param price     The specified price
   * @param available The specified availability
   * @param visible   The specified visibility
   */
  public RentalObject(double price, boolean available, boolean visible) {
    this.price = price;
    this.available = available;
    this.visible = visible;
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
   * Getter for price.
   * 
   * @return Price
   */
  public double getPrice() {
    return this.price;
  }

  /**
   * Setter for price.
   * 
   * @param price The specified price
   */
  public void setPrice(double price) {
    this.price = price;
  }

  /**
   * Checks if rental object is available.
   * 
   * @return True if rental object is available or false otherwise
   */
  public boolean isAvailable() {
    return available;
  }

  /**
   * Setter for availability.
   * 
   * @param available The specified availability
   */
  public void setAvailable(boolean available) {
    this.available = available;
  }

  /**
   * Checks if rental object is visible.
   * 
   * @return True if rental object is visible or false otherwise
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * Setter for visibility.
   * 
   * @param visible The specified visibility
   */
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  /**
   * Getter for provider.
   * 
   * @return Provider
   */
  public Provider getProvider() {
    return this.provider;
  }

  /**
   * Getter for configuration.
   * 
   * @return Configuration
   */
  public Configuration getConfiguration() {
    return this.configuration;
  }

  /**
   * Getter for rentals.
   * 
   * @return Rentals
   */
  public Set<Rental> getRentals() {
    return this.rentals;
  }

  /**
   * Checks if rental object is valid.
   * 
   * @return True if rental object is valid or false otherwise
   */
  public boolean isValid() {
    return this.price >= 0.0;
  }
}
