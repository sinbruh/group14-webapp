package no.ntnu.project.group14.webapp.dto;

import no.ntnu.project.group14.webapp.entities.Configuration;

/**
 * The ConfigurationDto class represents the data transfer object (DTO) for
 * {@link Configuration configurations}.
 */
public class ConfigurationDto {
  private String make;
  private String model;
  private String name;
  private String fuelType;
  private String transmissionType;
  private int numberOfSeats;
  private double price;

  /**
   * Constructor for the ConfigurationDto class.
   * 
   * @param make The specified car make
   * @param model The specified car model
   * @param name The specified name
   * @param fuelType The specified fuel type
   * @param transmissionType The specified transmission type
   * @param numberOfSeats The specified number of seats
   * @param price The specified lowest rental object price
   */
  public ConfigurationDto(
    String make,
    String model,
    String name,
    String fuelType,
    String transmissionType,
    int numberOfSeats,
    double price
  ) {
    this.make = make;
    this.model = model;
    this.name = name;
    this.fuelType = fuelType;
    this.transmissionType = transmissionType;
    this.numberOfSeats = numberOfSeats;
    this.price = price;
  }

  /**
   * Getter for car make.
   * 
   * @return Car make
   */
  public String getMake() {
    return make;
  }

  /**
   * Getter for car model.
   * 
   * @return Car model
   */
  public String getModel() {
    return model;
  }

  /**
   * Getter for name.
   * 
   * @return Name
   */
  public String getName() {
    return name;
  }

  /**
   * Getter for fuel type.
   * 
   * @return Fuel type
   */
  public String getFuelType() {
    return fuelType;
  }

  /**
   * Getter for transmission type.
   * 
   * @return Transmission type
   */
  public String getTransmissionType() {
    return transmissionType;
  }

  /**
   * Getter for number of seats.
   * 
   * @return Number of seats
   */
  public int getNumberOfSeats() {
    return numberOfSeats;
  }

  /**
   * Getter for lowest rental object price.
   * 
   * @return Lowest rental object price
   */
  public double getPrice() {
    return price;
  }
}
