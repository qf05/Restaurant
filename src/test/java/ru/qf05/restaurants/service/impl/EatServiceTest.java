package ru.qf05.restaurants.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.qf05.restaurants.data.EatData;
import ru.qf05.restaurants.model.Eat;
import ru.qf05.restaurants.service.AbstractServiceTest;
import ru.qf05.restaurants.service.EatService;
import ru.qf05.restaurants.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static ru.qf05.restaurants.data.EatData.*;
import static ru.qf05.restaurants.data.EatData.assertMatch;
import static ru.qf05.restaurants.data.RestaurantData.*;

public class EatServiceTest extends AbstractServiceTest {

    @Autowired
    private EatService service;

    @Test
    public void get() {
        Eat eat = service.get(EAT1_ID);
        assertMatch(eat, EAT1);
    }

    @Test
    public void delete() {
        service.delete(EAT1_ID);
        List<Eat> list = service.getAll(RES_ID1, DATE24);
        assertMatch(list, EAT2, EAT3);
    }

    @Test
    public void getAll() {
        List<Eat> list = service.getAll(RES_ID1, DATE24);
        assertMatch(list, EAT2, EAT1, EAT3);
    }

    @Test
    public void update() {
        Eat updated = new Eat(EAT1);
        updated.setName("Борщ");
        updated.setPrice(359);
        service.update(updated, RES_ID1);
        assertMatch(service.get(EAT1_ID), updated);
    }

    @Test
    public void create() {
        Eat newEat = new Eat(null, "Борщ", 350);
        Eat created = service.create(newEat, RES_ID1);
        newEat.setId(created.getId());
        newEat.setDate(created.getDate());
        assertMatch(service.getAll(RES_ID1, created.getDate()), newEat);
    }

    @Test
    public void getAllBetween() {
        List<Eat> list = service.getAllBetween(DATE24, DATE25, RES_ID1);
        assertMatch(list, EAT12, EAT2, EAT1, EAT3);
    }

    @Test
    public void copyMenu() {
        service.copyMenu(RES_ID1, DATE24);
        assertMatch(service.getAll(RES_ID1, LocalDate.now()), EatData.getNewEatToday());
    }

    @Test(expected = NotFoundException.class)
    public void notFoundDelete() throws Exception {
        service.delete(123);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundUpdate() throws Exception {
        Eat updated = new Eat(EAT1);
        updated.setName("Борщ");
        updated.setId(123);
        service.update(updated, RES_ID1);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundCreate() throws Exception {
        Eat newEat = new Eat(123, "Борщ", 350);
        service.create(newEat, RES_ID1);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateNameCreate() throws Exception {
        Eat updated = new Eat(EAT1);
        updated.setName("Сок");
        service.update(updated, RES_ID1);
    }

    @Test
    public void testValidation() throws Exception {
        validateRootCause(() -> service.create(new Eat(null, "  ", 300), RES_ID1), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Eat(null, null, 300), RES_ID1), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Eat(null, "Eat", -1), RES_ID1), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Eat(null, "Eat", 5000001), RES_ID1), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Eat(null, "o", 500), RES_ID1), ConstraintViolationException.class);
    }
}