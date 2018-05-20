package ru.qf05.restaurants.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.qf05.restaurants.model.Voices;
import ru.qf05.restaurants.service.AbstractServiceTest;
import ru.qf05.restaurants.service.VoicesService;
import ru.qf05.restaurants.util.exception.NotFoundException;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static ru.qf05.restaurants.data.RestaurantData.*;
import static ru.qf05.restaurants.data.UserData.USER_ID1;
import static ru.qf05.restaurants.data.VoicesData.*;
import static ru.qf05.restaurants.data.VoicesData.assertMatch;
import static ru.qf05.restaurants.util.DateTimeUtil.MAX_DATE;
import static ru.qf05.restaurants.util.DateTimeUtil.MIN_DATE;

public class VoicesServiceTest extends AbstractServiceTest {

    private static final LocalTime NOW = LocalTime.now();

    @Autowired
    VoicesService service;

    @Test
    public void voising() {
        Voices voiced = service.voising(RES_ID2, USER_ID1, NOW);
        voiced.setRestaurant(RES2);
        List<Voices> voices = service.getAllBetween(MIN_DATE, MAX_DATE);
        voices.sort(Comparator.comparing(Voices::getId));
        assertMatch(voices, VOICES1, VOICES2, VOICES3, VOICES4, VOICES5, voiced);
    }

    @Test
    public void secondVoising() {
        service.voising(RES_ID2, USER_ID1, NOW);
        Voices voiced = service.voising(RES_ID1, USER_ID1, LocalTime.of(10, 0));
        voiced.setRestaurant(RES1);
        List<Voices> voices = service.getAllBetween(MIN_DATE, MAX_DATE);
        voices.sort(Comparator.comparing(Voices::getId));
        assertMatch(voices, VOICES1, VOICES2, VOICES3, VOICES4, VOICES5, voiced);
    }

    @Test(expected = NotFoundException.class)
    public void secondVoisingError() {
        service.voising(RES_ID2, USER_ID1, NOW);
        service.voising(RES_ID1, USER_ID1, LocalTime.of(12, 0));
    }

    @Test
    public void getAll() {
        List<Voices> voices = service.getAll(DATE24);
        voices.sort(Comparator.comparing(Voices::getId));
        assertMatch(voices, VOICES1, VOICES2, VOICES3);
    }

    @Test
    public void getAllBetween() {
        List<Voices> voices = service.getAllBetween(DATE24, MAX_DATE);
        voices.sort(Comparator.comparing(Voices::getId));
        assertMatch(voices, VOICES1, VOICES2, VOICES3, VOICES4, VOICES5);
    }

    @Test
    public void get() {
        Voices voices = service.get(USER_ID1, DATE24);
        assertMatch(voices, VOICES1);
    }

    @Test
    public void getAllToRestaurant() {
        List<Voices> voices = service.getAllToRestaurant(DATE24, RES_ID1);
        voices.sort(Comparator.comparing(Voices::getId));
        assertMatch(voices, VOICES1, VOICES3);
    }

    @Test
    public void getAllToRestaurantHistory() {
        List<Voices> voices = service.getAllToRestaurantHistory(DATE24, MAX_DATE, RES_ID1);
        voices.sort(Comparator.comparing(Voices::getId));
        assertMatch(voices, VOICES1, VOICES3, VOICES4);
    }
}