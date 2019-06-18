package com.softserveinc.itacademy.bikechampionship.server.converter.user;

import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.UserProfile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserToUserProfileConverterTest {

    private UserToUserProfileConverter converter;

    @Before
    public void setUp() {
        converter = new UserToUserProfileConverter();
    }

    @Test
    public void converterShouldSetAllFieldsFromUser() {
        //given
        User user = new User();
        user.setId(1L);
        user.setEmail("email");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhoneNumber("phoneNumber");

        //when
        UserProfile userProfile = converter.convert(user);

        //then
        assertEquals(user.getId(), userProfile.getId());
        assertEquals(user.getEmail(), userProfile.getEmail());
        assertEquals(user.getFirstName(), userProfile.getFirstName());
        assertEquals(user.getLastName(), userProfile.getLastName());
        assertEquals(user.getPhoneNumber(), userProfile.getPhoneNumber());
    }
}