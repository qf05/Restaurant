package ru.qf05.restaurants.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.qf05.restaurants.model.Eat;
import ru.qf05.restaurants.model.Restaurant;
import ru.qf05.restaurants.repository.EatRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class EatRepositoryImpl implements EatRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Eat save(Eat eat, int resId) {
        if (!eat.isNew() && get(eat.getId()) == null) {
            return null;
        }
        eat.setRestaurant(em.getReference(Restaurant.class, resId));
        if (eat.isNew()) {
            eat.setDate(LocalDate.now());
            em.persist(eat);
            return eat;
        } else {
            return em.merge(eat);
        }
    }

    @Override
    public Eat get(int id) {
        return em.find(Eat.class, id);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(Eat.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    @Transactional
    public void copyMenu(int id, LocalDate date) {
        List<Eat> eats = em.createNamedQuery(Eat.ALL_SORTED, Eat.class)
                .setParameter("rId", id)
                .setParameter("date", date)
                .getResultList();
        eats.forEach(i -> save(new Eat(null, i.getName(), i.getPrice()), id));
    }
}
