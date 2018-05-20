package ru.qf05.restaurants.to;

import ru.qf05.restaurants.model.Eat;
import ru.qf05.restaurants.model.Restaurant;

import java.util.List;

public class RestaurantAndVoice {

    private String name;
    private List<Eat> menu;
    private int voices;
    private int id;

    public RestaurantAndVoice(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.voices = restaurant.getVoices() == null || restaurant.getVoices().size() == 0 ? 0 :
                restaurant.getVoices().size();
    }

    public RestaurantAndVoice(Restaurant restaurant, int voices, List<Eat> menu) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.menu = menu;
        this.voices = voices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Eat> getMenu() {
        return menu;
    }

    public void setMenu(List<Eat> menu) {
        this.menu = menu;
    }

    public int getVoices() {
        return voices;
    }

    public void setVoices(int voices) {
        this.voices = voices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
