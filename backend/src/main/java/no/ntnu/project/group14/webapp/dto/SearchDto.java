package no.ntnu.project.group14.webapp.dto;

import no.ntnu.project.group14.webapp.entities.Location;

/**
 * The SearchDto class represents a search data transfer object (DTO). The class contains data
 * the user will send when searching for a rental. These params will be used to se
 * cars.
 */
public class SearchDto {
  private final Location pickUpLocation;

  private final Location dropOffLocation;
  private final long startTime;
  private final long endTime;
  /**
   * Constructs an instance of the RegisterDto class.
   *
   * @param pickUpLocation The specified pick up location
   * @param dropOffLocation The specified drop off location
   * @param startTime The specified start time
   * @param endTime The specified end time
   */
  public SearchDto(Location pickUpLocation, Location dropOffLocation,
                   long startTime, long endTime) {
    this.pickUpLocation = pickUpLocation;
    this.dropOffLocation = dropOffLocation;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public Location getDropOffLocation() {
    return dropOffLocation;
  }

  public Location getPickUpLocation() {
    return pickUpLocation;
  }
}
