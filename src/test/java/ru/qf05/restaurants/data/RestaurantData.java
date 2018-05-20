package ru.qf05.restaurants.data;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.qf05.restaurants.model.Restaurant;
import ru.qf05.restaurants.to.RestaurantAndVoice;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static ru.qf05.restaurants.web.json.JsonUtil.writeValue;

public class RestaurantData {
    public static final int RES_ID1 = 100003;
    public static final int RES_ID2 = 100004;
    public static final int RES_ID3 = 100005;
    public static final int RES_ID4 = 100006;

    public static final Restaurant RES1 = new Restaurant(RES_ID1, "Палкин");
    public static final Restaurant RES2 = new Restaurant(RES_ID2, "Россия");
    public static final Restaurant RES3 = new Restaurant(RES_ID3, "Тройка");
    public static final Restaurant RES4 = new Restaurant(RES_ID4, "Сытинъ");

    public static final LocalDate DATE24 = LocalDate.of(2018, Month.MARCH, 24);
    public static final LocalDate DATE25 = LocalDate.of(2018, Month.MARCH, 25);

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "menu", "voices");
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("menu", "voices").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(List<RestaurantAndVoice> expected) {
        return content().json(writeValue(expected));
    }

    public static ResultMatcher contentJson(RestaurantAndVoice expected) {
        return content().json(writeValue(expected));
    }
}
