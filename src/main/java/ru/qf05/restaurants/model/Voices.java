package ru.qf05.restaurants.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NamedQueries({
        @NamedQuery(name = Voices.GET, query = "SELECT DISTINCT v FROM Voices v LEFT JOIN FETCH  v.restaurant WHERE v.userId=:userId AND v.date=:date"),
        @NamedQuery(name = Voices.ALL, query = "SELECT DISTINCT v FROM Voices v LEFT JOIN FETCH  v.restaurant WHERE v.date=:date"),
        @NamedQuery(name = Voices.ALLBETWEEN, query = "SELECT DISTINCT v FROM Voices v LEFT JOIN FETCH  v.restaurant WHERE v.date>=:startDate AND v.date<=:endDate"),
        @NamedQuery(name = Voices.ALLTORESTARAUNT, query = "SELECT DISTINCT v FROM Voices v LEFT JOIN FETCH  v.restaurant WHERE v.restaurant.id=:rId AND v.date=:date"),
        @NamedQuery(name = Voices.ALLTORESTARAUNTHISTORY, query = "SELECT DISTINCT v FROM Voices v LEFT JOIN FETCH  v.restaurant WHERE v.restaurant.id=:rId AND v.date>=:startDate AND v.date<=:endDate")

})

@Entity
@Table(name = "voices", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date"}, name = "voices_idx")})
public class Voices extends AbstractBaseEntity {

    public static final String GET = "Voices.get";
    public static final String ALL = "Voices.all";
    public static final String ALLBETWEEN = "Voices.allBetween";
    public static final String ALLTORESTARAUNT = "Voices.allToRes";
    public static final String ALLTORESTARAUNTHISTORY = "Voices.ToResHistory";

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Integer userId;

    @Column(name = "date", nullable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date;

    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    public Voices() {
    }

    public Voices(int id, int userId, Restaurant restaurant, LocalDate date) {
        super(id);
        this.userId = userId;
        this.restaurant = restaurant;
        this.date = date;
    }

    public Voices(int userId, LocalDate date) {
        this.userId = userId;
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getUser() {
        return userId;
    }

    public void setUser(int userId) {
        this.userId = userId;
    }
}
