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
import no.ntnu.project.group14.webapp.entities.Provider;
import no.ntnu.project.group14.webapp.entities.Region;
import no.ntnu.project.group14.webapp.entities.User;
import no.ntnu.project.group14.webapp.services.AccessUserService;
import no.ntnu.project.group14.webapp.services.ProviderService;
import no.ntnu.project.group14.webapp.services.RegionService;

@RestController
@CrossOrigin
@RequestMapping("/api/providers")
public class ProviderController {

  @Autowired
  private ProviderService providerService;
  
  @Autowired
  private RegionService regionService;

  @Autowired
  private AccessUserService accessUserService;

  private final Logger logger = LoggerFactory.getLogger(ProviderController.class);

  /**
   * Endpoint for getting all providers.
   * 
   * @return <p><b>200 OK</b> (<i>body:</i> all providers)</p>
   */
  @Operation(
    summary = "Get providers",
    description = "Gets all providers"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains all providers"
    )
  })
  @GetMapping
  public Iterable<Provider> getAll() {
    Iterable<Provider> providers = this.providerService.getAll();
    this.logger.info("[GET] Sending all providers...");
    return providers;
  }

  /**
   * Endpoint for getting the provider with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if provider exists (<i>body:</i> provider)</p>
   *         <li><b>404 NOT FOUND</b> if provider does not exist</li>
   */
  @Operation(
    summary = "Get provider",
    description = "Gets the provider with the specified ID"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Signals success and contains provider"
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Signals error"
    )
  })
  @GetMapping("/{id}")
  public ResponseEntity<Object> get(
    @Parameter(description = "ID of provider to get")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    Optional<Provider> provider = this.providerService.get(id);
    if (provider.isPresent()) {
      this.logger.info("[GET] Provider exists, sending provider...");
      response = ResponseEntity.ok().body(provider.get());
    } else {
      this.logger.error("[GET] Provider does not exist, sending error response...");
      response = ResponseEntity.notFound().build();
    }
    return response;
  }

  /**
   * Endpoint for adding the specified provider.
   * 
   * @param provider The specified provider
   * @return <p><b>201 CREATED</b> if provider is valid (<i>body:</i> generated ID of added
   *         provider)</p>
   *         <li><p><b>400 BAD REQUEST</b> if provider is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   */
  @Operation(
    summary = "Add provider",
    description = "Adds the specified provider"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "201",
      description = "Signals success and contains generated ID of added provider"
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
    @Parameter(description = "Provider to add")
    @RequestBody Provider provider
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isActive() && sessionUser.isAdmin()) {
      try {
        this.providerService.add(provider);
        this.logger.info("[POST] Valid provider, sending generated ID of added provider...");
        response = ResponseEntity.created(null).body(provider.getId());
      } catch (IllegalArgumentException e) {
        this.logger.error("[POST] Invalid provider, sending error message...");
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
   * Endpoint for adding the region with the specified region ID to the provider with the specified
   * ID.
   * 
   * @param id       The specified ID
   * @param regionId The specified region ID
   * @return <p><b>200 OK</b> if provider is valid</p>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if provider or region do not exist (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>500 INTERNAL SERVER ERROR</b> if provider is invalid</p></li>
   */
  @Operation(
    summary = "Add region",
    description = "Add the region with the specified region ID to the provider with the specified "
                + "ID"
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
      description = "Signals error and contains error message"
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Signals error and contains error message"
    )
  })
  @PutMapping("/{id}/region/add/{regionId}")
  public ResponseEntity<Object> addRegion(
    @Parameter(description = "ID of provider to add region to")
    @PathVariable Long id,
    @Parameter(description = "Region to add")
    @PathVariable Long regionId) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isActive() && sessionUser.isAdmin()) {
      Optional<Provider> provider = this.providerService.get(id);
      Optional<Region> region = this.regionService.get(regionId);
      if (provider.isPresent() && region.isPresent()) {
        Provider storedProvider = provider.get();
        storedProvider.addRegion(region.get());
        try {
          // Provider already exists
          this.providerService.update(id, storedProvider);
          this.logger.info("[PUT] Valid provider, sending success response...");
          response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
          this.logger.error("[PUT] Invalid provider, sending error message...");
          response = ResponseEntity.internalServerError().body(e.getMessage());
        }
      } else if (!provider.isPresent()) {
        this.logger.error("[PUT] Provider does not exist, sending error message...");
        response = ResponseEntity.status(404).body("Provider does not exist");
      } else {
        this.logger.error("[PUT] Region does not exist, sending error message...");
        response = ResponseEntity.status(404).body("Region does not exist");
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
   * Endpoint for removing the region with the specified region ID from the provider with the
   * specified ID.
   * 
   * @param id       The specified ID
   * @param regionId The specified region ID
   * @return <p><b>200 OK</b> if provider is valid</p>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if provider or region do not exist (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>500 INTERNAL SERVER ERROR</b> if provider is invalid</p></li>
   */
  @Operation(
    summary = "Remove region",
    description = "Remove the region with the specified region ID from the provider with the "
                + "specified ID"
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
      description = "Signals error and contains error message"
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Signals error and contains error message"
    )
  })
  @PutMapping("/{id}/region/remove/{regionId}")
  public ResponseEntity<Object> removeRegion(
    @Parameter(description = "ID of provider to add region to")
    @PathVariable Long id,
    @Parameter(description = "Region to add")
    @PathVariable Long regionId) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isActive() && sessionUser.isAdmin()) {
      Optional<Provider> provider = this.providerService.get(id);
      Optional<Region> region = this.regionService.get(regionId);
      if (provider.isPresent() && region.isPresent()) {
        Provider storedProvider = provider.get();
        storedProvider.removeRegion(region.get());
        try {
          // Provider already exists
          this.providerService.update(id, storedProvider);
          this.logger.info("[PUT] Valid provider, sending success response...");
          response = ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
          this.logger.error("[PUT] Invalid provider, sending error message...");
          response = ResponseEntity.internalServerError().body(e.getMessage());
        }
      } else if (!provider.isPresent()) {
        this.logger.error("[PUT] Provider does not exist, sending error message...");
        response = ResponseEntity.status(404).body("Provider does not exist");
      } else {
        this.logger.error("[PUT] Region does not exist, sending error message...");
        response = ResponseEntity.status(404).body("Region does not exist");
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
   * Endpoint for updating the provider with the specified ID with the specified update provider.
   * 
   * @param id       The specified ID
   * @param provider The specified update provider
   * @return <p><b>200 OK</b> if provider exists and update provider is valid</p>
   *         <li><p><b>400 BAD REQUEST</b> if update provider is invalid (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if provider does not exist</p></li>
   */
  @Operation(
    summary = "Update provider",
    description = "Updates the provider with the specified ID with the specified update provider"
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
    @Parameter(description = "ID of provider to update")
    @PathVariable Long id,
    @Parameter(description = "Update provider")
    @RequestBody Provider provider
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      try {
        if (this.providerService.update(id, provider)) {
          this.logger.info(
            "[PUT] Provider exists and valid update provider, sending success response..."
          );
          response = ResponseEntity.ok().build();
        } else {
          this.logger.error("[PUT] Provider does not exist, sending error response...");
          response = ResponseEntity.notFound().build();
        }
      } catch (IllegalArgumentException e) {
        this.logger.error("[PUT] Invalid update provider, sending error message...");
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
   * Endpoint for deleting the provider with the specified ID.
   * 
   * @param id The specified ID
   * @return <p><b>200 OK</b> if provider exists</b></p>
   *         <li><p><b>401 UNAUTHORIZED</b> if user is not authenticated</p></li>
   *         <li><p><b>403 FORBIDDEN</b> if user is deactivated or not admin (<i>body:</i> error
   *         message)</p></li>
   *         <li><p><b>404 NOT FOUND</b> if provider does not exist</p></li>
   */
  @Operation(
    summary = "Delete provider",
    description = "Deletes the provider with the specified ID"
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
    @Parameter(description = "ID of provider to delete")
    @PathVariable Long id
  ) {
    ResponseEntity<Object> response;
    User sessionUser = this.accessUserService.getSessionUser();
    if (sessionUser != null && sessionUser.isAdmin()) {
      if (this.providerService.delete(id)) {
        this.logger.info("[DELETE] Provider exists, sending success response...");
        response = ResponseEntity.ok().build();
      } else {
        this.logger.error("[DELETE] Provider does not exist, sending error response...");
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
