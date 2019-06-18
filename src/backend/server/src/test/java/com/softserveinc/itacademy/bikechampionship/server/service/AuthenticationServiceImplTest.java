package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.exception.AppException;
import com.softserveinc.itacademy.bikechampionship.commons.exception.BadRequestException;
import com.softserveinc.itacademy.bikechampionship.commons.model.Role;
import com.softserveinc.itacademy.bikechampionship.commons.model.RoleName;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.commons.repository.RoleRepository;
import com.softserveinc.itacademy.bikechampionship.commons.repository.UserRepository;
import com.softserveinc.itacademy.bikechampionship.server.payload.login.LoginRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.signup.SignUpRequest;
import com.softserveinc.itacademy.bikechampionship.server.security.JwtTokenProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private ConversionService conversionService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtTokenProvider jwtTokenProvider;

    private AuthenticationService authenticationService;

    @Before
    public void setUp() {
        authenticationService = new AuthenticationServiceImpl(
                userRepository, roleRepository, conversionService,
                passwordEncoder, authenticationManager, jwtTokenProvider);
    }

    @Test
    public void loginUserShouldSuccessfullyReturnJwtToken() {
        //given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@gmail.com");
        loginRequest.setPassword("password");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        String expected = "Token";
    
        //when
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(token);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn(expected);
        String actual = authenticationService.loginUser(loginRequest);

        //then
        assertEquals(expected, actual);
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(jwtTokenProvider, times(1)).generateToken(any(Authentication.class));
    }

    @Test
    public void registerUserShouldThrowExceptionWhenEmailExists() {
        //given
        expectedException.expect(BadRequestException.class);
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("Bad Email");
    
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
    
        //when
        authenticationService.registerUser(signUpRequest);
    }

    @Test
    public void registerUserShouldThrowExceptionWhenNoRolesSet() {
        //given
        expectedException.expect(AppException.class);
        SignUpRequest signUpRequest = new SignUpRequest();
        User user = new User();
    
        when(conversionService.convert(any(SignUpRequest.class), any())).thenReturn(user);
        when(roleRepository.findByName(any(RoleName.class))).thenReturn(Optional.empty());
    
        //when
        authenticationService.registerUser(signUpRequest);
    }

    @Test
    public void registerUserShouldReturnIdOfCreatedUser() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("Some Email");
        signUpRequest.setPassword("password");
        Optional<Role> role = Optional.of(new Role());
        User user = new User();
        user.setId(1L);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(conversionService.convert(any(SignUpRequest.class), any())).thenReturn(user);
        when(roleRepository.findByName(any(RoleName.class))).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn(signUpRequest.getPassword());

        //when
        Long createdId = authenticationService.registerUser(signUpRequest);

        //then
        assertEquals(user.getId(), createdId);
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(passwordEncoder, times(1)).encode(signUpRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(roleRepository, times(1)).findByName(any(RoleName.class));
        verify(conversionService, times(1)).convert(any(SignUpRequest.class), any());
    }
}