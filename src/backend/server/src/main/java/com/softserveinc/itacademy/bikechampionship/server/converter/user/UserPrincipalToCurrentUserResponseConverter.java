package com.softserveinc.itacademy.bikechampionship.server.converter.user;

import com.softserveinc.itacademy.bikechampionship.server.payload.user.CurrentUserResponse;
import com.softserveinc.itacademy.bikechampionship.server.security.UserPrincipal;
import org.springframework.core.convert.converter.Converter;

public class UserPrincipalToCurrentUserResponseConverter implements Converter<UserPrincipal, CurrentUserResponse> {

    @Override
    public CurrentUserResponse convert(UserPrincipal userPrincipal) {
        return new CurrentUserResponse(
                userPrincipal.getId(),
                userPrincipal.getEmail(),
                userPrincipal.getAuthorities());
    }
}
