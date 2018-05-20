package ru.qf05.restaurants.web.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.qf05.restaurants.model.Restaurant;
import ru.qf05.restaurants.service.RestaurantService;
import ru.qf05.restaurants.to.RestaurantAndVoice;
import ru.qf05.restaurants.util.RestaurantUtil;
import ru.qf05.restaurants.web.AbstractLogerController;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.qf05.restaurants.util.ValidationUtil.assureIdConsistent;
import static ru.qf05.restaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(RestaurantRestController.REST_URL_RESTAURANT)
public class RestaurantRestController extends AbstractLogerController {
    static final String REST_URL_RESTAURANT = "/restaurant";

    @Autowired
    RestaurantService restaurantService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantAndVoice> getAll() {
        log.info("getAll restaurant");
        return RestaurantUtil.getAll(restaurantService.getAll());
    }

    @GetMapping(value = "/date",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantAndVoice> getAllToDate(@RequestParam(value = "date", required = false)
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAll restaurant for {} date", date);
        return RestaurantUtil.getAllWidthOutMenu(restaurantService.getAll(), date);
    }

    @GetMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantAndVoice> getAllWidthMenu(@RequestParam(value = "date", required = false)
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAll restaurant for {} date", date);
        return RestaurantUtil.getAllWidthMenu(restaurantService.getAll(), date);
    }

    @GetMapping(value = "/date/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantAndVoice> getAllWidthMenuToDate(@RequestParam(value = "date", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllWidthMenu restaurant for {} date", date);
        return RestaurantUtil.getAll(restaurantService.getAll(), date);
    }

    @GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantAndVoice> getAllBetweenWidthOutMenu(@RequestParam(value = "startDate", required = false)
                                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                              @RequestParam(value = "endDate", required = false)
                                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("getAllBetweenWidthOutMenu restaurant from {} to {} date", startDate, endDate);
        return RestaurantUtil.getAllWidthOutMenu(restaurantService.getAll(), startDate, endDate);
    }

    @GetMapping(value = "/history/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantAndVoice> getAllBetweenOfVoices(@RequestParam(value = "startDate", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateVoice,
                                                          @RequestParam(value = "endDate", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateVoice,
                                                          @RequestParam(value = "dateMenu", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllBetweenOfVoices restaurant from {} to {} date and menu for {} date", startDateVoice, endDateVoice, date);
        return RestaurantUtil.getAll(restaurantService.getAll(), startDateVoice, endDateVoice, date);
    }

    @GetMapping(value = "/history/menu/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantAndVoice> getAllBetweenOfVoicesAndMenu(@RequestParam(value = "startDateVoice", required = false)
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateVoice,
                                                                 @RequestParam(value = "endDateVoice", required = false)
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateVoice,
                                                                 @RequestParam(value = "startDateMenu", required = false)
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateMenu,
                                                                 @RequestParam(value = "endDateMenu", required = false)
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateMenu) {
        log.info("getAllBetweenOfVoicesAndMenu restaurant voices from {} to {} date and menu from{} to {} date", startDateVoice, endDateVoice, startDateMenu, endDateMenu);
        return RestaurantUtil.getAll(restaurantService.getAll(), startDateVoice, endDateVoice, startDateMenu, endDateMenu);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> add(@Valid @RequestBody Restaurant restaurantId) {
        log.info("add restaurant {}", restaurantId);
        checkNew(restaurantId);
        Restaurant created = restaurantService.create(restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_RESTAURANT + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int restaurantId) {
        log.info("delete restaurant {}", restaurantId);
        restaurantService.delete(restaurantId);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody Restaurant restaurantId, @PathVariable("id") int id) {
        log.info("update restaurant {}", restaurantId);
        assureIdConsistent(restaurantId, id);
        restaurantService.create(restaurantId);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestaurantAndVoice get(@PathVariable("id") int restaurantId) {
        log.info("get restaurant {}", restaurantId);
        return RestaurantUtil.get(restaurantService.get(restaurantId));
    }
}