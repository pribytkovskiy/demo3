package com.softserveinc.itacademy.bikechampionship.server.controller;

import com.softserveinc.itacademy.bikechampionship.server.payload.login.JwtResponse;
import com.softserveinc.itacademy.bikechampionship.server.payload.login.LoginRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.signup.SignUpRequest;
import com.softserveinc.itacademy.bikechampionship.server.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@Api(value = "/api/auth")
public class AuthController {
    private AuthenticationService authenticationService;
    
    public AuthController (AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation(value = "to login anonymous user into a system")
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signinUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new JwtResponse(authenticationService.loginUser(loginRequest)));
    }

    @ApiOperation(value = "to sign up anonymous user into a system")
    @PostMapping("/signup")
    public ResponseEntity signUpUser(@Valid @RequestBody SignUpRequest request) {
        Long id = authenticationService.registerUser(request);
        URI location =
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
