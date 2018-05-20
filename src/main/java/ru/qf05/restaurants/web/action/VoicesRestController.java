package ru.qf05.restaurants.web.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.qf05.restaurants.model.Voices;
import ru.qf05.restaurants.service.VoicesService;
import ru.qf05.restaurants.web.AbstractLogerController;
import ru.qf05.restaurants.web.AuthorizedUser;

import java.net.URI;
import java.time.LocalTime;

@RestController
@RequestMapping(VoicesRestController.REST_URL_VOICE)
public class VoicesRestController extends AbstractLogerController {
    static final String REST_URL_VOICE = "/voice";

    @Autowired
    VoicesService voicesService;

    @PostMapping(value = "/{id}")
    public ResponseEntity<Voices> voice(@PathVariable("id") int restaurantId, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("voice to {} restaurant from {} user", restaurantId, authorizedUser.getId());
        Voices v = voicesService.voising(restaurantId, authorizedUser.getId(), LocalTime.now());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_VOICE + "/{id}")
                .buildAndExpand(v.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(v);
    }
}
