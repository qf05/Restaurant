package ru.qf05.restaurants.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.qf05.restaurants.model.User;
import ru.qf05.restaurants.web.AuthorizedUser;

import javax.validation.Valid;

@RestController
@RequestMapping(ProfileRestController.PROFILE_REST_URL)
public class ProfileRestController extends AbstractUserController {
    static final String PROFILE_REST_URL = "/profile";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        return super.get(authorizedUser.getId());
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        super.delete(authorizedUser.getId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody User user, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        super.update(user, authorizedUser.getId());
    }
}