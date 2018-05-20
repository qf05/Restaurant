package ru.qf05.restaurants.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import ru.qf05.restaurants.util.ValidationUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NamedQueries({
        @NamedQuery(name = Eat.ALL_SORTED, query = "SELECT DISTINCT e FROM Eat e LEFT JOIN FETCH e.restaurant WHERE e.restaurant.id=:rId AND e.date=:date ORDER BY e.name"),
        @NamedQuery(name = Eat.DELETE, query = "DELETE FROM Eat e WHERE e.id =:id")
})

@Entity
@Table(name = "eats", uniqueConstraints = {@UniqueConstraint(columnNames = {"date_time", "name", "restaurant_id"}, name = "eat_name_unique_restaurant_idx")})
public class Eat extends AbstractNamedEntity {

    public static final String ALL_SORTED = "Eat.getAllSorted";
    public static final String DELETE = "Eat.delete";

    @Column(name = "date_time", nullable = false)
    @NotNull(groups = ValidationUtil.Persist.class)
    private LocalDate date;

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 1, max = 500000)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull(groups = ValidationUtil.Persist.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Eat(Eat e) {
        this(e.getId(), e.getDate(), e.getName(), e.getPrice(), e.getRestaurant());
    }

    public Eat(Integer id, LocalDate date, String name, Integer price, Restaurant restaurant) {
        super(id, name);
        this.date = date;
        this.price = price;
        this.restaurant = restaurant;
    }

    public Eat(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }

    public Eat() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
