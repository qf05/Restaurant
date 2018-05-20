package ru.qf05.restaurants.web;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.qf05.restaurants.model.User;
import ru.qf05.restaurants.web.json.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static ru.qf05.restaurants.data.EatData.*;
import static ru.qf05.restaurants.data.RestaurantData.*;
import static ru.qf05.restaurants.data.VoicesData.*;

public class TestUtil {

    public static String getContent(ResultActions action) throws UnsupportedEncodingException {
        return action.andReturn().getResponse().getContentAsString();
    }

    public static ResultActions print(ResultActions action) throws UnsupportedEncodingException {
        System.out.println(getContent(action));
        return action;
    }

    public static <T> T readFromJson(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(action), clazz);
    }

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    public static void setProperty() {
        RES1.setVoices(Arrays.asList(VOICES1, VOICES3, VOICES4));
        RES2.setVoices(Arrays.asList(VOICES2, VOICES5));
        RES1.setMenu(Arrays.asList(EAT1, EAT2, EAT3, EAT12));
        RES2.setMenu(Arrays.asList(EAT4, EAT5));
        RES3.setMenu(Arrays.asList(EAT6, EAT7, EAT8, EAT9));
        RES4.setMenu(Arrays.asList(EAT10, EAT11));
    }
}
