package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.server.payload.login.LoginRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.signup.SignUpRequest;

public interface AuthenticationService {

    String loginUser(LoginRequest loginRequest);

    Long registerUser(SignUpRequest signUpRequest);
}
