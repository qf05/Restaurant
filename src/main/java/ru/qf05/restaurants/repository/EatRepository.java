package ru.qf05.restaurants.repository;

import ru.qf05.restaurants.model.Eat;

import java.time.LocalDate;

public interface EatRepository {
    Eat save(Eat eat, int rId);

    Eat get(int id);

    boolean delete(int id);

    void copyMenu(int id, LocalDate date);
}
