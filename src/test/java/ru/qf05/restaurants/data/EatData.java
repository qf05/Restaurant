package ru.qf05.restaurants.data;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.qf05.restaurants.model.Eat;
import ru.qf05.restaurants.model.Restaurant;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static ru.qf05.restaurants.data.RestaurantData.*;
import static ru.qf05.restaurants.web.json.JsonUtil.writeValue;

public class EatData {

    public static final int EAT1_ID = 100007;
    public static final int EAT2_ID = 100008;
    public static final int EAT3_ID = 100009;

    public static final Eat EAT1 = new Eat(100007, LocalDate.of(2018, Month.MARCH, 24), "Пельмени", 500, new Restaurant(RES1));
    public static final Eat EAT2 = new Eat(100008, LocalDate.of(2018, Month.MARCH, 24), "Голубцы", 700, new Restaurant(RES1));
    public static final Eat EAT3 = new Eat(100009, LocalDate.of(2018, Month.MARCH, 24), "Сок", 100, new Restaurant(RES1));
    public static final Eat EAT4 = new Eat(100010, LocalDate.of(2018, Month.MARCH, 24), "Суп из краба", 1200, new Restaurant(RES2));
    public static final Eat EAT5 = new Eat(100011, LocalDate.of(2018, Month.MARCH, 24), "Жаркое из кенгуру", 1500, new Restaurant(RES2));
    public static final Eat EAT6 = new Eat(100012, LocalDate.of(2018, Month.MARCH, 24), "Мясо", 800, new Restaurant(RES3));
    public static final Eat EAT7 = new Eat(100013, LocalDate.of(2018, Month.MARCH, 24), "Гарнир", 400, new Restaurant(RES3));
    public static final Eat EAT8 = new Eat(100014, LocalDate.of(2018, Month.MARCH, 24), "Фруктовый салат", 700, new Restaurant(RES3));
    public static final Eat EAT9 = new Eat(100015, LocalDate.of(2018, Month.MARCH, 24), "Шницель", 400, new Restaurant(RES3));
    public static final Eat EAT10 = new Eat(100016, LocalDate.of(2018, Month.MARCH, 24), "Шашлык", 600, new Restaurant(RES4));
    public static final Eat EAT11 = new Eat(100017, LocalDate.of(2018, Month.MARCH, 24), "Фондю", 1100, new Restaurant(RES4));
    public static final Eat EAT12 = new Eat(100018, LocalDate.of(2018, Month.MARCH, 25), "Драники", 350, new Restaurant(RES1));

    private static Comparator<Restaurant> restaurantComparator = (o1, o2) ->
            o1 == null || o2 == null || o1.getId().equals(o2.getId()) ? 0 : 1;

    public static void assertMatch(Eat actual, Eat expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "restaurant");
        assertThat(actual).usingComparatorForFields(restaurantComparator, "restaurant").isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Eat> actual, Eat... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Eat> actual, Iterable<Eat> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant").isEqualTo(expected);
        assertThat(actual).usingComparatorForElementFieldsWithNames(restaurantComparator, "restaurant")
                .usingFieldByFieldElementComparator()
                .isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Eat... expected) {
        return content().json(writeValue(Arrays.asList(expected)));
    }

    public static ResultMatcher contentJson(Eat expected) {
        return content().json(writeValue(expected));
    }

    public static List<Eat> getNewEatToday() {
        List<Eat> list = Arrays.asList(new Eat(EAT2), new Eat(EAT1), new Eat(EAT3));
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setDate(LocalDate.now());
            list.get(i).setId(100024 + i);
        }
        return list;
    }
}
