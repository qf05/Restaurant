package ru.qf05.restaurants.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import ru.qf05.restaurants.model.User;
import ru.qf05.restaurants.service.UserService;
import ru.qf05.restaurants.web.AbstractLogerController;

import java.util.List;

import static ru.qf05.restaurants.util.ValidationUtil.assureIdConsistent;
import static ru.qf05.restaurants.util.ValidationUtil.checkNew;

public abstract class AbstractUserController extends AbstractLogerController {

    @Autowired
    private UserService service;

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }
}