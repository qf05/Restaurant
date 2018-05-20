package ru.qf05.restaurants.service;

import ru.qf05.restaurants.model.Voices;

import java.time.LocalTime;

public interface VoicesService {

    Voices voising(int rId, int userId, LocalTime localTime);
}
