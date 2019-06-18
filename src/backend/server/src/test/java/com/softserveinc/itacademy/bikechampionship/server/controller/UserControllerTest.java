package com.softserveinc.itacademy.bikechampionship.server.controller;

import com.softserveinc.itacademy.bikechampionship.commons.model.Role;
import com.softserveinc.itacademy.bikechampionship.commons.model.RoleName;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.CurrentUserResponse;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.UserPassword;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.UserProfile;
import com.softserveinc.itacademy.bikechampionship.server.security.UserPrincipal;
import com.softserveinc.itacademy.bikechampionship.server.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest extends AbstractRestControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private ConversionService conversionService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void getCurrentUserShouldReturnConvertedUserPrincipal() throws Exception {
        //given
        User user = new User();
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        CurrentUserResponse expected = new CurrentUserResponse(1L, "email", Collections.emptyList());

        when(conversionService.convert(userPrincipal, CurrentUserResponse.class)).thenReturn(expected);

        //when
        mockMvc.perform(get("/api/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(expected.getId().intValue())))
            .andExpect(jsonPath("$.email", equalTo(expected.getEmail())))
            .andExpect(jsonPath("$.grantedAuthorities", hasSize(0)));

        //then
        verify(conversionService, times(1)).convert(any(UserPrincipal.class), any());
        verifyNoMoreInteractions(conversionService);
    }

    @Test
    public void isEmailAvailableShowReturnOkStatusAndIsAvailableAsTrue() throws Exception {
        //given
        String email = "email";

        when(userService.isEmailAvailable(anyString())).thenReturn(true);

        //when
        mockMvc.perform(get("/api/users/checkEmailAvailability/" + email))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.available", equalTo(true)));

        //then
        verify(userService, times(1)).isEmailAvailable(anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getUserProfileShowReturnOkStatusAndUserProfile() throws Exception {
        //given
        User user = new User();
        UserProfile userProfile = new UserProfile(1L, "email", "firstName", "lastName", "phoneNumber");

        when(userService.getUserById(anyLong())).thenReturn(user);
        when(conversionService.convert(user, UserProfile.class)).thenReturn(userProfile);

        //when
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.email", equalTo(userProfile.getEmail())))
            .andExpect(jsonPath("$.firstName", equalTo(userProfile.getFirstName())))
            .andExpect(jsonPath("$.lastName", equalTo(userProfile.getLastName())))
            .andExpect(jsonPath("$.phoneNumber", equalTo(userProfile.getPhoneNumber())));

        //then
        verify(userService, times(1)).getUserById(anyLong());
        verify(conversionService, times(1)).convert(user, UserProfile.class);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void editUserProfileShowReturnOkStatusAndUserProfile() throws Exception {

        //given
        UserProfile userProfile = new UserProfile(
            1L, "artyomsetko@gmail.com", "Artem",
            "Setko", "+380958167610"
        );

        User user = new User();
        user.setId(1L);
        user.setEmail("artyomsetko@gmail.com");
        user.setFirstName("Artem");
        user.setLastName("Setko");
        user.setPhoneNumber("+380958167610");
        Role userRole = new Role();
        userRole.setName(RoleName.ROLE_USER);
        user.setRoles(Collections.singleton(userRole));

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver()).build();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(UserPrincipal.create(user), null,
                UserPrincipal.create(user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        when(conversionService.convert(any(UserProfile.class), any())).thenReturn(user);
        when(userService.editUserProfile(any(User.class))).thenReturn(user);
        when(conversionService.convert(any(User.class), any())).thenReturn(userProfile);

        mockMvc.perform(put("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userProfile)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.email", equalTo(userProfile.getEmail())))
            .andExpect(jsonPath("$.firstName", equalTo(userProfile.getFirstName())))
            .andExpect(jsonPath("$.lastName", equalTo(userProfile.getLastName())))
            .andExpect(jsonPath("$.phoneNumber", equalTo(userProfile.getPhoneNumber())));
        //then
        verify(userService, times(1)).editUserProfile(user);
        verify(conversionService, times(1)).convert(any(UserProfile.class), any());
        verify(conversionService, times(1)).convert(any(User.class), any());
        verifyNoMoreInteractions(conversionService);
    }

    @Test
    public void changeUserPasswordShouldReturnOkStatus() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setEmail("artyomsetko@gmail.com");
        user.setFirstName("Artem");
        user.setLastName("Setko");
        user.setPhoneNumber("+380958167610");
        user.setPassword("password");

        UserPassword userPassword = new UserPassword();
        userPassword.setId(1L);
        userPassword.setPassword("password");
        userPassword.setMatchingPassword("password");
        userPassword.setOldPassword("administrator");

        when(conversionService.convert(any(UserPassword.class), any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("password");
        when(userService.changeUserPassword(any())).thenReturn(user);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        mockMvc.perform(put("/api/users/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userPassword)))
            .andExpect(status().isOk());
    }

}