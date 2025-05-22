package no.ntnu.project.group14.webapp.entities;

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

import java.sql.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * The User class represents the entity for users. Users can have zero or more
 * {@link Rental rentals}.
 */
@Entity
@Table(name = "user")
@Schema(description = "User entity representing user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  @Schema(description = "Unique ID")
  private Long id;

  @Column(name = "first_name")
  @Schema(description = "User first name")
  private String firstName;

  @Column(name = "last_name")
  @Schema(description = "User last name")
  private String lastName;

  @Column(name = "email")
  @Schema(description = "User email")
  private String email;

  @Column(name = "phone_number")
  @Schema(description = "User phone number")
  private int phoneNumber;

  @Column(name = "password")
  @Schema(description = "User password")
  private String password;

  @Column(name = "date_of_birth")
  @Schema(description = "User date of birth")
  private Date dateOfBirth;

  @Column(name = "active_status")
  @Schema(description = "User active status")
  private boolean active = true;

  @OneToMany(mappedBy = "user")
  @JsonManagedReference
  @Schema(description = "User rentals")
  private Set<Rental> rentals = new LinkedHashSet<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JsonManagedReference
  @JoinTable(
    name = "user_role",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  @Schema(description = "User roles")
  private Set<Role> roles = new LinkedHashSet<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JsonManagedReference
  @JoinTable(
    name = "favorite",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "provider_id")
  )
  @Schema(description = "Configurations favorited by user")
  private Set<Configuration> favorites = new LinkedHashSet<>();

  /**
   * Constructor for the User class. This default constructor is required by JPA.
   */
  public User() {
    // Intentionally left blank
  }

  /**
   * Constructor for the User class.
   *
   * @param firstName       The specified first name
   * @param lastName        The specfifed last name
   * @param email           The specified email
   * @param phoneNumber     The specified phone number
   * @param password        The specified password
   * @param unixDateOfBirth The specified date of birth as a UNIX timestamp
   */
  public User(
    String firstName,
    String lastName,
    String email,
    int phoneNumber,
    String password,
    long unixDateOfBirth
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.password = password;
    this.dateOfBirth = new Date(unixDateOfBirth);
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
   * Getter for first name.
   *
   * @return First name
   */
  public String getFirstName() {
    return this.firstName;
  }

  /**
   * Setter for first name.
   *
   * @param firstName The specified first name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Getter for last name.
   *
   * @return Last name
   */
  public String getLastName() {
    return this.lastName;
  }

  /**
   * Setter for last name.
   *
   * @param lastName The specified last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Getter for email.
   *
   * @return Email
   */
  public String getEmail() {
    return this.email;
  }

  /**
   * Setter for email.
   *
   * @param email The specified email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Getter for phone number.
   *
   * @return Phone number
   */
  public int getPhoneNumber() {
    return this.phoneNumber;
  }

  /**
   * Setter for phone number.
   *
   * @param phoneNumber The specified phone number
   */
  public void setPhoneNumber(int phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Getter for password.
   *
   * @return Password
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Setter for password.
   *
   * @param password The specified password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Getter for date of birth.
   *
   * @return Date of birth
   */
  public Date getDateOfBirth() {
    return this.dateOfBirth;
  }

  /**
   * Setter for date of birth.
   *
   * @param dateOfBirth The specified date of birth as a UNIX timestamp
   */
  public void setDateOfBirth(long unixDateOfBirth) {
    this.dateOfBirth = new Date(unixDateOfBirth);
  }

  /**
   * Checks if the user is active.
   *
   * @return True if the user is active or false otherwise
   */
  public boolean isActive() {
    return this.active;
  }

  /**
   * Setter for active status.
   *
   * @param active The specified active status
   */
  public void setActive(boolean active) {
    this.active = active;
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
   * Getter for roles.
   *
   * @return Roles
   */
  public Set<Role> getRoles() {
    return this.roles;
  }

  /**
   * Getter for favorites.
   *
   * @return Favorites
   */
  public Set<Configuration> getFavorites() {
    return this.favorites;
  }

  /**
   * Add the specified role to the user.
   *
   * @param role The specified role
   */
  public void addRole(Role role) {
    this.roles.add(role);
  }

  /**
   * Add the specified configuration to the user favorites.
   *
   * @param configuraiton The specified configuration
   */
  public void addFavorite(Configuration configuration) {
    this.favorites.add(configuration);
  }

  /**
   * Remove the specified configuraiton from the user favorites.
   *
   * @param configuration The specified configuration
   */
  public void removeFavorite(Configuration configuration) {
    this.favorites.remove(configuration);
  }
  
  /**
   * Checks if the user has the admin role.
   *
   * @return True if the user has the admin role or false otherwise
   */
  public boolean isAdmin() {
    return this.hasRole("ROLE_ADMIN");
  }

  /**
   * Checks if the user has the specified role.
   *
   * @param roleName The specified role
   * @return True if the user has the specified role or false otherwise
   */
  private boolean hasRole(String roleName) {
    boolean found = false;
    Iterator<Role> it = this.roles.iterator();
    while (!found && it.hasNext()) {
      Role role = it.next();
      if (role.getName().equals(roleName)) {
        found = true;
      }
    }
    return found;
  }

  /**
   * Checks if user is valid.
   *
   * @return True if user is valid or false otherwise
   */
  public boolean isValid() {
    return this.firstName != null && !this.firstName.isBlank() && this.lastName != null
        && !this.lastName.isBlank() && this.email != null && !this.email.isBlank()
        && this.phoneNumber > 0 && this.password != null && !this.password.isBlank()
        && this.dateOfBirth != null;
  }
}
