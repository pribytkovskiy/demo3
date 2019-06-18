package com.softserveinc.itacademy.bikechampionship.server.converter;

import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.signup.SignUpRequest;
import org.springframework.core.convert.converter.Converter;

public class SignUpRequestToUserConverter implements Converter<SignUpRequest, User> {

    @Override
    public User convert(SignUpRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        return user;
    }
}
