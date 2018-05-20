package ru.qf05.restaurants.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.qf05.restaurants.model.Restaurant;
import ru.qf05.restaurants.repository.RestaurantRepository;
import ru.qf05.restaurants.service.RestaurantService;
import ru.qf05.restaurants.util.exception.NotFoundException;

import java.util.List;

import static ru.qf05.restaurants.util.ValidationUtil.checkNotFound;
import static ru.qf05.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    @Override
    public Restaurant update(Restaurant restaurant) throws NotFoundException {
        return checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
    }

    @Override
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "meal must not be null");
        return checkNotFound(repository.save(restaurant), "check of create restaurant");
    }
}
