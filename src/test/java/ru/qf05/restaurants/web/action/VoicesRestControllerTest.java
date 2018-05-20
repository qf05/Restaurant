package ru.qf05.restaurants.web.action;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.qf05.restaurants.model.Voices;
import ru.qf05.restaurants.service.VoicesService;
import ru.qf05.restaurants.util.DateTimeUtil;
import ru.qf05.restaurants.web.AbstractControllerTest;
import ru.qf05.restaurants.web.TestUtil;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.qf05.restaurants.data.RestaurantData.RES1;
import static ru.qf05.restaurants.data.RestaurantData.RES_ID1;
import static ru.qf05.restaurants.data.UserData.USER1;
import static ru.qf05.restaurants.data.VoicesData.*;
import static ru.qf05.restaurants.web.TestUtil.userHttpBasic;
import static ru.qf05.restaurants.web.action.VoicesRestController.REST_URL_VOICE;

public class VoicesRestControllerTest extends AbstractControllerTest {

    private static final String URL = REST_URL_VOICE + "/";

    @Autowired
    private VoicesService service;

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get(URL + "?date=2018-03-24")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOICES1, VOICES2, VOICES3));
    }

    @Test
    public void getBetween() throws Exception {
        mockMvc.perform(get(URL + "history?startDate=2018-03-24&endDate=2018-03-25")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOICES1, VOICES2, VOICES3, VOICES4, VOICES5));
    }

    @Test
    public void getBetweenWidthNull() throws Exception {
        mockMvc.perform(get(URL + "history?startDate=")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOICES1, VOICES2, VOICES3, VOICES4, VOICES5));
    }

    @Test
    public void voice() throws Exception {
        ResultActions action = mockMvc.perform(post(URL + RES_ID1)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isCreated());

        Voices returned = TestUtil.readFromJson(action, Voices.class);
        Voices v = new Voices(0, USER1.getId(), RES1, LocalDate.now());
        v.setId(returned.getId());

        assertMatch(service.getAllBetween(DateTimeUtil.MIN_DATE, DateTimeUtil.MAX_DATE), VOICES1, VOICES2, VOICES3, VOICES4, VOICES5, v);
    }

    @Test
    public void getAllToRestaurant() throws Exception {
        mockMvc.perform(get(URL + RES_ID1 + "?date=2018-03-24")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOICES1, VOICES3));
    }

    @Test
    public void getAllToRestaurantHistory() throws Exception {
        mockMvc.perform(get(URL + "history/" + RES_ID1 + "?startDate=2018-03-24&endDate=2018-03-25")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOICES1, VOICES3, VOICES4));
    }

    @Test
    public void getAllToRestaurantNull() throws Exception {
        mockMvc.perform(get(URL + RES_ID1 + "?date=")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getAllToRestaurantHistoryNull() throws Exception {
        mockMvc.perform(get(URL + "history/" + RES_ID1 + "?startDate=")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOICES1, VOICES3, VOICES4));
    }

    @Test
    public void voiceNotAuthorized() throws Exception {
        mockMvc.perform(post(URL + RES_ID1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}