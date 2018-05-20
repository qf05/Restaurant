package ru.qf05.restaurants.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.qf05.restaurants.model.Voices;
import ru.qf05.restaurants.repository.VoicesRepository;
import ru.qf05.restaurants.service.VoicesService;
import ru.qf05.restaurants.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.qf05.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoicesServiceImpl implements VoicesService {

    private final VoicesRepository repository;

    @Autowired
    public VoicesServiceImpl(VoicesRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Voices voising(int restaurantId, int userId, LocalTime localTime) {
        Voices voices = get(userId, LocalDate.now());
        if (voices == null) {
            voices = new Voices(userId, LocalDate.now());
        }
        voices = !voices.isNew() && localTime.isAfter(LocalTime.of(11, 0)) ? null : repository.save(restaurantId, voices);
        return checkNotFoundWithId(voices, restaurantId);
    }

    @Override
    public List<Voices> getAll(LocalDate date) {
        date = DateTimeUtil.nullToNow(date);
        return repository.getAll(date);
    }

    @Override
    public List<Voices> getAllBetween(LocalDate startDate, LocalDate endDate) {
        startDate = DateTimeUtil.nullToMin(startDate);
        endDate = DateTimeUtil.nullToMax(endDate);
        return repository.getAllBetween(startDate, endDate);
    }

    @Override
    public Voices get(int userId, LocalDate date) {
        date = DateTimeUtil.nullToNow(date);
        return repository.get(userId, date);
    }

    @Override
    public List<Voices> getAllToRestaurant(LocalDate date, int restaurantId) {
        date = DateTimeUtil.nullToNow(date);
        return repository.getAllToRestaurant(date, restaurantId);
    }

    @Override
    public List<Voices> getAllToRestaurantHistory(LocalDate startDate, LocalDate endDate, int restaurantId) {
        startDate = DateTimeUtil.nullToMin(startDate);
        endDate = DateTimeUtil.nullToMax(endDate);
        return repository.getAllToRestaurantHistory(startDate, endDate, restaurantId);
    }
}
