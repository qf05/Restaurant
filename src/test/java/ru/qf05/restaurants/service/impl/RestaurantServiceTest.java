package ru.qf05.restaurants.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.qf05.restaurants.model.Restaurant;
import ru.qf05.restaurants.service.AbstractServiceTest;
import ru.qf05.restaurants.service.RestaurantService;
import ru.qf05.restaurants.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;

import static ru.qf05.restaurants.data.RestaurantData.*;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    RestaurantService service;

    @Test
    public void get() {
        Restaurant restaurant = service.get(RES_ID1);
        assertMatch(restaurant, RES1);
    }

    @Test
    public void delete() {
        service.delete(RES_ID1);
        assertMatch(service.getAll(), RES2, RES4, RES3);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(), RES1, RES2, RES4, RES3);
    }

    @Test
    public void update() {
        Restaurant updated = new Restaurant(RES1);
        updated.setName("PALKIN'S");
        service.update(updated);
        assertMatch(service.get(RES_ID1), updated);
    }

    @Test
    public void create() {
        Restaurant newRest = new Restaurant(null, "Диззи");
        Restaurant created = service.create(newRest);
        newRest.setId(created.getId());
        assertMatch(service.getAll(), newRest, RES1, RES2, RES4, RES3);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundDelete() throws Exception {
        service.delete(123);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundUpdate() throws Exception {
        Restaurant updated = new Restaurant(RES1);
        updated.setName("PALKIN'S");
        updated.setId(123);
        service.update(updated);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundCreate() throws Exception {
        Restaurant newRestaurant = new Restaurant(123, "PALKIN'S");
        service.create(newRestaurant);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateNameCreate() throws Exception {
        Restaurant newRest = new Restaurant(null, "Палкин");
        service.create(newRest);
    }

    @Test
    public void testValidation() throws Exception {
        validateRootCause(() -> service.create(new Restaurant(null, "    ")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant(null, null)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant(null, "o")), ConstraintViolationException.class);
    }
}