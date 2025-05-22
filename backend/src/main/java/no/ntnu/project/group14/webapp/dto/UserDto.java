package no.ntnu.project.group14.webapp.dto;

import java.sql.Date;
import java.util.Set;

import no.ntnu.project.group14.webapp.entities.Configuration;
import no.ntnu.project.group14.webapp.entities.Rental;
import no.ntnu.project.group14.webapp.entities.Review;
import no.ntnu.project.group14.webapp.entities.Role;
import no.ntnu.project.group14.webapp.entities.User;

/**
 * The UserDto class represents the data transfer object (DTO) for receiving {@link User user} data.
 */
public class UserDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private int phoneNumber;
  private Date dateOfBirth;
  private boolean active;
  private Set<Rental> rentals;
  private Set<Review> reviews;
  private Set<Role> roles;
  private Set<Configuration> favorites;

  /**
   * Constructor for the UserDto class.
   *
   * @param id          The specified ID
   * @param firstName   The specified first name
   * @param lastName    The specified last name
   * @param email       The specified email
   * @param phoneNumber The specified phone number
   * @param dateOfBirth The specified date of birth
   * @param active      The specified active status
   * @param rentals     The specified rentals
   * @param reviews     The specified reviews
   * @param roles       The specified roles
   * @param favorites   The specified favorites
   */
  public UserDto(
    Long id,
    String firstName,
    String lastName,
    String email,
    int phoneNumber,
    Date dateOfBirth,
    boolean active,
    Set<Rental> rentals,
    Set<Review> reviews,
    Set<Role> roles,
    Set<Configuration> favorites
  ) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.dateOfBirth = dateOfBirth;
    this.active = active;
    this.rentals = rentals;
    this.reviews = reviews;
    this.roles = roles;
    this.favorites = favorites;
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
   * Getter for last name.
   *
   * @return Last name
   */
  public String getLastName() {
    return this.lastName;
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
   * Getter for phone number.
   *
   * @return Phone number
   */
  public int getPhoneNumber() {
    return this.phoneNumber;
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
   * Checks if the user is active.
   *
   * @return True if the user is active or false otherwise
   */
  public boolean isActive() {
    return this.active;
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
   * Getter for reviews.
   * 
   * @return Reviews
   */
  public Set<Review> getReviews() {
    return this.reviews;
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
}
