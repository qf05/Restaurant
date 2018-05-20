package ru.qf05.restaurants.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.qf05.restaurants.model.Voices;
import ru.qf05.restaurants.repository.VoicesRepository;
import ru.qf05.restaurants.service.VoicesService;

import java.time.LocalDate;
import java.time.LocalTime;

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
        Voices voices = repository.get(userId, LocalDate.now());
        if (voices == null) {
            voices = new Voices(userId, LocalDate.now());
        }
        voices = !voices.isNew() && localTime.isAfter(LocalTime.of(11, 0)) ? null : repository.save(restaurantId, voices);
        return checkNotFoundWithId(voices, restaurantId);
    }
}
