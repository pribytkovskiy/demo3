package com.softserveinc.itacademy.bikechampionship.server.converter.user;

import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.UserPassword;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserPasswordToUserConverterTest {

    private UserPasswordToUserConverter converter;

    @Before
    public void setUp() {
        converter = new UserPasswordToUserConverter();
    }

    @Test
    public void converterShouldSetAllFieldsFromUserPassword() {
        //given
        UserPassword userPassword = new UserPassword();
        userPassword.setId(1L);
        userPassword.setPassword("password");

        //when
        User user = converter.convert(userPassword);

        //then
        assertEquals(user.getId(), userPassword.getId());
        assertEquals(user.getPassword(), userPassword.getPassword());
    }
}