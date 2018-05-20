package ru.qf05.restaurants.repository;

import ru.qf05.restaurants.model.Voices;

import java.time.LocalDate;
import java.util.List;

public interface VoicesRepository {

    List<Voices> getAllBetween(LocalDate startDate, LocalDate endDate);

    List<Voices> getAll(LocalDate date);

    List<Voices> getAllToRestaurant(LocalDate date, int rId);

    List<Voices> getAllToRestaurantHistory(LocalDate startDate, LocalDate endDate, int rId);

    Voices save(int rId, Voices voices);

    Voices get(int userId, LocalDate date);
}
