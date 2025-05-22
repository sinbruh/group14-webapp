package no.ntnu.project.group14.webapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import no.ntnu.project.group14.webapp.dto.ConfigurationDto;
import no.ntnu.project.group14.webapp.entities.Car;
import no.ntnu.project.group14.webapp.entities.Configuration;
import no.ntnu.project.group14.webapp.entities.RentalObject;
import no.ntnu.project.group14.webapp.entities.User;
import no.ntnu.project.group14.webapp.services.AccessUserService;
import no.ntnu.project.group14.webapp.services.CarService;
import no.ntnu.project.group14.webapp.services.ConfigurationService;
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

/**
 * The ConfigurationController class represents the REST controller for
 * {@link Configuration configurations}.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/configurations")
public class ConfigurationController {

  @Autowired
  private ConfigurationService configurationService;

  @Autowired
  private CarService carService;

  @Autowired
  private AccessUserService accessUserService;

  private final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

  /**
   * Endpoint for getting all configuraitons for search. The configuraitons are sent in the form of
   * {@link ConfigurationDto DTOs} containing only required data.
   *
   * @return <p><b>200 OK</b> (<i>body:</i> all configurations)</p>
   */
  @Operation(
      summary = "Get configurations",
      description = "Gets all configurations"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains all configuraitons"
    )
  })
  @GetMapping("/search")
  public Iterable<ConfigurationDto> getSearch() {
    Iterable<Configuration> configurations = this.configurationService.getAll();
    List<ConfigurationDto> dtos = new ArrayList<>();
    for (Configuration configuration : configurations) {
      double price = 9999;
      // Search for rental object with lowest price
      for (RentalObject rentalObject : configuration.getRentalObjects()) {
        double rentalPrice = rentalObject.getPrice();
        if (rentalPrice < price) {
          price = rentalPrice;
        }
      }
      ConfigurationDto dto = new ConfigurationDto(
        configuration.getCar().getMake(),
        configuration.getCar().getModel(),
        configuration.getName(),
        configuration.getFuelType(),
        configuration.getTransmissionType(),
        configuration.getNumberOfSeats(),
        price
      );
      dtos.add(dto);
    }
    this.logger.info("[GET] Sending all configuration data...");
    return dtos;
  }

  /**
   * Endpoint for getting all configurations.
   * 
   * @return <p><b>200 OK</b> (<i>body:</i> all configurations)</p>
   */
  @Operation(
    summary = "Get configurations",
    description = "Gets all configurations"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains all configurations"
    )
  })
  @GetMapping
  public Iterable<Configuration> getAll() {
    Iterable<Configuration> configurations = this.configurationService.getAll();
    this.logger.info("[GET] Sending all configurations...");
    return configurations;
  }

  /**
   * Endpoint for getting the configuration with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if configuration exists (<i>body:</i> configuration)</p>
   *         <li><b>404 NOT FOUND</b> if configuration does not exist</li>
   */
  @Operation(
    summary = "Get configuration",
    description = "Gets the configuration with the specified ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains configuration"
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Signals error"
    )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Object> get(
    @Parameter(description = "ID of configuration to get")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    Optional<Configuration> configuration = this.configurationService.get(id);
    if (configuration.isPresent()) {
      this.logger.info("[GET] Configuration exists, sending configuration...");
      response = ResponseEntity.ok().body(configuration.get());
    } else {
      this.logger.error("[GET] Configuration does not exist, sending error response...");
      response = ResponseEntity.notFound().build();
    }
    return response;
  }

  /**
   * Endpoint for adding the specified configuration to the car with the specified car ID.
   * 
   * @param carId         The specified car ID
   * @param configuration The specified configuration
   * @return <p><b>201 CREATED</b> if configuration is valid (<i>body:</i> generated ID of added
   *         configuration)</p>
   *         <li><p><b>400 BAD REQUEST</b> if configuration is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if car does not exist</p></li>
   */
  @Operation(
    summary = "Add configuration",
    description = "Adds the specified configuration"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Signals success and contains generated ID of added configuration"
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
  @PostMapping("/car/{carId}")
  public ResponseEntity<Object> add(
    @Parameter(description = "ID of car to add configuration to")
    @PathVariable Long carId,
    @Parameter(description = "Configuration to add")
    @RequestBody Configuration configuration
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isActive() && sessionUser.isAdmin()) {
      Optional<Car> car = this.carService.get(carId);
      if (car.isPresent()) {
        configuration.setCar(car.get());
        try {
          this.configurationService.add(configuration);
          this.logger.info(
            "[POST] Valid configuration, sending generated ID of added configuration..."
          );
          response = ResponseEntity.created(null).body(configuration.getId());
        } catch (IllegalArgumentException e) {
          this.logger.error("[POST] Invalid configuration, sending error message...");
          response = ResponseEntity.badRequest().body(e.getMessage());
        }
      } else {
        this.logger.error("[POST] Car does not exist, sending error response...");
        response = ResponseEntity.notFound().build();
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
   * Endpoint for updating the configuration with the specified ID with the specified update
   * configuration.
   * 
   * @param id            The specified ID
   * @param configuration The specified update configuration
   * @return <p><b>200 OK</b> if configuration exists and update configuration is valid</p>
   *         <li><p><b>400 BAD REQUEST</b> if update configuration is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if configuration does not exist</p></li>
   */
  @Operation(
    summary = "Update configuration",
    description = "Updates the configuration with the specified ID with the specified update "
                + "configuration"
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
    @Parameter(description = "ID of configuration to update")
    @PathVariable Long id,
    @Parameter(description = "Update configuration")
    @RequestBody Configuration configuration
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      try {
        if (this.configurationService.update(id, configuration)) {
          this.logger.info(
            "[PUT] Configuration exists and valid update configuration, sending success "
          + "response..."
          );
          response = ResponseEntity.ok().build();
        } else {
          this.logger.error("[PUT] Configuration does not exist, sending error response...");
          response = ResponseEntity.notFound().build();
        }
      } catch (IllegalArgumentException e) {
        this.logger.error("[PUT] Invalid update configuration, sending error message...");
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
   * Endpoint for deleting the configuration with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if configuration exists</b></p>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if configuration does not exist</p></li>
   */
  @Operation(
    summary = "Delete configuration",
    description = "Deletes the configuration with the specified ID"
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
    @Parameter(description = "ID of configuration to delete")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      if (this.configurationService.delete(id)) {
        this.logger.info("[DELETE] Configuration exists, sending success response...");
        response = ResponseEntity.ok().build();
      } else {
        this.logger.error("[DELETE] Configuration does not exist, sending error response...");
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
