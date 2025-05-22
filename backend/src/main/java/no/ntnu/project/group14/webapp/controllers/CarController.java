package no.ntnu.project.group14.webapp.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import no.ntnu.project.group14.webapp.entities.Car;
import no.ntnu.project.group14.webapp.entities.User;
import no.ntnu.project.group14.webapp.services.AccessUserService;
import no.ntnu.project.group14.webapp.services.CarService;

/**
 * The CarController class represents the REST controller for {@link Car cars}. The class handles
 * all HTTP traffic its endpoints.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/cars")
public class CarController {

  @Autowired
  private CarService carService;

  @Autowired
  private AccessUserService accessUserService;

  private final Logger logger = LoggerFactory.getLogger(CarController.class);

  /**
   * Endpoint for getting all cars.
   * 
   * @return <p><b>200 OK</b> (<i>body:</i> all cars)</p>
   */
  @Operation(
    summary = "Get cars",
    description = "Gets all cars"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains all cars"
    )
  })
  @GetMapping
  public Iterable<Car> getAll() {
    Iterable<Car> cars = this.carService.getAll();
    this.logger.info("[GET] Sending all cars...");
    return cars;
  }

  /**
   * Endpoint for getting the car with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if car exists (<i>body:</i> car)</p>
   *         <li><b>404 NOT FOUND</b> if car does not exist</li>
   */
  @Operation(
    summary = "Get car",
    description = "Gets the car with the specified ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains car"
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Signals error"
    )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Object> get(
    @Parameter(description = "ID of car to get")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    Optional<Car> car = this.carService.get(id);
    if (car.isPresent()) {
      this.logger.info("[GET] Car exists, sending car...");
      response = ResponseEntity.ok().body(car.get());
    } else {
      this.logger.error("[GET] Car does not exist, sending error response...");
      response = ResponseEntity.notFound().build();
    }
    return response;
  }

  /**
   * Endpoint for adding the specified car.
   * 
   * @param car The specified car
   * @return <p><b>201 CREATED</b> if car is valid (<i>body:</i> generated ID of added car)</p>
   *         <li><p><b>400 BAD REQUEST</b> if car is invalid (<i>body:</i> error message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   */
  @Operation(
    summary = "Add car",
    description = "Adds the specified car"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Signals success and contains generated ID of added car"
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Signals error and contains error message"
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Signals error"
    ),
    @ApiResponse(
      responseCode = "403",
      description = "Signals error and contains error message"
    )
  })
  @PostMapping
  public ResponseEntity<Object> add(
    @Parameter(description = "Car to add")
    @RequestBody Car car
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isActive() && sessionUser.isAdmin()) {
      try {
        this.carService.add(car);
        this.logger.info("[POST] Valid car, sending generated ID of added car...");
        response = ResponseEntity.created(null).body(car.getId());
      } catch (IllegalArgumentException e) {
        this.logger.error("[POST] Invalid car, sending error message");
        response = ResponseEntity.badRequest().body(e.getMessage());
      }
    } else if (sessionUser == null) {
      this.logger.error("[POST] User not authenticated, sending error response...");
      response = ResponseEntity.status(401).build();
    } else if (!sessionUser.isActive()) {
      this.logger.error("[POST] User is deactivated, sending error message...");
      response = ResponseEntity.status(403).body("User deactivated");
    } else {
      this.logger.error("[POST] User not admin, sending error message...");
      response = ResponseEntity.status(403).body("User not admin");
    }
    return response;
  }

  /**
   * Endpoint for updating the car with the specified ID with the specified update car.
   * 
   * @param id  The specified ID
   * @param car The specified update car
   * @return <p><b>200 OK</b> if car exists and update car is valid</p>
   *         <li><p><b>400 BAD REQUEST</b> if update car is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FONUD</b> if car does not exist</p></li>
   */
  @Operation(
    summary = "Update car",
    description = "Updates the car with the specified ID with the specified update car"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success"
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Signals error and contains error message"
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Signals error"
    ),
    @ApiResponse(
      responseCode = "403",
      description = "Signals error and contains error message"
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Signals error"
    )
  })
  @PutMapping("/{id}")
  public ResponseEntity<Object> update(
    @Parameter(description = "ID of car to update")
    @PathVariable Long id,
    @Parameter(description = "Update car")
    @RequestBody Car car
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      try {
        if (this.carService.update(id, car)) {
          this.logger.info("[PUT] Car exists and valid update car, sending success response...");
          response = ResponseEntity.ok().build();
        } else {
          this.logger.error("[PUT] Car does not exist, sending error response...");
          response = ResponseEntity.notFound().build();
        }
      } catch (IllegalArgumentException e) {
        this.logger.error("[PUT] Invalid update car, sending error message...");
        response = ResponseEntity.badRequest().body(e.getMessage());
      }
    } else if (sessionUser == null) {
      this.logger.error("[PUT] User not authenticated, sending error response...");
      response = ResponseEntity.status(401).build();
    } else if (!sessionUser.isActive()) {
      this.logger.error("[PUT] User is deactivated, sending error message...");
      response = ResponseEntity.status(403).body("User deactivated");
    } else {
      this.logger.error("[PUT] User not admin, sending error message...");
      response = ResponseEntity.status(403).body("User not admin");
    }
    return response;
  }

  /**
   * Endpoint for deleting the car with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if car exists</b></p>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if car does not exist</p></li>
   */
  @Operation(
    summary = "Delete car",
    description = "Deletes the car with the specified ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success"
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Signals error"
    ),
    @ApiResponse(
      responseCode = "403",
      description = "Signals error and contains error message"
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Signals error"
    )
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(
    @Parameter(description = "ID of car to delete")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      if (this.carService.delete(id)) {
        this.logger.info("[DELETE] Car exists, sending success response...");
        response = ResponseEntity.ok().build();
      } else {
        this.logger.error("[DELETE] Car does not exist, sending error response...");
        response = ResponseEntity.notFound().build();
      }
    } else if (sessionUser == null) {
      this.logger.error("[DELETE] User not authenticated, sending error response...");
      response = ResponseEntity.status(401).build();
    } else if (!sessionUser.isActive()) {
      this.logger.error("[DELETE] User is deactivated, sending error message...");
      response = ResponseEntity.status(403).body("User deactivated");
    } else {
      this.logger.error("[DELETE] User not admin, sending error message...");
      response = ResponseEntity.status(403).body("User not admin");
    }
    return response;
  }

  /**
   * Exception handler for handling exceptions caused by invalid formatting of path variables. This
   * method sends a response to the request causing the specified exception.
   *
   * @param e The specified exception
   * @return <p><b>400 BAD REQUEST</b> (<i>body:</i> error message)<p>
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> handlePathVarException(MethodArgumentTypeMismatchException e) {
    this.logger.error(
      "[EXCEPTION] Received request contains invalid formatting, sending error message..."
    );
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  /**
   * Exception handler for handling exceptions caused by invalid formatting of request bodies. This
   * method sends a response to the request causing the specified exception.
   *
   * @param e The specified exception
   * @return <p><b>400 BAD REQUEST</b> (<i>body:</i> error message)</p>
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleRequestBodyException(HttpMessageNotReadableException e) {
    this.logger.error(
      "[EXCEPTION] Received request body contains invalid formatting, sending error message..."
    );
    return ResponseEntity.badRequest().body(e.getMessage());
  }
}
