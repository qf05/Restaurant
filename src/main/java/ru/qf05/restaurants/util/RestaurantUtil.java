package ru.qf05.restaurants.util;

import ru.qf05.restaurants.model.Eat;
import ru.qf05.restaurants.model.Restaurant;
import ru.qf05.restaurants.to.RestaurantAndVoice;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public final class RestaurantUtil {

    private RestaurantUtil() {
    }

    public static RestaurantAndVoice get(Restaurant restaurant) {
        return new RestaurantAndVoice(restaurant);
    }

    public static List<RestaurantAndVoice> getAllWidthMenu(List<Restaurant> list,
                                                           LocalDate inputDate) {
        LocalDate date = DateTimeUtil.nullToNow(inputDate);

        return list.stream().map(i -> {
            Integer count = i.getVoices() == null ? 0 : i.getVoices().size();
            return new RestaurantAndVoice(i, count, menu(i, date));
        }).collect(Collectors.toList());
    }

    public static List<RestaurantAndVoice> getAll(List<Restaurant> list,
                                                  LocalDate inputDate) {
        LocalDate date = DateTimeUtil.nullToNow(inputDate);
        return list.stream().map(i -> new RestaurantAndVoice(i, voice(i, date), menu(i, date)))
                .collect(Collectors.toList());
    }

    public static List<RestaurantAndVoice> getAll(List<Restaurant> list,
                                                  LocalDate inputStartDate,
                                                  LocalDate inputEndDate,
                                                  LocalDate inputDate) {
        LocalDate date = DateTimeUtil.nullToNow(inputDate);
        LocalDate startDate = DateTimeUtil.nullToMin(inputStartDate);
        LocalDate endDate = DateTimeUtil.nullToMax(inputEndDate);

        return list.stream().map(i -> new RestaurantAndVoice(i, voice(i, startDate, endDate), menu(i, date))).collect(Collectors.toList());
    }

    public static List<RestaurantAndVoice> getAll(List<Restaurant> list,
                                                  LocalDate inputStartDate,
                                                  LocalDate inputEndDate,
                                                  LocalDate inputStartDateMenu,
                                                  LocalDate inputEndDateMenu) {
        LocalDate startDate = DateTimeUtil.nullToMin(inputStartDate);
        LocalDate endDate = DateTimeUtil.nullToMax(inputEndDate);
        LocalDate startDateMenu = DateTimeUtil.nullToMin(inputStartDateMenu);
        LocalDate endDateMenu = DateTimeUtil.nullToMax(inputEndDateMenu);

        return list.stream().map(i -> new RestaurantAndVoice(i, voice(i, startDate, endDate),
                menu(i, startDateMenu, endDateMenu))).collect(Collectors.toList());
    }

    private static List<Eat> menu(Restaurant restaurant, LocalDate date) {
        if (restaurant.getMenu() != null && restaurant.getMenu().size() > 0) {
            restaurant.getMenu().forEach(i -> i.setRestaurant(null));
            return restaurant.getMenu().stream()
                    .filter(i -> i.getDate().isEqual(date))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private static List<Eat> menu(Restaurant restaurant, LocalDate startDate, LocalDate endDate) {
        if (restaurant.getMenu() != null && restaurant.getMenu().size() > 0) {
            restaurant.getMenu().forEach(i -> i.setRestaurant(null));
            return restaurant.getMenu().stream()
                    .filter(i -> DateTimeUtil.isBetween(i.getDate(), startDate, endDate))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private static int voice(Restaurant restaurant, LocalDate date) {
        return restaurant.getVoices() == null || restaurant.getVoices().size() == 0 ? 0 :
                (int) restaurant.getVoices().stream().filter(i -> i.getDate().isEqual(date)).count();
    }

    private static int voice(Restaurant restaurant, LocalDate startDate, LocalDate endDate) {
        return restaurant.getVoices() == null || restaurant.getVoices().size() == 0 ? 0 :
                (int) restaurant.getVoices().stream().
                        filter(i -> DateTimeUtil.isBetween(i.getDate(), startDate, endDate)).count();
    }
}