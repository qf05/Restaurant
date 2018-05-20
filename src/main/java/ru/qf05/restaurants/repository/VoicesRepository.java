package ru.qf05.restaurants.repository;

import ru.qf05.restaurants.model.Voices;

import java.time.LocalDate;

public interface VoicesRepository {

    Voices save(int rId, Voices voices);

    Voices get(int userId, LocalDate date);
}
