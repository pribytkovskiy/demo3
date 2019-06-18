package com.softserveinc.itacademy.bikechampionship.server.converter;

import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.signup.SignUpRequest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class SignUpRequestToUserConverterTest {


    private static SignUpRequestToUserConverter converter;
    private static User userExpected;
    private static User userActual;

    @BeforeClass
    public static void init() {
        SignUpRequest request = new SignUpRequest();

        request.setEmail("daniel_changer@yahoo.com");
        request.setFirstName("Daniel");
        request.setLastName("Changer");
        request.setPhoneNumber("+380975250866");

        userExpected = new User();
        userExpected.setEmail("daniel_changer@yahoo.com");
        userExpected.setFirstName("Daniel");
        userExpected.setLastName("Changer");
        userExpected.setPhoneNumber("+380975250866");

        converter = new SignUpRequestToUserConverter();
        userActual = converter.convert(request);
    }

    @Test
    public void testPositiveConvertFirstName() {
        Assert.assertEquals(userExpected.getFirstName(), userActual.getFirstName());
    }

    @Test
    public void testPositiveConvertLastName() {
        Assert.assertEquals(userExpected.getLastName(), userActual.getLastName());
    }

    @Test
    public void testPositiveConvertEmail() {
        Assert.assertEquals(userExpected.getEmail(), userActual.getEmail());
    }

    @Test
    public void testConvertPhoneNumber() {
        Assert.assertEquals(userExpected.getPhoneNumber(), userActual.getPhoneNumber());
    }

    @Test
    public void testConvertCheckIfPasswordIsNull() {
        Assert.assertNull(userActual.getPassword());
    }
}