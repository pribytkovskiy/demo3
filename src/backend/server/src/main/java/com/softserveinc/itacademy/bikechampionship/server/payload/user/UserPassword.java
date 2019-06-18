package com.softserveinc.itacademy.bikechampionship.server.payload.user;

import com.softserveinc.itacademy.bikechampionship.server.constraint.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@FieldMatch(
        first = "password",
        second = "matchingPassword",
        message = "The password fields must match")
public class UserPassword {

    @NotBlank
    private Long id;

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 6, max = 16, message = "UserPassword should be 6-16 characters long")
    private String password;

    @NotBlank
    @Size(min = 6, max = 16, message = "UserPassword should be 6-16 characters long")
    private String matchingPassword;
}
