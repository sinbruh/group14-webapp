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
 * The ExtraFeature class represents the entity for extra features. Extra features represent
 * features {@link Configuration configurations} can have in addition to their base features.
 */
@Entity
@Table(name = "extra_feature")
@Schema(description = "Extra feature entity representing extra configuration features")
public class ExtraFeature {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "extra_feature_id")
  @Schema(description = "Unique ID")
  private Long id;

  @Column(name = "name")
  @Schema(description = "Extra feature name")
  private String name;

  @ManyToOne
  @JsonManagedReference
  @JoinColumn(name = "configuration_id")
  @Schema(description = "Configuration providing extra feature")
  private Configuration configuration;

  /**
   * Constructor for the ExtraFeature class. This default constructor is required by JPA.
   */
  public ExtraFeature() {
    // Intentionally left blank
  }

  /**
   * Constructor for the ExtraFeature
   *
   * @param name The specified name
   */
  public ExtraFeature(String name) {
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
   * Getter for configuration.
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
   * Checks if extra feature is valid.
   *
   * @return True if extra feature is valid or false otherwise
   */
  public boolean isValid() {
    return this.name != null && !this.name.isBlank();
  }
}
