package ru.qf05.restaurants.web.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.qf05.restaurants.model.Eat;
import ru.qf05.restaurants.service.EatService;
import ru.qf05.restaurants.web.AbstractLogerController;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;

import static ru.qf05.restaurants.util.ValidationUtil.assureIdConsistent;
import static ru.qf05.restaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(EatRestController.REST_URL_MENU)
public class EatRestController extends AbstractLogerController {
    static final String REST_URL_MENU = "/menu";

    @Autowired
    private EatService eatService;

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Eat> create(@Valid @RequestBody Eat eat, @PathVariable("id") int restaurantId) {
        log.info("create eat {} for {} restaurant", eat, restaurantId);
        checkNew(eat);
        Eat created = eatService.create(eat, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_MENU + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PutMapping(value = "/{rId}/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody Eat eat, @PathVariable("id") int id, @PathVariable("rId") int restaurantId) {
        log.info("update eat {} for {} restaurant", eat, restaurantId);
        assureIdConsistent(eat, id);
        eatService.update(eat, restaurantId);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int eatId) {
        log.info("delete eat {}", eatId);
        eatService.delete(eatId);
    }

    @GetMapping(value = "/is/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Eat get(@PathVariable("id") int eatId) {
        log.info("get eat {}", eatId);
        return eatService.get(eatId);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping(value = "/copy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void copyMenu(@PathVariable("id") int restaurantId,
                         @RequestParam(value = "date", required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("copyMenu for {} restaurant from {} date", restaurantId, date);
        eatService.copyMenu(restaurantId, date);
    }
}
