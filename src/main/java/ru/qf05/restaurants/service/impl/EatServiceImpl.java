package ru.qf05.restaurants.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.qf05.restaurants.model.Eat;
import ru.qf05.restaurants.repository.EatRepository;
import ru.qf05.restaurants.service.EatService;
import ru.qf05.restaurants.util.DateTimeUtil;
import ru.qf05.restaurants.util.exception.NotFoundException;

import java.time.LocalDate;

import static ru.qf05.restaurants.util.ValidationUtil.checkNotFound;
import static ru.qf05.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service
public class EatServiceImpl implements EatService {

    private final EatRepository repository;

    @Autowired
    public EatServiceImpl(EatRepository repository) {
        this.repository = repository;
    }

    @Override
    public Eat get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @CacheEvict(value = "eats", allEntries = true)
    @Override
    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }

    @CacheEvict(value = "eats", allEntries = true)
    @Override
    public Eat update(Eat eat, int restaurantId) throws NotFoundException {
        return checkNotFoundWithId(repository.save(eat, restaurantId), eat.getId());
    }

    @CacheEvict(value = "eats", allEntries = true)
    @Override
    public Eat create(Eat eat, int restaurantId) {
        Assert.notNull(eat, "eat must not be null");
        return checkNotFound(repository.save(eat, restaurantId), "check of create eat");
    }

    @Override
    public void copyMenu(int id, LocalDate date) {
        date = DateTimeUtil.nullToNow(date);
        if (date.isEqual(LocalDate.now())) {
            date = date.minusDays(1);
        }
        repository.copyMenu(id, date);
    }
}
