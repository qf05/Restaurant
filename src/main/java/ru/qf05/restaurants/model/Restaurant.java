package ru.qf05.restaurants.model;

import javax.persistence.*;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = Restaurant.GET, query = "SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.voices WHERE r.id=:id"),
        @NamedQuery(name = Restaurant.ALL_SORTED, query = "SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.voices ORDER BY r.name "),
        @NamedQuery(name = Restaurant.DELETE, query = "DELETE FROM Restaurant r WHERE r.id =:id")
})

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurant_unique_idx")})
public class Restaurant extends AbstractNamedEntity {

    public static final String DELETE = "Restaurant.delete";
    public static final String GET = "Restaurant.get";
    public static final String ALL_SORTED = "Restaurant.getAllSorted";

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    @OrderBy("name")
    private List<Eat> menu;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Voices> voices;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Restaurant(Restaurant r) {
        this(r.getId(), r.getName());
    }

    public Restaurant() {
    }

    public List<Eat> getMenu() {
        return menu;
    }

    public void setMenu(List<Eat> menu) {
        this.menu = menu;
    }

    public List<Voices> getVoices() {
        return voices;
    }

    public void setVoices(List<Voices> voices) {
        this.voices = voices;
    }
}
