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
import no.ntnu.project.group14.webapp.entities.Location;
import no.ntnu.project.group14.webapp.entities.Rental;
import no.ntnu.project.group14.webapp.entities.RentalObject;
import no.ntnu.project.group14.webapp.entities.User;
import no.ntnu.project.group14.webapp.services.AccessUserService;
import no.ntnu.project.group14.webapp.services.LocationService;
import no.ntnu.project.group14.webapp.services.RentalObjectService;
import no.ntnu.project.group14.webapp.services.RentalService;

@RestController
@CrossOrigin
@RequestMapping("/api/rentals")
public class RentalController {

  @Autowired
  private RentalService rentalService;

  @Autowired
  private RentalObjectService rentalObjectService;

  @Autowired
  private LocationService locationService;

  @Autowired
  private AccessUserService accessUserService;

  private final Logger logger = LoggerFactory.getLogger(RentalController.class);

  /**
   * Endpoint for getting all rentals.
   * 
   * @return <p><b>200 OK</b> (<i>body:</i> all rentals)</p>
   */
  @Operation(
    summary = "Get rentals",
    description = "Gets all rentals"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains all rentals"
    )
  })
  @GetMapping
  public Iterable<Rental> getAll() {
    Iterable<Rental> rentals = this.rentalService.getAll();
    this.logger.info("[GET] Sending all rentals...");
    return rentals;
  }

  /**
   * Endpoint for getting the rental with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if rental exists (<i>body:</i> rental)</p>
   *         <li><b>404 NOT FOUND</b> if rental does not exist</li>
   */
  @Operation(
    summary = "Get rental",
    description = "Gets the rental with the specified ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains rental"
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Signals error"
    )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Object> get(
    @Parameter(description = "ID of rental to get")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    Optional<Rental> rental = this.rentalService.get(id);
    if (rental.isPresent()) {
      this.logger.info("[GET] Rental exists, sending rental...");
      response = ResponseEntity.ok().body(rental.get());
    } else {
      this.logger.error("[GET] Rental does not exist, sending error response...");
      response = ResponseEntity.notFound().build();
    }
    return response;
  }

  /**
   * Endpoint for adding the specified rental to the rental object with the specified rental object
   * ID and to the pick up and drop off location with the specified pick up and drop off location
   * ID.
   * 
   * @param rentalObjectId    The specified rental object ID
   * @param pickUpLocationId  The specified pick up location ID
   * @param dropOffLocationId The specified drop off location ID
   * @param rental            The specified rental
   * @return <p><b>201 CREATED</b> if rental is valid (<i>body:</i> generated ID of added
   *         rental)</p>
   *         <li><p><b>400 BAD REQUEST</b> if rental is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin</p></li>
   *         <li><p><b>404 NOT FOUND</b> if rental object or pick up or drop off location do not
   *         exist (<i>body:</i> error message)</p></li>
   */
  @Operation(
    summary = "Add rental",
    description = "Adds the specified rental to the rental object with the specified rental "
                + "object ID and to the pick up and drop off location with the specified pick up "
                + "and drop off location ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Signals success and contains generated ID of added rental"
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
      description = "Signals error"
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Signals error and contains error message"
    )
  })
  @PostMapping(
    "/rental-object/{rentalObjectId}/pickup/{pickUpLocationId}/dropoff/{dropOffLocationId}"
  )
  public ResponseEntity<Object> add(
    @Parameter(description = "ID of rental object to add rental to")
    @PathVariable Long rentalObjectId,
    @Parameter(description = "ID of pick up location to add rental to")
    @PathVariable Long pickUpLocationId,
    @Parameter(description = "ID of drop off location to add rental to")
    @PathVariable Long dropOffLocationId,
    @Parameter(description = "Rental to add")
    @RequestBody Rental rental
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isActive()) {
      Optional<RentalObject> rentalObject = this.rentalObjectService.get(rentalObjectId);
      Optional<Location> pickUpLocation = locationService.get(pickUpLocationId);
      Optional<Location> dropOffLocation = locationService.get(dropOffLocationId);
      if (rentalObject.isPresent() && pickUpLocation.isPresent() && dropOffLocation.isPresent()) {
        rental.setRentalObject(rentalObject.get());
        rental.setPickUpLocation(pickUpLocation.get());
        rental.setDropOffLocation(dropOffLocation.get());
        try {
          this.rentalService.add(rental);
          this.logger.info("[POST] Valid rental, sending generated ID of added rental...");
          response = ResponseEntity.created(null).body(rental.getId());
        } catch (IllegalArgumentException e) {
          this.logger.error("[POST] Invalid rental, sending error message...");
          response = ResponseEntity.badRequest().body(e.getMessage());
        }
      } else if (!pickUpLocation.isPresent()) {
        this.logger.error("[POST] Pick up location does not exist, sending error message...");
        response = ResponseEntity.status(404).body("Pick up location does not exist");
      } else if (!dropOffLocation.isPresent()) {
        this.logger.error("[POST] Drop off location does not exist, sending error message...");
        response = ResponseEntity.status(404).body("Drop off location does not exist");
      } else {
        this.logger.error("[POST] Rental object does not exist, sending error message...");
        response = ResponseEntity.status(404).body("Rental object does not exist");
      }
    } else if (sessionUser == null) {
      this.logger.error("[POST] User not authenticated, sending error response...");
      response = ResponseEntity.status(401).build();
    } else {
      this.logger.error("[POST] User is deactivated, sending error response...");
      response = ResponseEntity.status(403).build();
    }
    return response;
  }

  /**
   * Endpoint for updating the rental with the specified ID with the specified update
   * rental.
   * 
   * @param id     The specified ID
   * @param rental The specified update rental
   * @return <p><b>200 OK</b> if rental exists and update rental is valid</p>
   *         <li><p><b>400 BAD REQUEST</b> if update rental is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if rental does not exist</p></li>
   */
  @Operation(
    summary = "Update rental",
    description = "Updates the rental with the specified ID with the specified update rental"
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
    @Parameter(description = "ID of rental to update")
    @PathVariable Long id,
    @Parameter(description = "Update rental")
    @RequestBody Rental rental
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      try {
        if (this.rentalService.update(id, rental)) {
          this.logger.info(
            "[PUT] Rental exists and valid update rental, sending success response..."
          );
          response = ResponseEntity.ok().build();
        } else {
          this.logger.error("[PUT] Rental does not exist, sending error response...");
          response = ResponseEntity.notFound().build();
        }
      } catch (IllegalArgumentException e) {
        this.logger.error("[PUT] Invalid update rental, sending error message...");
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
   * Endpoint for deleting the rental with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if rental exists</b></p>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if rental does not exist</p></li>
   */
  @Operation(
    summary = "Delete rental",
    description = "Deletes the rental with the specified ID"
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
    @Parameter(description = "ID of rental to delete")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      if (this.rentalService.delete(id)) {
        this.logger.info("[DELETE] Rental exists, sending success response...");
        response = ResponseEntity.ok().build();
      } else {
        this.logger.error("[DELETE] Rental does not exist, sending error response...");
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
