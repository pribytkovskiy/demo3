package com.softserveinc.itacademy.bikechampionship.server.controller;

import com.softserveinc.itacademy.bikechampionship.server.payload.login.LoginRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.signup.SignUpRequest;
import com.softserveinc.itacademy.bikechampionship.server.service.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest extends AbstractRestControllerTest {

    @Mock private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void signinUserShouldReturnOkStatusAndJson() throws Exception {

        //given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@admin.com");
        loginRequest.setPassword("admin");

        when(authenticationService.loginUser(any(LoginRequest.class))).thenReturn("some token");

        //when
        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignUpUser() throws Exception {
        //given
        SignUpRequest request = new SignUpRequest();
        request.setFirstName("Daniel");
        request.setLastName("Changer");
        request.setEmail("daniel_changer@yahoo.com");
        request.setPassword("rooting");
        request.setMatchingPassword("rooting");
        request.setPhoneNumber("+380975250866");

        when(authenticationService.registerUser(any(SignUpRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isCreated());
    }
}