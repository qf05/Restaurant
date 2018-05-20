package ru.qf05.restaurants.service;

import ru.qf05.restaurants.model.Restaurant;
import ru.qf05.restaurants.util.exception.NotFoundException;

import java.util.List;

public interface RestaurantService {

    Restaurant get(int id) throws NotFoundException;

    void delete(int id) throws NotFoundException;

    List<Restaurant> getAll();

    Restaurant update(Restaurant restaurant) throws NotFoundException;

    Restaurant create(Restaurant restaurant);
}
