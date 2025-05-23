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
import no.ntnu.project.group14.webapp.entities.Region;
import no.ntnu.project.group14.webapp.entities.User;
import no.ntnu.project.group14.webapp.services.AccessUserService;
import no.ntnu.project.group14.webapp.services.LocationService;
import no.ntnu.project.group14.webapp.services.RegionService;

@RestController
@CrossOrigin
@RequestMapping("/api/locations")
public class LocationController {

  @Autowired
  private LocationService locationService;

  @Autowired
  private RegionService regionService;

  @Autowired
  private AccessUserService accessUserService;

  private final Logger logger = LoggerFactory.getLogger(LocationController.class);

  /**
   * Endpoint for getting all locations.
   * 
   * @return <p><b>200 OK</b> (<i>body:</i> all locations)</p>
   */
  @Operation(
    summary = "Get locations",
    description = "Gets all locations"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains all locations"
    )
  })
  @GetMapping
  public Iterable<Location> getAll() {
    Iterable<Location> locations = this.locationService.getAll();
    this.logger.info("[GET] Sending all locations...");
    return locations;
  }

  /**
   * Endpoint for getting the location with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if location exists (<i>body:</i> location)</p>
   *         <li><b>404 NOT FOUND</b> if location does not exist</li>
   */
  @Operation(
    summary = "Get location",
    description = "Gets the location with the specified ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains location"
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Signals error"
    )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Object> get(
    @Parameter(description = "ID of location to get")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    Optional<Location> location = this.locationService.get(id);
    if (location.isPresent()) {
      this.logger.info("[GET] Location exists, sending location...");
      response = ResponseEntity.ok().body(location.get());
    } else {
      this.logger.error("[GET] Location does not exist, sending error response...");
      response = ResponseEntity.notFound().build();
    }
    return response;
  }

  /**
   * Endpoint for adding the specified location to the region with the specified region ID.
   * 
   * @param regionId The specified region ID
   * @param location The specified location
   * @return <p><b>201 CREATED</b> if location is valid (<i>body:</i> generated ID of added
   *         location)</p>
   *         <li><p><b>400 BAD REQUEST</b> if location is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if region does not exist</p></li>
   */
  @Operation(
    summary = "Add location",
    description = "Adds the specified location to the region with the specified region ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Signals success and contains generated ID of added location"
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
  @PostMapping("/region/{regionId}")
  public ResponseEntity<Object> add(
    @Parameter(description = "ID of region to add location to")
    @PathVariable Long regionId,
    @Parameter(description = "Location to add")
    @RequestBody Location location
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isActive() && sessionUser.isAdmin()) {
      Optional<Region> region = this.regionService.get(regionId);
      if (region.isPresent()) {
        location.setRegion(region.get());
        try {
          this.locationService.add(location);
          this.logger.info("[POST] Valid location, sending generated ID of added location...");
          response = ResponseEntity.created(null).body(location.getId());
        } catch (IllegalArgumentException e) {
          this.logger.error("[POST] Invalid location, sending error message...");
          response = ResponseEntity.badRequest().body(e.getMessage());
        }
      } else {
        this.logger.error("[POST] Region does not exist, sending error response...");
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
   * Endpoint for updating the location with the specified ID with the specified update
   * location.
   * 
   * @param id       The specified ID
   * @param location The specified update location
   * @return <p><b>200 OK</b> if location exists and update location is valid</p>
   *         <li><p><b>400 BAD REQUEST</b> if update location is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if location does not exist</p></li>
   */
  @Operation(
    summary = "Update location",
    description = "Updates the location with the specified ID with the specified update location"
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
    @Parameter(description = "ID of location to update")
    @PathVariable Long id,
    @Parameter(description = "Update location")
    @RequestBody Location location
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      try {
        if (this.locationService.update(id, location)) {
          this.logger.info(
            "[PUT] Location exists and valid update location, sending success response..."
          );
          response = ResponseEntity.ok().build();
        } else {
          this.logger.error("[PUT] Location does not exist, sending error response...");
          response = ResponseEntity.notFound().build();
        }
      } catch (IllegalArgumentException e) {
        this.logger.error("[PUT] Invalid update location, sending error message...");
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
   * Endpoint for deleting the location with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if location exists</b></p>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if location does not exist</p></li>
   */
  @Operation(
    summary = "Delete location",
    description = "Deletes the location with the specified ID"
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
    @Parameter(description = "ID of location to delete")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      if (this.locationService.delete(id)) {
        this.logger.info("[DELETE] Location exists, sending success response...");
        response = ResponseEntity.ok().build();
      } else {
        this.logger.error("[DELETE] Location does not exist, sending error response...");
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
