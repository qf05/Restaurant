package ru.qf05.restaurants.service;

import ru.qf05.restaurants.model.Voices;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface VoicesService {

    Voices voising(int rId, int userId, LocalTime localTime);

    List<Voices> getAll(LocalDate date);

    List<Voices> getAllBetween(LocalDate startDate, LocalDate endDate);

    Voices get(int userId, LocalDate date);

    List<Voices> getAllToRestaurant(LocalDate date, int rId);

    List<Voices> getAllToRestaurantHistory(LocalDate startDate, LocalDate endDate, int rId);
}
