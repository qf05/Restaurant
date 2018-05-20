package ru.qf05.restaurants.web.json;

import org.junit.Assert;
import org.junit.Test;
import ru.qf05.restaurants.data.UserData;
import ru.qf05.restaurants.model.Eat;
import ru.qf05.restaurants.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static ru.qf05.restaurants.data.EatData.EAT1;
import static ru.qf05.restaurants.data.EatData.assertMatch;

public class JsonUtilTest {

    @Test
    public void testReadWriteValue() throws Exception {
        String json = JsonUtil.writeValue(EAT1);
        System.out.println(json);
        Eat eat = JsonUtil.readValue(json, Eat.class);
        assertMatch(eat, EAT1);
    }

    @Test
    public void testWriteOnlyAccess() throws Exception {
        String json = JsonUtil.writeValue(UserData.USER1);
        System.out.println(json);
        assertThat(json, not(containsString("password")));
        String jsonWithPass = UserData.jsonWithPassword(UserData.USER1, "newPass");
        System.out.println(jsonWithPass);
        User user = JsonUtil.readValue(jsonWithPass, User.class);
        Assert.assertEquals(user.getPassword(), "newPass");
    }
}