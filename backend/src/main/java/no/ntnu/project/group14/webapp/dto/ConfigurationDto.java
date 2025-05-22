package no.ntnu.project.group14.webapp.dto;


/**
 * The ConfigurationDto class represents the data transfer object (DTO) for receiving
 * configuration data. 
 *
 * @author Group 14
 * @version v1.0 (2025.05.22)
 */
public class ConfigurationDto {
    private String name;
    private String make;
    private String model;
    private int numberOfSeats;
    private String fuelType;
    private String transmissionType;
    private double price;

    /**
     * Constructor for the ConfigurationDto class.
     * This constructor is used to create a ConfigurationDto object with the specified parameters.
     * @param name the name of a car model
     * @param make the make of a car
     * @param model the model of a car
     * @param numberOfSeats the number of seats in a car
     * @param fuelType the fuel type of a car
     * @param transmissionType the transmission type of a car
     * @param price the price of a car
     */
    public ConfigurationDto(String name, String make, String model, int numberOfSeats,
                            String fuelType, String transmissionType, double price) {
        this.name = name;
        this.make = make;
        this.model = model;
        this.numberOfSeats = numberOfSeats;
        this.fuelType = fuelType;
        this.transmissionType = transmissionType;
        this.price = price;
    }

    /**
     * Getter for name.
     * 
     * @return the specified name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     * 
     * @param name the specified name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for make.
     * 
     * @return the specified make
     */
    public String getMake() {
        return make;
    }

    /**
     * Setter for make.
     * 
     * @param make the specified make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Getter for model.
     * 
     * @return the specified model
     */
    public String getModel() {
        return model;
    }

    /**
     * Setter for model.
     * 
     * @param model the specified model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Getter for number of seats.
     * 
     * @return number of seats
     */
    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    /**
     * Setter for number of seats
     * 
     * @param numberOfSeats number of seats
     */
    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    /**
     * Getter for fuel type.
     * 
     * @return fuel type
     */
    public String getFuelType() {
        return fuelType;
    }

    /**
     * Setter for fuel type.
     * 
     * @param fuelType fuel type
     */
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * Getter for transmission type.
     * 
     * @return transmission type
     */
    public String getTransmissionType() {
        return transmissionType;
    }

    /**
     * Setter for transmission type.
     * 
     * @param transmissionType transmission type
     */
    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    /**
     * Getter for price.
     * 
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Setter for price
     * 
     * @param price price
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
