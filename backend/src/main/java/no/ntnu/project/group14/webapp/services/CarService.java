package no.ntnu.project.group14.webapp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.ntnu.project.group14.webapp.entities.Car;
import no.ntnu.project.group14.webapp.repositories.CarRepository;

/**
 * The CarService class represents the service for {@link Car cars}.
 */
@Service
@Transactional(readOnly = true)
public class CarService {

  @Autowired
  private CarRepository carRepository;

  /**
   * Gets all cars.
   * 
   * @return All cars
   */
  public Iterable<Car> getAll() {
    return this.carRepository.findAll();
  }

  /**
   * Gets the car with the specified ID.
   * 
   * @param id The specified ID
   * @return The car
   */
  public Optional<Car> get(Long id) {
    return this.carRepository.findById(id);
  }

  /**
   * Adds the specified car. The specified car is only added if it is valid.
   * 
   * @param car The specified car
   * @return The generated ID if the specified car is valid
   * @throws IllegalArgumentException If the specified car is invalid
   */
  @Transactional
  public Long add(Car car) {
    if (!car.isValid()) {
      throw new IllegalArgumentException("Car is invalid");
    }
    this.carRepository.save(car);
    return car.getId();
  }

  /**
   * Updates the car with the specified ID with the specified car. The car is only updated if a car
   * with the specified ID exists and the specified car is valid.
   * 
   * @param id The specified ID
   * @param car The specified car
   * @return True if the car exists and is updated or false otherwise
   * @throws IllegalArgumentException If the specified car is invalid
   */
  @Transactional
  public boolean update(Long id, Car car) {
    if (!car.isValid()) {
      throw new IllegalArgumentException("Car is invalid");
    }
    Optional<Car> existingCar = this.carRepository.findById(id);
    boolean exist = existingCar.isPresent();
    if (exist) {
      Car storedCar = existingCar.get();
      storedCar.setMake(car.getMake());
      storedCar.setModel(car.getModel());
      storedCar.setYear(car.getYear());
      this.carRepository.save(storedCar);
    }
    return exist;
  }

  /**
   * Deletes the car with the specified ID. The car is only deleted if a car with the specified ID
   * exists.
   * 
   * @param id The specified ID
   * @return True if the car exists and is deleted or false otherwise
   */
  @Transactional
  public boolean delete(Long id) {
    Optional<Car> car = this.carRepository.findById(id);
    boolean exist = car.isPresent();
    if (exist) {
      this.carRepository.deleteById(id);
    }
    return exist;
  }
}
