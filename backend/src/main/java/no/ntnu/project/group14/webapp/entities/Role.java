package no.ntnu.project.group14.webapp.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * The Role class represents the entity for roles. Roles represent user roles and each
 * {@link User user} has one or more role.
 */
@Entity(name = "role")
@Schema(description = "Role entity representing user roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  @Schema(description = "Unique ID")
  private Long id;

  @Column(name = "name")
  @Schema(description = "Role name")
  private String name;

  @ManyToMany(mappedBy = "roles")
  @JsonBackReference
  @Schema(description = "Users with role")
  private Set<User> users = new LinkedHashSet<>();

  /**
   * Constructor for the Role class. This default constructor is required by JPA.
   */
  public Role() {
    // Intentionally left blank
  }

  /**
   * Constructor for the Role class.
   *
   * @param name The specified name
   */
  public Role(String name) {
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
   * Getter for users.
   *
   * @return Users
   */
  public Set<User> getUsers() {
    return this.users;
  }

  /**
   * Checks if role is valid.
   *
   * @return True if role is valid or false otherwise
   */
  public boolean isValid() {
    return this.name != null && !this.name.isBlank();
  }
}
