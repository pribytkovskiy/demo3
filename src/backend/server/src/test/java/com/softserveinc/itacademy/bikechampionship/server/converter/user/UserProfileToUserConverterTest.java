package com.softserveinc.itacademy.bikechampionship.server.converter.user;

import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.UserProfile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserProfileToUserConverterTest {

    private UserProfileToUserConverter converter;

    @Before
    public void setUp() {
        converter = new UserProfileToUserConverter();
    }

    @Test
    public void converterShouldSetAllFieldsFromUserProfile() {
        //given
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setEmail("email");
        userProfile.setFirstName("firstName");
        userProfile.setLastName("lastName");
        userProfile.setPhoneNumber("phoneNumber");

        //when
        User user = converter.convert(userProfile);

        //then
        assertEquals(user.getId(), userProfile.getId());
        assertEquals(user.getEmail(), userProfile.getEmail());
        assertEquals(user.getFirstName(), userProfile.getFirstName());
        assertEquals(user.getLastName(), userProfile.getLastName());
        assertEquals(user.getPhoneNumber(), userProfile.getPhoneNumber());
    }
}