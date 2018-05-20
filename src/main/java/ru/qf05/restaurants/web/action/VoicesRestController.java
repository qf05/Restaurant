package ru.qf05.restaurants.web.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.qf05.restaurants.model.Voices;
import ru.qf05.restaurants.service.VoicesService;
import ru.qf05.restaurants.web.AbstractLogerController;
import ru.qf05.restaurants.web.AuthorizedUser;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(VoicesRestController.REST_URL_VOICE)
public class VoicesRestController extends AbstractLogerController {
    static final String REST_URL_VOICE = "/voice";

    @Autowired
    VoicesService voicesService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Voices> getAll(@RequestParam(value = "date", required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAll voices for {} date", date);
        return voicesService.getAll(date);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Voices> getAllToRestaurant(@PathVariable("id") int restaurantId,
                                           @RequestParam(value = "date", required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAll voices to restaurant {} for {} date", restaurantId, date);
        return voicesService.getAllToRestaurant(date, restaurantId);
    }

    @GetMapping(value = "/history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Voices> getAllToRestaurantHistory(@PathVariable("id") int restaurantId,
                                                  @RequestParam(value = "date", required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam(value = "date", required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("getAllHistory voices for restaurant {} from {} to {} date", restaurantId, startDate, endDate);
        return voicesService.getAllToRestaurantHistory(startDate, endDate, restaurantId);
    }

    @GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Voices> getBetween(@RequestParam(value = "startDate", required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam(value = "endDate", required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("getBetween voices from {} to {} date", startDate, endDate);
        return voicesService.getAllBetween(startDate, endDate);
    }


    @PostMapping(value = "/{id}")
    public ResponseEntity<Voices> voice(@PathVariable("id") int restaurantId, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("voice to {} restaurant from {} user", restaurantId, authorizedUser.getId());
        Voices v = voicesService.voising(restaurantId, authorizedUser.getId(), LocalTime.now());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_VOICE + "/{id}")
                .buildAndExpand(v.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(v);
    }

    @GetMapping(value = "/is/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Voices get(@RequestParam(value = "date", required = false)
                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                      @PathVariable("id") int id) {
        log.info("get user is {} voice from {} date", id, date);
        return voicesService.get(id, date);
    }
}
