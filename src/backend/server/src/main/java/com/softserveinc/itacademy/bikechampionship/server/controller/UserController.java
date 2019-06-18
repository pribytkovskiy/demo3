package com.softserveinc.itacademy.bikechampionship.server.controller;

import com.softserveinc.itacademy.bikechampionship.commons.exception.BadRequestException;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.signup.EmailAvailabilityRespose;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.CurrentUserResponse;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.UserPassword;
import com.softserveinc.itacademy.bikechampionship.server.payload.user.UserProfile;
import com.softserveinc.itacademy.bikechampionship.server.security.CurrentUser;
import com.softserveinc.itacademy.bikechampionship.server.security.UserPrincipal;
import com.softserveinc.itacademy.bikechampionship.server.service.UserService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    private ConversionService conversionService;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, ConversionService conversionService, PasswordEncoder passwordEncoder)
    {
        this.userService = userService;
        this.conversionService = conversionService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(conversionService.convert(userPrincipal, CurrentUserResponse.class));
    }

    @GetMapping("/checkEmailAvailability/{email}")
    public ResponseEntity<EmailAvailabilityRespose> checkEmailAvailability(@PathVariable String email) {
        Boolean isAvailable = userService.isEmailAvailable(email);
        return ResponseEntity.ok(new EmailAvailabilityRespose(isAvailable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserProfile userProfile = conversionService.convert(user, UserProfile.class);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping
    public ResponseEntity<UserProfile> editUserProfile(@CurrentUser UserPrincipal currentUser,
                                                       @RequestBody UserProfile userProfile) {
        if (currentUser.getId().equals(userProfile.getId())) {
            User user = conversionService.convert(userProfile, User.class);
            User editedUser = userService.editUserProfile(user);
            return ResponseEntity.ok(conversionService.convert(editedUser, UserProfile.class));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/password")
    public ResponseEntity<Long> changeUserPassword (@CurrentUser UserPrincipal currentUser,
                                              @RequestBody UserPassword userPassword) {
        if(!passwordEncoder.matches(userPassword.getOldPassword(), currentUser.getPassword()))
            throw new BadRequestException("Please, enter correct password!");
        User user = conversionService.convert(userPassword, User.class);
        user.setPassword(passwordEncoder.encode(userPassword.getPassword()));
        User editedUser = userService.changeUserPassword(user);
        return ResponseEntity.ok(editedUser.getId());
    }
}
