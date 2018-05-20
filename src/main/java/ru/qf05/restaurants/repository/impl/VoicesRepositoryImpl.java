package ru.qf05.restaurants.repository.impl;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.qf05.restaurants.model.Restaurant;
import ru.qf05.restaurants.model.Voices;
import ru.qf05.restaurants.repository.VoicesRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class VoicesRepositoryImpl implements VoicesRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Voices> getAll(LocalDate date) {
        List<Voices> voices = em.createNamedQuery(Voices.ALL, Voices.class).setParameter("date", date).getResultList();
        voices.forEach(i->i.getRestaurant().setMenu(null));
        return voices;
    }

    @Override
    public List<Voices> getAllBetween(LocalDate startDate, LocalDate endDate) {
        List<Voices> voices =  em.createNamedQuery(Voices.ALLBETWEEN, Voices.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        voices.forEach(i->i.getRestaurant().setMenu(null));
        return voices;
    }

    @Override
    public List<Voices> getAllToRestaurant(LocalDate date, int rId) {
        List<Voices> voices =  em.createNamedQuery(Voices.ALLTORESTARAUNT, Voices.class)
                .setParameter("date", date)
                .setParameter("rId", rId)
                .getResultList();
        voices.forEach(i->i.getRestaurant().setMenu(null));
        return voices;
    }

    @Override
    public List<Voices> getAllToRestaurantHistory(LocalDate startDate, LocalDate endDate, int rId) {
        List<Voices> voices =  em.createNamedQuery(Voices.ALLTORESTARAUNTHISTORY, Voices.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("rId", rId)
                .getResultList();
        voices.forEach(i->i.getRestaurant().setMenu(null));
        return voices;
    }

    @Transactional
    @Override
    public Voices save(int rId, Voices voices) {
        Restaurant restaurant = em.getReference(Restaurant.class, rId);
        voices.setRestaurant(restaurant);
        if (voices.isNew()) {
            em.persist(voices);
            return voices;
        } else {
            return em.merge(voices);
        }
    }

    @Override
    public Voices get(int userId, LocalDate date) {
        List<Voices> voicesList = em.createNamedQuery(Voices.GET, Voices.class)
                .setParameter("userId", userId)
                .setParameter("date", date)
                .getResultList();
        Voices voice = DataAccessUtils.singleResult(voicesList);
        if (voice!=null) {
            voice.getRestaurant().setMenu(null);
        }
        return voice;
    }
}
