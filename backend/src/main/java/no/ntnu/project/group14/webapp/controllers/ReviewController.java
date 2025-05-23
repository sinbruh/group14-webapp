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
import no.ntnu.project.group14.webapp.entities.Review;
import no.ntnu.project.group14.webapp.entities.User;
import no.ntnu.project.group14.webapp.services.AccessUserService;
import no.ntnu.project.group14.webapp.services.ConfigurationService;
import no.ntnu.project.group14.webapp.services.ReviewService;

@RestController
@CrossOrigin
@RequestMapping("/api/reviews")
public class ReviewController {

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private ConfigurationService configurationService;

  @Autowired
  private AccessUserService accessUserService;

  private final Logger logger = LoggerFactory.getLogger(ReviewController.class);

  /**
   * Endpoint for getting all reviews.
   * 
   * @return <p><b>200 OK</b> (<i>body:</i> all reviews)</p>
   */
  @Operation(
    summary = "Get reviews",
    description = "Gets all reviews"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains all reviews"
    )
  })
  @GetMapping
  public Iterable<Review> getAll() {
    Iterable<Review> reviews = this.reviewService.getAll();
    this.logger.info("[GET] Sending all reviews...");
    return reviews;
  }

  /**
   * Endpoint for getting the review with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if review exists (<i>body:</i> review)</p>
   *         <li><b>404 NOT FOUND</b> if review does not exist</li>
   */
  @Operation(
    summary = "Get review",
    description = "Gets the review with the specified ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains review"
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Signals error"
    )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Object> get(
    @Parameter(description = "ID of review to get")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    Optional<Review> review = this.reviewService.get(id);
    if (review.isPresent()) {
      this.logger.info("[GET] Review exists, sending review...");
      response = ResponseEntity.ok().body(review.get());
    } else {
      this.logger.error("[GET] Review does not exist, sending error response...");
      response = ResponseEntity.notFound().build();
    }
    return response;
  }

  /**
   * Endpoint for adding the specified review to the configuration with the specified configuration ID.
   * 
   * @param configurationId The specified configuration ID
   * @param review          The specified review
   * @return <p><b>201 CREATED</b> if review is valid (<i>body:</i> generated ID of added
   *         review)</p>
   *         <li><p><b>400 BAD REQUEST</b> if review is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if configuration does not exist</p></li>
   */
  @Operation(
    summary = "Add review",
    description = "Adds the specified review to the configuration with the specified "
                + "configuration ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Signals success and contains generated ID of added review"
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
    @Parameter(description = "ID of configuration to add review to")
    @PathVariable Long configurationId,
    @Parameter(description = "Review to add")
    @RequestBody Review review
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isActive() && sessionUser.isAdmin()) {
      Optional<Configuration> configuration = this.configurationService.get(configurationId);
      if (configuration.isPresent()) {
        review.setUser(sessionUser);
        review.setConfiguration(configuration.get());
        try {
          this.reviewService.add(review);
          this.logger.info("[POST] Valid review, sending generated ID of added review...");
          response = ResponseEntity.created(null).body(review.getId());
        } catch (IllegalArgumentException e) {
          this.logger.error("[POST] Invalid review, sending error message...");
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
   * Endpoint for updating the review with the specified ID with the specified update review.
   * 
   * @param id     The specified ID
   * @param review The specified update review
   * @return <p><b>200 OK</b> if review exists and update review is valid</p>
   *         <li><p><b>400 BAD REQUEST</b> if update review is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if review does not exist</p></li>
   */
  @Operation(
    summary = "Update review",
    description = "Updates the review with the specified ID with the specified update review"
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
    @Parameter(description = "ID of review to update")
    @PathVariable Long id,
    @Parameter(description = "Update review")
    @RequestBody Review review
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      try {
        if (this.reviewService.update(id, review)) {
          this.logger.info(
            "[PUT] Review exists and valid update review, sending success response..."
          );
          response = ResponseEntity.ok().build();
        } else {
          this.logger.error("[PUT] Review does not exist, sending error response...");
          response = ResponseEntity.notFound().build();
        }
      } catch (IllegalArgumentException e) {
        this.logger.error("[PUT] Invalid update review, sending error message...");
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
   * Endpoint for deleting the review with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if review exists</b></p>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if review does not exist</p></li>
   */
  @Operation(
    summary = "Delete review",
    description = "Deletes the review with the specified ID"
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
    @Parameter(description = "ID of review to delete")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      if (this.reviewService.delete(id)) {
        this.logger.info("[DELETE] Review exists, sending success response...");
        response = ResponseEntity.ok().build();
      } else {
        this.logger.error("[DELETE] Review does not exist, sending error response...");
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
