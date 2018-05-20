package ru.qf05.restaurants.data;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.qf05.restaurants.model.Restaurant;
import ru.qf05.restaurants.model.Voices;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static ru.qf05.restaurants.data.RestaurantData.RES1;
import static ru.qf05.restaurants.data.RestaurantData.RES2;
import static ru.qf05.restaurants.data.UserData.*;
import static ru.qf05.restaurants.web.json.JsonUtil.writeValue;

public class VoicesData {

    public static final Voices VOICES1 = new Voices(100019, USER_ID1, new Restaurant(RES1), LocalDate.of(2018, Month.MARCH, 24));
    public static final Voices VOICES2 = new Voices(100020, USER_ID2, new Restaurant(RES2), LocalDate.of(2018, Month.MARCH, 24));
    public static final Voices VOICES3 = new Voices(100021, ADMIN_ID, new Restaurant(RES1), LocalDate.of(2018, Month.MARCH, 24));
    public static final Voices VOICES4 = new Voices(100022, USER_ID1, new Restaurant(RES1), LocalDate.of(2018, Month.MARCH, 25));
    public static final Voices VOICES5 = new Voices(100023, USER_ID2, new Restaurant(RES2), LocalDate.of(2018, Month.MARCH, 25));


    public static void assertMatch(Voices actual, Voices expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Voices> actual, Voices... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Voices> actual, Iterable<Voices> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Voices... expected) {
        return content().json(writeValue(Arrays.asList(expected)));
    }
}
