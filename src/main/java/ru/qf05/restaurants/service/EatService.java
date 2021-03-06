package ru.qf05.restaurants.service;

import ru.qf05.restaurants.model.Eat;
import ru.qf05.restaurants.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface EatService {

    Eat get(int id) throws NotFoundException;

    void delete(int id) throws NotFoundException;

    List<Eat> getAll(int restaurantId, LocalDate date);

    Eat update(Eat eat, int rId) throws NotFoundException;

    Eat create(Eat eat, int rId);

    List<Eat> getAllBetween(LocalDate startDate, LocalDate endDate, int id);

    void copyMenu(int id, LocalDate date);
}
