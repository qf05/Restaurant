package ru.qf05.restaurants.web.action;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.qf05.restaurants.data.EatData;
import ru.qf05.restaurants.model.Eat;
import ru.qf05.restaurants.service.EatService;
import ru.qf05.restaurants.service.RestaurantService;
import ru.qf05.restaurants.util.exception.ErrorType;
import ru.qf05.restaurants.web.AbstractControllerTest;
import ru.qf05.restaurants.web.TestUtil;
import ru.qf05.restaurants.web.json.JsonUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.qf05.restaurants.data.EatData.*;
import static ru.qf05.restaurants.data.RestaurantData.DATE24;
import static ru.qf05.restaurants.data.RestaurantData.RES_ID1;
import static ru.qf05.restaurants.data.UserData.ADMIN;
import static ru.qf05.restaurants.data.UserData.USER1;
import static ru.qf05.restaurants.web.TestUtil.userHttpBasic;
import static ru.qf05.restaurants.web.action.EatRestController.REST_URL_MENU;

public class EatRestControllerTest extends AbstractControllerTest {

    private static final String URL = REST_URL_MENU + "/";

    @Autowired
    private EatService service;

    @Autowired
    public RestaurantService restaurantService;

    @Test
    public void getIs() throws Exception {
        Eat expected = new Eat(EAT1);
        expected.setRestaurant(null);
        mockMvc.perform(get(URL + "is/" + EAT1_ID)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(expected));
    }

    @Test
    public void copyMenu() throws Exception {
        mockMvc.perform(get(URL + "copy/" + RES_ID1 + "?date=2018-03-24")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<Eat> list = restaurantService.get(RES_ID1).getMenu().stream()
                .filter(i->i.getDate().isEqual(LocalDate.now())).collect(Collectors.toList());
        assertMatch(list, EatData.getNewEatToday());
    }

    @Test
    public void create() throws Exception {
        Eat expected = new Eat(null, "wertytreerer", 500);
        ResultActions action = mockMvc.perform(post(URL + RES_ID1)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Eat returned = TestUtil.readFromJson(action, Eat.class);
        expected.setId(returned.getId());
        expected.setDate(returned.getDate());
        expected.setRestaurant(returned.getRestaurant());
        List<Eat> list = restaurantService.get(RES_ID1).getMenu().stream()
                .filter(i->i.getDate().isEqual(LocalDate.now())).collect(Collectors.toList());

        assertMatch(returned, expected);
        assertMatch(list, expected);
    }

    @Test
    public void update() throws Exception {
        Eat updated = new Eat(EAT1);
        updated.setPrice(321);
        updated.setName("vodka");

        mockMvc.perform(put(URL + RES_ID1 + "/" + EAT1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk());

        assertMatch(service.get(EAT1_ID), updated);
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(URL + EAT1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        List<Eat> list = restaurantService.get(RES_ID1).getMenu().stream()
                .filter(i->i.getDate().isEqual(DATE24)).collect(Collectors.toList());
        assertMatch(list, EAT2, EAT3);
    }

    @Test
    public void testDeleteForbidden() throws Exception {
        mockMvc.perform(delete(URL + EAT1_ID)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateForbidden() throws Exception {
        Eat updated = new Eat(EAT1);
        updated.setName("vodka");
        mockMvc.perform(put(URL + RES_ID1 + "/" + EAT1_ID)
                .with(userHttpBasic(USER1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateForbidden() throws Exception {
        Eat expected = new Eat(null, "Пельмени", 500);
        mockMvc.perform(post(URL + RES_ID1)
                .with(userHttpBasic(USER1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createValidError() throws Exception {
        Eat expected = new Eat(null, "", 500);
        MvcResult result = mockMvc.perform(post(URL + RES_ID1)
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
    public void createDuplicate() throws Exception {
        Eat expected = new Eat(null, "Мясо", 500);
        service.create(expected, RES_ID1);
        expected.setId(null);
        mockMvc.perform(post(URL + RES_ID1)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(ErrorType.DATA_ERROR));
    }

    @Test
    public void updateValidError() throws Exception {
        Eat updated = new Eat(EAT1);
        updated.setDate(LocalDate.now());
        updated.setPrice(321);
        updated.setName("");

        MvcResult result = mockMvc.perform(put(URL + RES_ID1 + "/" + EAT1_ID)
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
        Eat updated = new Eat(EAT1);
        updated.setPrice(321);
        updated.setName("Голубцы");

        mockMvc.perform(put(URL + RES_ID1 + "/" + EAT1_ID)
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
        Eat updated = new Eat(EAT1);
        updated.setPrice(321);
        updated.setId(ERROR_ID);

        mockMvc.perform(put(URL + RES_ID1 + "/" + ERROR_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get(URL + "is/" + ERROR_ID)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}