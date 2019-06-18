package com.softserveinc.itacademy.bikechampionship.server.converter.user;

import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.UserPassword;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordToUserConverter implements Converter<UserPassword, User> {

    @Override
    public User convert(UserPassword userPassword) {
        if (userPassword == null) {
            throw new IllegalArgumentException("UserPassword cannot be converted because it's null");
        } else {
            User user = new User();
            user.setId(userPassword.getId());
            user.setPassword(userPassword.getPassword());
            return user;
        }

    }
}
