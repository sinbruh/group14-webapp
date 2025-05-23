package no.ntnu.project.group4.webapp;

import no.ntnu.project.group4.webapp.dto.UserUpdatePasswordDto;
import no.ntnu.project.group4.webapp.models.Car;
import no.ntnu.project.group4.webapp.models.Configuration;
import no.ntnu.project.group4.webapp.models.ExtraFeature;
import no.ntnu.project.group4.webapp.models.Provider;
import no.ntnu.project.group4.webapp.models.Role;
import no.ntnu.project.group4.webapp.models.User;
import no.ntnu.project.group4.webapp.repositories.RoleRepository;
import no.ntnu.project.group4.webapp.services.AccessUserService;
import no.ntnu.project.group4.webapp.services.CarService;
import no.ntnu.project.group4.webapp.services.ConfigurationService;
import no.ntnu.project.group4.webapp.services.ExtraFeatureService;
import no.ntnu.project.group4.webapp.services.ProviderService;
import no.ntnu.project.group4.webapp.services.UserService;

import java.sql.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * A class which inserts necessary data into the database, when Spring Boot app has started.
 */
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private AccessUserService accessUserService;
  @Autowired
  private CarService carService;
  @Autowired
  private ConfigurationService configurationService;
  @Autowired
  private ExtraFeatureService extraFeatureService;
  @Autowired
  private ProviderService providerService;

  @Value("${ADMIN_USERNAME}")
  private String adminUsername;
  @Value("${ADMIN_PASSWORD}")
  private String adminPassword;
  @Value("${USER_USERNAME}")
  private String userUsername;
  @Value("${USER_PASSWORD}")
  private String userPassword;

  private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

  /**
   * This method is called when the application is ready (loaded).
   * 
   * <p>Imports the initial data into the database. The initial data includes user roles and
   * cars.</p>
   *
   * @param event Event which is not used
   */
  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    this.logger.info("Importing data...");
    this.loadUserRoles();
    this.logger.info("Done importing data");
  }

  /**
   * Loads the user roles into the database if they are not already there.
   */
  private void loadUserRoles() {
    boolean isEmpty = true; // Guard condition
    Iterable<Role> existingRoles = this.roleRepository.findAll();
    Iterator<Role> rolesIt = existingRoles.iterator();
    if (rolesIt.hasNext()) {
      isEmpty = false;
    }
    this.logger.info("Loading user role data...");
    if (isEmpty) {
      Role user = new Role("ROLE_USER");
      Role admin = new Role("ROLE_ADMIN");

      this.roleRepository.save(user);
      this.roleRepository.save(admin);

      this.logger.info("Done loading user role data");
    } else {
      this.logger.info("User roles already in the database, not loading data");
    }
    this.loadUsers();
  }

  /**
   * Loads the admin user into the database if it is not already there.
   */
  private void loadUsers() {
    boolean isEmpty = true; // Guard condition
    Iterable<User> existingUsers = this.userService.getAll();
    Iterator<User> usersIt = existingUsers.iterator();
    if (usersIt.hasNext()) {
      isEmpty = false;
    }
    this.logger.info("Loading user data...");
    if (isEmpty) {
      // Create admin user with temporary password
      User admin = new User("Chuck", "Norris", adminUsername, 12345678, "temp",
                            new Date(5875200000l));

      // Create users with temporary password
      User user = new User("Dave", "Dangerous", userUsername, 87654321, "temp",
                           new Date(324601200000l));

      Role userRole = this.roleRepository.findOneByName("ROLE_USER");
      Role adminRole = this.roleRepository.findOneByName("ROLE_ADMIN");

      admin.addRole(userRole);
      admin.addRole(adminRole);
      user.addRole(userRole);

      this.userService.add(admin);
      this.userService.add(user);

      // Update admin user password to a proper password that uses BCrypt hashing
      this.accessUserService.updateUserPassword(admin, new UserUpdatePasswordDto(adminPassword));
      this.accessUserService.updateUserPassword(user, new UserUpdatePasswordDto(userPassword));

      this.logger.info("Done loading user data");
    } else {
      this.logger.info("Users already in the database, not loading data");
    }
    this.loadCars();
  }

  /**
   * Loads the cars into the database if they are not already there.
   */
  private void loadCars() {
    boolean isEmpty = true; // Guard condition
    Iterable<Car> existingCars = this.carService.getAll();
    Iterator<Car> carsIt = existingCars.iterator();
    if (carsIt.hasNext()) {
      isEmpty = false;
    }
    this.logger.info("Loading car data...");
    if (isEmpty) {
      // CAR 1

      // Initialization
      Car car1 = new Car("Volkswagen", "Golf", 2007);
      Configuration configuration1_1 = new Configuration("Diesel config", "Diesel", "Manual", 5);
      Configuration configuration1_2 = new Configuration("Petrol config", "Petrol", "Manual", 5);
      ExtraFeature extraFeature1_1_1 = new ExtraFeature("Bluetooth");
      ExtraFeature extraFeature1_1_2 = new ExtraFeature("DAB radio");
      ExtraFeature extraFeature1_1_3 = new ExtraFeature("Warming in the chairs");
      Provider provider1_1_1 = new Provider("Miller Bil", 600, "Ålesund", true, true);
      Provider provider1_1_2 = new Provider("Biller Bil", 550, "Stryn", true, true);

      configuration1_1.setCar(car1);
      configuration1_2.setCar(car1);
      extraFeature1_1_1.setConfiguration(configuration1_1);
      extraFeature1_1_2.setConfiguration(configuration1_1);
      extraFeature1_1_3.setConfiguration(configuration1_1);
      extraFeature1_1_1.setConfiguration(configuration1_2);
      extraFeature1_1_2.setConfiguration(configuration1_2);
      extraFeature1_1_3.setConfiguration(configuration1_2);
      provider1_1_1.setConfiguration(configuration1_1);
      provider1_1_2.setConfiguration(configuration1_1);
      provider1_1_1.setConfiguration(configuration1_2);
      provider1_1_2.setConfiguration(configuration1_2);

      // Loading into database
      carService.add(car1);
      configurationService.add(configuration1_1);
      configurationService.add(configuration1_2);
      extraFeatureService.add(extraFeature1_1_1);
      extraFeatureService.add(extraFeature1_1_2);
      extraFeatureService.add(extraFeature1_1_3);
      providerService.add(provider1_1_1);
      providerService.add(provider1_1_2);

      // CAR 2

      // Initialization
      Car car2 = new Car("Tesla", "Model 3", 2019);
      Configuration configuration2_1 = new Configuration("Electric config", "Electric",
                                                         "Automatic", 5);
      ExtraFeature extraFeature2_1_1 = new ExtraFeature("Autonomous driving");
      ExtraFeature extraFeature2_1_2 = new ExtraFeature("Long range");
      ExtraFeature extraFeature2_1_3 = new ExtraFeature("Warming in the seats");
      Provider provider2_1_1 = new Provider("Biggernes Tesla", 700, "Alta", true, true);
      Provider provider2_1_2 = new Provider("Tesla Tom (private)", 500, "Oslo", true, true);

      configuration2_1.setCar(car2);
      extraFeature2_1_1.setConfiguration(configuration2_1);
      extraFeature2_1_2.setConfiguration(configuration2_1);
      extraFeature2_1_3.setConfiguration(configuration2_1);
      provider2_1_1.setConfiguration(configuration2_1);
      provider2_1_2.setConfiguration(configuration2_1);

      // Loading into database
      carService.add(car2);
      configurationService.add(configuration2_1);
      extraFeatureService.add(extraFeature2_1_1);
      extraFeatureService.add(extraFeature2_1_2);
      extraFeatureService.add(extraFeature2_1_3);
      providerService.add(provider2_1_1);
      providerService.add(provider2_1_2);

      // CAR 3

      // Initialization
      Car car3 = new Car("Tesla", "Model Y", 2022);
      Configuration configuration3_1 = new Configuration("Electric config", "Electric",
                                                         "Automatic", 5);
      ExtraFeature extraFeature3_1_1 = new ExtraFeature("Four wheel drive");
      ExtraFeature extraFeature3_1_2 = new ExtraFeature("Glass roof");
      ExtraFeature extraFeature3_1_3 = new ExtraFeature("Autonomous driving");
      Provider provider3_1_1 = new Provider("Biggernes Tesla", 900, "Alta", true, true);
      Provider provider3_1_2 = new Provider("Tesla Tom (private)", 700, "Oslo", true, true);

      configuration3_1.setCar(car3);
      extraFeature3_1_1.setConfiguration(configuration3_1);
      extraFeature3_1_2.setConfiguration(configuration3_1);
      extraFeature3_1_3.setConfiguration(configuration3_1);
      provider3_1_1.setConfiguration(configuration3_1);
      provider3_1_2.setConfiguration(configuration3_1);

      // Loading into database
      carService.add(car3);
      configurationService.add(configuration3_1);
      extraFeatureService.add(extraFeature3_1_1);
      extraFeatureService.add(extraFeature3_1_2);
      extraFeatureService.add(extraFeature3_1_3);
      providerService.add(provider3_1_1);
      providerService.add(provider3_1_2);

      // CAR 4

      // Initialization
      Car car4 = new Car("Nissan", "Leaf", 2016);
      Configuration configuration4_1 = new Configuration("Electric config", "Electric",
                                                         "Automatic", 5);
      Configuration configuration4_2 = new Configuration("Hybrid config", "Hybrid", "Automatic", 5);
      Provider provider4_1_1 = new Provider("Auto 9-9", 500, "Stavanger", true, true);
      Provider provider4_1_2 = new Provider("Auto 10-10", 500, "Ålesund", true, true);

      configuration4_1.setCar(car4);
      configuration4_2.setCar(car4);
      provider4_1_1.setConfiguration(configuration4_1);
      provider4_1_2.setConfiguration(configuration4_1);
      provider4_1_1.setConfiguration(configuration4_2);
      provider4_1_2.setConfiguration(configuration4_2);

      // Loading into database
      carService.add(car4);
      configurationService.add(configuration4_1);
      configurationService.add(configuration4_2);
      providerService.add(provider4_1_1);
      providerService.add(provider4_1_2);

      // CAR 5

      // Initialization
      Car car5 = new Car("Mazda", "2", 2017);
      Configuration configuration5_1 = new Configuration("Petrol config", "Petrol", "Automatic",
                                                         5);
      ExtraFeature extraFeature5_1_1 = new ExtraFeature("DAB radio");
      Provider provider5_1_1 = new Provider("Bilikist", 400, "Stryn", true, true);

      configuration5_1.setCar(car5);
      extraFeature5_1_1.setConfiguration(configuration5_1);
      provider5_1_1.setConfiguration(configuration5_1);

      // Loading into database
      carService.add(car5);
      configurationService.add(configuration5_1);
      extraFeatureService.add(extraFeature5_1_1);
      providerService.add(provider5_1_1);

      // CAR 6

      // Initialization
      Car car6 = new Car("Volkswagen", "Transporter", 1978);
      Configuration configuration6_1 = new Configuration("Petrol config", "Petrol", "Manual", 8);
      ExtraFeature extraFeature6_1_1 = new ExtraFeature("Yellow");
      ExtraFeature extraFeature6_1_2 = new ExtraFeature("Retro");
      Provider provider6_1_1 = new Provider("Ørsta kommune", 200, "Alta", true, true);
      Provider provider6_1_2 = new Provider("Sirkelsliper", 70, "Oslo", true, true);
      Provider provider6_1_3 = new Provider("Peace Per", 180, "Stavanger", true, true);

      configuration6_1.setCar(car6);
      extraFeature6_1_1.setConfiguration(configuration6_1);
      extraFeature6_1_2.setConfiguration(configuration6_1);
      provider6_1_1.setConfiguration(configuration6_1);
      provider6_1_2.setConfiguration(configuration6_1);
      provider6_1_3.setConfiguration(configuration6_1);

      // Loading into database
      carService.add(car6);
      configurationService.add(configuration6_1);
      extraFeatureService.add(extraFeature6_1_1);
      extraFeatureService.add(extraFeature6_1_2);
      providerService.add(provider6_1_1);
      providerService.add(provider6_1_2);
      providerService.add(provider6_1_3);

      // CAR 7

      // Initialization
      Car car7 = new Car("BMW", "M3 Evo", 1988);
      Configuration configuration7_1 = new Configuration("Petrol config", "Petrol", "Manual", 4);
      ExtraFeature extraFeature7_1_1 = new ExtraFeature("Three stripes");
      ExtraFeature extraFeature7_1_2 = new ExtraFeature("Original tire discs");
      Provider provider7_1_1 = new Provider("Bilverksted", 400, "Ålesund", true, true);
      Provider provider7_1_2 = new Provider("Grabes", 450, "Stryn", true, true);
      Provider provider7_1_3 = new Provider("Djarney", 449, "Alta", true, true);

      configuration7_1.setCar(car7);
      extraFeature7_1_1.setConfiguration(configuration7_1);
      extraFeature7_1_2.setConfiguration(configuration7_1);
      provider7_1_1.setConfiguration(configuration7_1);
      provider7_1_2.setConfiguration(configuration7_1);
      provider7_1_3.setConfiguration(configuration7_1);

      // Loading into database
      carService.add(car7);
      configurationService.add(configuration7_1);
      extraFeatureService.add(extraFeature7_1_1);
      extraFeatureService.add(extraFeature7_1_2);
      providerService.add(provider7_1_1);
      providerService.add(provider7_1_2);
      providerService.add(provider7_1_3);

      // CAR 8

      // Initialization
      Car car8 = new Car("Skoda", "Fabia", 2011);
      Configuration configuration8_1 = new Configuration("Diesel config", "Diesel", "Automatic",
                                                         5);
      ExtraFeature extraFeature8_1_1 = new ExtraFeature("Tow hook");
      Provider provider8_1_1 = new Provider("Sprekksaver", 300, "Oslo", true, true);
      Provider provider8_1_2 = new Provider("Smidig bilforhandler", 229, "Stavanger", true, true);
      Provider provider8_1_3 = new Provider("Fossefall bilforhandler", 700, "Ålesund", true, true);

      configuration8_1.setCar(car8);
      extraFeature8_1_1.setConfiguration(configuration8_1);
      provider8_1_1.setConfiguration(configuration8_1);
      provider8_1_2.setConfiguration(configuration8_1);
      provider8_1_3.setConfiguration(configuration8_1);

      // Loading into database
      carService.add(car8);
      configurationService.add(configuration8_1);
      extraFeatureService.add(extraFeature8_1_1);
      providerService.add(provider8_1_1);
      providerService.add(provider8_1_2);
      providerService.add(provider8_1_3);

      // CAR 9

      // Initialization
      Car car9 = new Car("Peugeot", "307 SW", 2008);
      Configuration configuration9_1 = new Configuration("Diesel config", "Diesel", "Manual", 7);
      ExtraFeature extraFeature9_1_1 = new ExtraFeature("Travel box on the roof");
      Provider provider9_1_1 = new Provider("Bertel Ostein", 600, "Stryn", true, true);
      Provider provider9_1_2 = new Provider("Auto 10-10", 550, "Ålesund", true, true);

      configuration9_1.setCar(car9);
      extraFeature9_1_1.setConfiguration(configuration9_1);
      provider9_1_1.setConfiguration(configuration9_1);
      provider9_1_2.setConfiguration(configuration9_1);

      // Loading into database
      carService.add(car9);
      configurationService.add(configuration9_1);
      extraFeatureService.add(extraFeature9_1_1);
      providerService.add(provider9_1_1);
      providerService.add(provider9_1_2);

      // CAR 10

      // Initialization
      Car car10 = new Car("Peugeot", "207", 2007);
      Configuration configuration10_1 = new Configuration("Diesel config", "Diesel", "Manual", 5);
      ExtraFeature extraFeature10_1_1 = new ExtraFeature("Glass window");
      ExtraFeature extraFeature10_1_2 = new ExtraFeature("Warming in the seats");
      ExtraFeature extraFeature10_1_3 = new ExtraFeature("Warming in the steering wheel");
      ExtraFeature extraFeature10_1_4 = new ExtraFeature("Warming in the mirrors");
      ExtraFeature extraFeature10_1_5 = new ExtraFeature("Warming in the tires");
      ExtraFeature extraFeature10_1_6 = new ExtraFeature("Warming under the rubber rugs");
      ExtraFeature extraFeature10_1_7 = new ExtraFeature("Warming 360");
      Provider provider10_1_1 = new Provider("Bertel Ostein", 500, "Stryn", true, true);
      Provider provider10_1_2 = new Provider("Auto 10-10", 550, "Ålesund", true, true);

      configuration10_1.setCar(car10);
      extraFeature10_1_1.setConfiguration(configuration10_1);
      extraFeature10_1_2.setConfiguration(configuration10_1);
      extraFeature10_1_3.setConfiguration(configuration10_1);
      extraFeature10_1_4.setConfiguration(configuration10_1);
      extraFeature10_1_5.setConfiguration(configuration10_1);
      extraFeature10_1_6.setConfiguration(configuration10_1);
      extraFeature10_1_7.setConfiguration(configuration10_1);
      provider10_1_1.setConfiguration(configuration10_1);
      provider10_1_2.setConfiguration(configuration10_1);

      // Loading into database
      carService.add(car10);
      configurationService.add(configuration10_1);
      extraFeatureService.add(extraFeature10_1_1);
      extraFeatureService.add(extraFeature10_1_2);
      extraFeatureService.add(extraFeature10_1_3);
      extraFeatureService.add(extraFeature10_1_3);
      extraFeatureService.add(extraFeature10_1_3);
      extraFeatureService.add(extraFeature10_1_3);
      extraFeatureService.add(extraFeature10_1_3);
      providerService.add(provider10_1_1);
      providerService.add(provider10_1_2);

      // CAR 11

      // Initialization
      Car car11 = new Car("Peugeot", "3008", 2010);
      Configuration configuration11_1 = new Configuration("Diesel config", "Diesel", "Manual", 5);
      ExtraFeature extraFeature11_1_1 = new ExtraFeature("FM radio");
      ExtraFeature extraFeature11_1_2 = new ExtraFeature("CD player");
      ExtraFeature extraFeature11_1_3 = new ExtraFeature("Metallic paint");
      Provider provider11_1_1 = new Provider("Bertel Ostein", 600, "Stryn", true, true);
      Provider provider11_1_2 = new Provider("Auto 10-10", 600, "Ålesund", true, true);

      configuration11_1.setCar(car11);
      extraFeature11_1_1.setConfiguration(configuration11_1);
      extraFeature11_1_2.setConfiguration(configuration11_1);
      extraFeature11_1_3.setConfiguration(configuration11_1);
      provider11_1_1.setConfiguration(configuration11_1);
      provider11_1_2.setConfiguration(configuration11_1);

      // Loading into database
      carService.add(car11);
      configurationService.add(configuration11_1);
      extraFeatureService.add(extraFeature11_1_1);
      extraFeatureService.add(extraFeature11_1_2);
      extraFeatureService.add(extraFeature11_1_3);
      providerService.add(provider11_1_1);
      providerService.add(provider11_1_2);

      // CAR 12

      // Initialization
      Car car12 = new Car("Peugeot", "iOn", 2015);
      Configuration configuration12_1 = new Configuration("Electric config", "Electric",
                                                          "Automatic", 4);
      ExtraFeature extraFeature12_1_1 = new ExtraFeature("Five doors");
      ExtraFeature extraFeature12_1_2 = new ExtraFeature("Very economic");
      Provider provider12_1_1 = new Provider("Bertel Ostein", 200, "Stryn", true, true);
      Provider provider12_1_2 = new Provider("Auto 10-10", 201, "Ålesund", true, true);

      configuration12_1.setCar(car12);
      extraFeature12_1_1.setConfiguration(configuration12_1);
      extraFeature12_1_2.setConfiguration(configuration12_1);
      provider12_1_1.setConfiguration(configuration12_1);
      provider12_1_2.setConfiguration(configuration12_1);

      // Loading into database
      carService.add(car12);
      configurationService.add(configuration12_1);
      extraFeatureService.add(extraFeature12_1_1);
      extraFeatureService.add(extraFeature12_1_2);
      providerService.add(provider12_1_1);
      providerService.add(provider12_1_2);

      this.logger.info("Done loading car data");
    } else {
      this.logger.info("Cars already in the database, not loading data");
    }
  }
}
