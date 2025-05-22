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
import no.ntnu.project.group14.webapp.entities.Configuration;
import no.ntnu.project.group14.webapp.entities.Provider;
import no.ntnu.project.group14.webapp.entities.RentalObject;
import no.ntnu.project.group14.webapp.entities.User;
import no.ntnu.project.group14.webapp.services.AccessUserService;
import no.ntnu.project.group14.webapp.services.ConfigurationService;
import no.ntnu.project.group14.webapp.services.RentalObjectService;
import no.ntnu.project.group14.webapp.services.ProviderService;

@RestController
@CrossOrigin
@RequestMapping("/api/rentalobjects")
public class RentalObjectController {

  @Autowired
  private RentalObjectService rentalObjectService;

  @Autowired
  private ProviderService providerService;

  @Autowired
  private ConfigurationService configurationService;

  @Autowired
  private AccessUserService accessUserService;

  private final Logger logger = LoggerFactory.getLogger(RentalObjectController.class);

  /**
   * Endpoint for getting all rental objects.
   * 
   * @return <p><b>200 OK</b> (<i>body:</i> all rental objects)</p>
   */
  @Operation(
    summary = "Get rental objects",
    description = "Gets all rental objects"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains all rental objects"
    )
  })
  @GetMapping
  public Iterable<RentalObject> getAll() {
    Iterable<RentalObject> rentalObjects = this.rentalObjectService.getAll();
    this.logger.info("[GET] Sending all rental objects...");
    return rentalObjects;
  }

  /**
   * Endpoint for getting the rental object with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if rental object exists (<i>body:</i> rental object)</p>
   *         <li><b>404 NOT FOUND</b> if rental object does not exist</li>
   */
  @Operation(
    summary = "Get rental object",
    description = "Gets the rental object with the specified ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains rental object"
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Signals error"
    )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Object> get(
    @Parameter(description = "ID of rental object to get")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    Optional<RentalObject> rentalObject = this.rentalObjectService.get(id);
    if (rentalObject.isPresent()) {
      this.logger.info("[GET] Rental object exists, sending rental object...");
      response = ResponseEntity.ok().body(rentalObject.get());
    } else {
      this.logger.error("[GET] Rental object does not exist, sending error response...");
      response = ResponseEntity.notFound().build();
    }
    return response;
  }

  /**
   * Endpoint for adding the specified rental object to the provider with the specified provider ID
   * and the configuration with the specified configuration ID.
   * 
   * @param providerId      The specified provider ID
   * @param configurationId The specified configuration ID
   * @param rentalObject    The specified rental object
   * @return <p><b>201 CREATED</b> if rental object is valid (<i>body:</i> generated ID of added
   *         rental object)</p>
   *         <li><p><b>400 BAD REQUEST</b> if rental object is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if provider or configuation do not exist (<i>body:</i>
   *         error message)</p></li>
   */
  @Operation(
    summary = "Add rental object",
    description = "Adds the specified rental object"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Signals success and contains generated ID of added rental object"
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
  @PostMapping("/provider/{providerId}/configuration/{configurationId}")
  public ResponseEntity<Object> add(
    @Parameter(description = "ID of provider to add rental object to")
    @PathVariable Long providerId,
    @Parameter(description = "ID of configuration to add rental object to")
    @PathVariable Long configurationId,
    @Parameter(description = "Rental object to add")
    @RequestBody RentalObject rentalObject
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isActive() && sessionUser.isAdmin()) {
      Optional<Provider> provider = this.providerService.get(providerId);
      Optional<Configuration> configuration = this.configurationService.get(configurationId);
      if (provider.isPresent() && configuration.isPresent()) {
        rentalObject.setProvider(provider.get());
        rentalObject.setConfiguration(configuration.get());
        try {
          this.rentalObjectService.add(rentalObject);
          this.logger.info(
            "[POST] Valid rental object, sending generated ID of added rental object..."
          );
          response = ResponseEntity.created(null).body(rentalObject.getId());
        } catch (IllegalArgumentException e) {
          this.logger.error("[POST] Invalid rental object, sending error message...");
          response = ResponseEntity.badRequest().body(e.getMessage());
        }
      } else if (!provider.isPresent()) {
        this.logger.error("[POST] Provider does not exist, sending error message...");
        response = ResponseEntity.status(404).body("Provider does not exist");
      } else {
        this.logger.error("[POST] Configuration does not exist, sending error message...");
        response = ResponseEntity.status(404).body("Configuration does not exist");
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
   * Endpoint for updating the rental object with the specified ID with the specified update
   * rental object.
   * 
   * @param id           The specified ID
   * @param rentalObject The specified update rental object
   * @return <p><b>200 OK</b> if rental object exists and update rental object is valid</p>
   *         <li><p><b>400 BAD REQUEST</b> if update rental object is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FONUD</b> if rental object does not exist</p></li>
   */
  @Operation(
    summary = "Update rental object",
    description = "Updates the rental object with the specified ID with the specified update "
                + "rental object"
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
    @Parameter(description = "ID of rental object to update")
    @PathVariable Long id,
    @Parameter(description = "Update rental object")
    @RequestBody RentalObject rentalObject
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      try {
        if (this.rentalObjectService.update(id, rentalObject)) {
          this.logger.info(
            "[PUT] Rental object exists and valid update rental object, sending success "
          + "response..."
          );
          response = ResponseEntity.ok().build();
        } else {
          this.logger.error("[PUT] Rental object does not exist, sending error response...");
          response = ResponseEntity.notFound().build();
        }
      } catch (IllegalArgumentException e) {
        this.logger.error("[PUT] Invalid update rental object, sending error message...");
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
   * Endpoint for deleting the rental object with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if rental object exists</b></p>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if rental object does not exist</p></li>
   */
  @Operation(
    summary = "Delete rental object",
    description = "Deletes the rental object with the specified ID"
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
    @Parameter(description = "ID of rental object to delete")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      if (this.rentalObjectService.delete(id)) {
        this.logger.info("[DELETE] Rental object exists, sending success response...");
        response = ResponseEntity.ok().build();
      } else {
        this.logger.error("[DELETE] Rental object does not exist, sending error response...");
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
