package ru.qf05.restaurants.web.action;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.qf05.restaurants.model.Voices;
import ru.qf05.restaurants.service.RestaurantService;
import ru.qf05.restaurants.web.AbstractControllerTest;
import ru.qf05.restaurants.web.TestUtil;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.qf05.restaurants.data.RestaurantData.*;
import static ru.qf05.restaurants.data.UserData.USER1;
import static ru.qf05.restaurants.data.VoicesData.*;
import static ru.qf05.restaurants.data.VoicesData.assertMatch;
import static ru.qf05.restaurants.web.TestUtil.userHttpBasic;
import static ru.qf05.restaurants.web.action.VoicesRestController.REST_URL_VOICE;

public class VoicesRestControllerTest extends AbstractControllerTest {

    private static final String URL = REST_URL_VOICE + "/";

    @Autowired
    public RestaurantService restaurantService;

    @Test
    public void voice() throws Exception {
        ResultActions action = mockMvc.perform(post(URL + RES_ID2)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isCreated());

        Voices returned = TestUtil.readFromJson(action, Voices.class);
        Voices v = new Voices(0, USER1.getId(), RES2, LocalDate.now());
        v.setId(returned.getId());
        List<Voices> list = restaurantService.get(RES_ID2).getVoices();

        assertMatch(list, VOICES2, VOICES5, v);
    }

    @Test
    public void voiceNotAuthorized() throws Exception {
        mockMvc.perform(post(URL + RES_ID1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}