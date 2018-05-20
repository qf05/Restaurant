package ru.qf05.restaurants.web.action;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.qf05.restaurants.model.Restaurant;
import ru.qf05.restaurants.service.RestaurantService;
import ru.qf05.restaurants.util.RestaurantUtil;
import ru.qf05.restaurants.util.exception.ErrorType;
import ru.qf05.restaurants.web.AbstractControllerTest;
import ru.qf05.restaurants.web.TestUtil;
import ru.qf05.restaurants.web.json.JsonUtil;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.qf05.restaurants.data.RestaurantData.*;
import static ru.qf05.restaurants.data.UserData.ADMIN;
import static ru.qf05.restaurants.data.UserData.USER1;
import static ru.qf05.restaurants.web.TestUtil.userHttpBasic;
import static ru.qf05.restaurants.web.action.RestaurantRestController.REST_URL_RESTAURANT;

public class RestaurantRestControllerTest extends AbstractControllerTest {

    private static final String URL = REST_URL_RESTAURANT + "/";

    @Autowired
    private RestaurantService service;


    @Test
    public void getIs() throws Exception {
        mockMvc.perform(get(URL + RES_ID1)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(RestaurantUtil.get(RES1)));
    }


    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get(URL + "?date=2018-03-24")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(RestaurantUtil.getAllWidthMenu(Arrays.asList(RES1, RES2, RES4, RES3),DATE24)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void getAllToDate() throws Exception {
        mockMvc.perform(get(URL + "date?date=2018-03-24")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(RestaurantUtil.getAll(Arrays.asList(RES1, RES2, RES4, RES3), DATE24)));
    }

    @Test
    public void getAllBetweenOfVoices() throws Exception {
        mockMvc.perform(get(URL + "history?startDate=2018-03-24&endDate=2018-03-25" +
                "&dateMenu=2018-03-24")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(RestaurantUtil.getAll(Arrays.asList(RES1, RES2, RES4, RES3), DATE24, DATE25, DATE24)));
    }

    @Test
    public void getAllBetweenOfVoicesAndMenu() throws Exception {
        mockMvc.perform(get(URL + "history/menu?startDateVoice=2018-03-24&endDateVoice=2018-03-25" +
                "&startDateMenu=2018-03-24&endDateMenu=2018-03-25")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(RestaurantUtil.getAll(Arrays.asList(RES1, RES2, RES4, RES3), DATE24, DATE25, DATE24, DATE25)));
    }

    @Test
    public void add() throws Exception {
        Restaurant expected = new Restaurant(null, "Диззи");
        ResultActions action = mockMvc.perform(post(URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Restaurant returned = TestUtil.readFromJson(action, Restaurant.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(service.getAll(), expected, RES1, RES2, RES4, RES3);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(URL + RES_ID1)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(), RES2, RES4, RES3);
    }

    @Test
    public void update() throws Exception {
        Restaurant updated = new Restaurant(RES1);
        updated.setName("KFC");
        mockMvc.perform(put(URL + RES_ID1)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk());

        assertMatch(service.get(RES_ID1), updated);
    }

    @Test
    public void testDeleteForbidden() throws Exception {
        mockMvc.perform(delete(URL + RES_ID1)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateForbidden() throws Exception {
        Restaurant updated = new Restaurant(RES1);
        updated.setName("KFC");
        mockMvc.perform(put(URL + RES_ID1)
                .with(userHttpBasic(USER1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateForbidden() throws Exception {
        Restaurant expected = new Restaurant(null, "Диззи");
        mockMvc.perform(post(URL)
                .with(userHttpBasic(USER1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addValidError() throws Exception {
        Restaurant expected = new Restaurant(null, "");
        MvcResult result = mockMvc.perform(post(URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
        Assert.assertTrue(result.getResponse().getContentAsString().contains(" must not be blank"));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void addDuplicate() throws Exception {
        Restaurant expected = new Restaurant(null, "Палкин");
        mockMvc.perform(post(URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(ErrorType.DATA_ERROR));
    }

    @Test
    public void updateValidError() throws Exception {
        Restaurant updated = new Restaurant(RES1);
        updated.setName("");
        MvcResult result = mockMvc.perform(put(URL + RES_ID1)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
        Assert.assertTrue(result.getResponse().getContentAsString().contains(" must not be blank"));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void updateDuplicate() throws Exception {
        Restaurant updated = new Restaurant(RES1);
        updated.setName("Россия");
        mockMvc.perform(put(URL + RES_ID1)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(ErrorType.DATA_ERROR));
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(URL + ERROR_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        Restaurant updated = new Restaurant(RES1);
        updated.setName("KFC");
        updated.setId(ERROR_ID);
        mockMvc.perform(put(URL + ERROR_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get(URL + ERROR_ID)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}