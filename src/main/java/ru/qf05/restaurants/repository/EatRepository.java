package ru.qf05.restaurants.repository;

import ru.qf05.restaurants.model.Eat;

import java.time.LocalDate;
import java.util.List;

public interface EatRepository {
    Eat save(Eat eat, int rId);

    Eat get(int id);

    List<Eat> getAll(int rId, LocalDate date);

    boolean delete(int id);

    List<Eat> getAllBetween(LocalDate startDate, LocalDate endDate, int id);

    void copyMenu(int id, LocalDate date);
}
