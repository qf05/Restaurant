package ru.qf05.restaurants.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.qf05.restaurants.model.Voices;
import ru.qf05.restaurants.service.AbstractServiceTest;
import ru.qf05.restaurants.service.RestaurantService;
import ru.qf05.restaurants.service.VoicesService;
import ru.qf05.restaurants.util.exception.NotFoundException;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static ru.qf05.restaurants.data.RestaurantData.*;
import static ru.qf05.restaurants.data.UserData.USER_ID1;
import static ru.qf05.restaurants.data.VoicesData.*;
import static ru.qf05.restaurants.data.VoicesData.assertMatch;

public class VoicesServiceTest extends AbstractServiceTest {

    private static final LocalTime NOW = LocalTime.now();

    @Autowired
    VoicesService service;

    @Autowired
    public RestaurantService restaurantService;

    @Test
    public void voising() {
        Voices voiced = service.voising(RES_ID2, USER_ID1, NOW);
        voiced.setRestaurant(RES2);
        List<Voices> voices = restaurantService.get(RES_ID2).getVoices();
        voices.sort(Comparator.comparing(Voices::getId));
        assertMatch(voices, VOICES2, VOICES5, voiced);
    }

    @Test
    public void secondVoising() {
        service.voising(RES_ID1, USER_ID1, LocalTime.of(9, 0));
        Voices voiced = service.voising(RES_ID2, USER_ID1, LocalTime.of(10, 0));
        voiced.setRestaurant(RES2);
        List<Voices> voices = restaurantService.get(RES_ID2).getVoices();
        voices.sort(Comparator.comparing(Voices::getId));
        assertMatch(voices, VOICES2, VOICES5, voiced);
    }

    @Test(expected = NotFoundException.class)
    public void secondVoisingError() {
        service.voising(RES_ID2, USER_ID1, NOW);
        service.voising(RES_ID1, USER_ID1, LocalTime.of(12, 0));
    }
}