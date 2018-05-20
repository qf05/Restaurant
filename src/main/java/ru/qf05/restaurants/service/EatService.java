package ru.qf05.restaurants.service;

import ru.qf05.restaurants.model.Eat;
import ru.qf05.restaurants.util.exception.NotFoundException;

import java.time.LocalDate;

public interface EatService {

    Eat get(int id) throws NotFoundException;

    void delete(int id) throws NotFoundException;

    Eat update(Eat eat, int rId) throws NotFoundException;

    Eat create(Eat eat, int rId);

    void copyMenu(int id, LocalDate date);
}
