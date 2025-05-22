package no.ntnu.project.group14.webapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.Optional;

import no.ntnu.project.group14.webapp.entities.Configuration;
import no.ntnu.project.group14.webapp.entities.ExtraFeature;
import no.ntnu.project.group14.webapp.entities.User;
import no.ntnu.project.group14.webapp.services.AccessUserService;
import no.ntnu.project.group14.webapp.services.ConfigurationService;
import no.ntnu.project.group14.webapp.services.ExtraFeatureService;
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
 * The ExtraFeatureController class represents the REST controller for
 * {@link ExtraFeature extra features}.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/extrafeatures")
public class ExtraFeatureController {

  @Autowired
  private ExtraFeatureService extraFeatureService;

  @Autowired
  private ConfigurationService configurationService;

  @Autowired
  private AccessUserService accessUserService;

  private final Logger logger = LoggerFactory.getLogger(ExtraFeatureController.class);

  /**
   * Endpoint for getting all extra features.
   * 
   * @return <p><b>200 OK</b> (<i>body:</i> all extra features)</p>
   */
  @Operation(
    summary = "Get extra features",
    description = "Gets all extra features"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains all extra features"
    )
  })
  @GetMapping
  public Iterable<ExtraFeature> getAll() {
    Iterable<ExtraFeature> extraFeatures = this.extraFeatureService.getAll();
    this.logger.info("[GET] Sending all extra features...");
    return extraFeatures;
  }

  /**
   * Endpoint for getting the extra feature with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if extra feature exists (<i>body:</i> extra feature)</p>
   *         <li><b>404 NOT FOUND</b> if extra feature does not exist</li>
   */
  @Operation(
    summary = "Get extra feature",
    description = "Gets the extra feature with the specified ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains extra feature"
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Signals error"
    )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Object> get(
    @Parameter(description = "ID of extra feature to get")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    Optional<ExtraFeature> extraFeature = this.extraFeatureService.get(id);
    if (extraFeature.isPresent()) {
      this.logger.info("[GET] Extra feature exists, sending extra feature...");
      response = ResponseEntity.ok().body(extraFeature.get());
    } else {
      this.logger.error("[GET] Extra feature does not exist, sending error response...");
      response = ResponseEntity.notFound().build();
    }
    return response;
  }

  /**
   * Endpoint for adding the specified extra feature to the configuration with the specified
   * configuration ID.
   * 
   * @param configurationId The specified configuration ID
   * @param extraFeature    The specified extra feature
   * @return <p><b>201 CREATED</b> if extra feature is valid (<i>body:</i> generated ID of added
   *         extra feature)</p>
   *         <li><p><b>400 BAD REQUEST</b> if extra feature is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if configuration does not exist</p></li>
   */
  @Operation(
    summary = "Add extra feature",
    description = "Adds the specified extra feature"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Signals success and contains generated ID of added extra feature"
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
  @PostMapping("/configuration/{configurationId}")
  public ResponseEntity<Object> add(
    @Parameter(description = "ID of configuration to add extra feature to")
    @PathVariable Long configurationId,
    @Parameter(description = "Extra feature to add")
    @RequestBody ExtraFeature extraFeature
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isActive() && sessionUser.isAdmin()) {
      Optional<Configuration> configuration = this.configurationService.get(configurationId);
      if (configuration.isPresent()) {
        extraFeature.setConfiguration(configuration.get());
        try {
          this.extraFeatureService.add(extraFeature);
          this.logger.info(
            "[POST] Valid extra feature, sending generated ID of added extra feature..."
          );
          response = ResponseEntity.created(null).body(extraFeature.getId());
        } catch (IllegalArgumentException e) {
          this.logger.error("[POST] Invalid extra feature, sending error message...");
          response = ResponseEntity.badRequest().body(e.getMessage());
        }
      } else {
        this.logger.error("[POST] Configuration does not exist, sending error response...");
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
   * Endpoint for updating the extra feature with the specified ID with the specified update extra
   * feature.
   * 
   * @param id           The specified ID
   * @param extraFeature The specified update extra feature
   * @return <p><b>200 OK</b> if extra feature exists and update extra feature is valid</p>
   *         <li><p><b>400 BAD REQUEST</b> if update extra feature is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if extra feature does not exist</p></li>
   */
  @Operation(
    summary = "Update extra feature",
    description = "Updates the extra feature with the specified ID with the specified update "
                + "extra feature"
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
    @Parameter(description = "ID of extra feature to update")
    @PathVariable Long id,
    @Parameter(description = "Update extra feature")
    @RequestBody ExtraFeature extraFeature
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      try {
        if (this.extraFeatureService.update(id, extraFeature)) {
          this.logger.info(
            "[PUT] Extra feature exists and valid update extra feature, sending success "
          + "response..."
          );
          response = ResponseEntity.ok().build();
        } else {
          this.logger.error("[PUT] Extra feature does not exist, sending error response...");
          response = ResponseEntity.notFound().build();
        }
      } catch (IllegalArgumentException e) {
        this.logger.error("[PUT] Invalid update extra feature, sending error message...");
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
   * Endpoint for deleting the extra feature with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if extra feature exists</b></p>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if extra feature does not exist</p></li>
   */
  @Operation(
    summary = "Delete extra feature",
    description = "Deletes the extra feature with the specified ID"
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
    @Parameter(description = "ID of extra feature to delete")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      if (this.extraFeatureService.delete(id)) {
        this.logger.info("[DELETE] Extra feature exists, sending success response...");
        response = ResponseEntity.ok().build();
      } else {
        this.logger.error("[DELETE] Extra feature does not exist, sending error response...");
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
