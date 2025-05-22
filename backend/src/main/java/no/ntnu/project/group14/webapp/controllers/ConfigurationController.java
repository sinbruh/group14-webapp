package no.ntnu.project.group14.webapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.project.group14.webapp.dto.ConfigurationDto;
import no.ntnu.project.group14.webapp.entities.Configuration;
import no.ntnu.project.group14.webapp.entities.RentalObject;
import no.ntnu.project.group14.webapp.services.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  private final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

  /**
   * Endpoint for getting all configuraitons. The configuraitons are sent in the form of
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
  @GetMapping
  public Iterable<ConfigurationDto> getAll() {
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
}
