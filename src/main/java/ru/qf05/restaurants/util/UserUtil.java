package ru.qf05.restaurants.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import ru.qf05.restaurants.model.Role;
import ru.qf05.restaurants.model.User;

import java.util.Collections;

public final class UserUtil {

    private UserUtil(){}

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        if (user.getRoles() == null || user.getRoles().size() < 1) {
            user.setRoles(Collections.singleton(Role.ROLE_USER));
        }
        String password = user.getPassword();
        user.setPassword(StringUtils.isEmpty(password) ? password : passwordEncoder.encode(password));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
