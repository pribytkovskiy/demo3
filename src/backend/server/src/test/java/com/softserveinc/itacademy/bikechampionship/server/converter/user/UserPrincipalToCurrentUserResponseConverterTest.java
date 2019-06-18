package com.softserveinc.itacademy.bikechampionship.server.converter.user;

import com.softserveinc.itacademy.bikechampionship.commons.model.Role;
import com.softserveinc.itacademy.bikechampionship.commons.model.RoleName;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.CurrentUserResponse;
import com.softserveinc.itacademy.bikechampionship.server.security.UserPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserPrincipalToCurrentUserResponseConverterTest {

    private UserPrincipalToCurrentUserResponseConverter converter;

    @Before
    public void setUp() {
        converter = new UserPrincipalToCurrentUserResponseConverter();
    }

    @Test
    public void converterShouldSetAllFieldsFromUserPrincipal() {
        //given
        Role userRole = new Role();
        userRole.setName(RoleName.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        GrantedAuthority userGrantedAuthority = new SimpleGrantedAuthority(userRole.getName().name());
        User user = new User();
        user.setId(1L);
        user.setEmail("email");
        user.setRoles(roles);
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        //when
        CurrentUserResponse currentUserResponse = converter.convert(userPrincipal);

        //then
        assertEquals(user.getId(), currentUserResponse.getId());
        assertEquals(user.getEmail(), currentUserResponse.getEmail());
        assertTrue(currentUserResponse.getGrantedAuthorities().contains(userGrantedAuthority));
    }
}
