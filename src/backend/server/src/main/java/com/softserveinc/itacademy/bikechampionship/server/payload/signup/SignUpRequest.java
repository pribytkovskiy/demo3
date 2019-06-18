package com.softserveinc.itacademy.bikechampionship.server.payload.signup;

import com.softserveinc.itacademy.bikechampionship.server.constraint.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
@FieldMatch(
    first = "password",
    second = "matchingPassword",
    message = "The password fields must match")
public class SignUpRequest {

    @NotBlank
    @Size(min = 2, max = 255, message = "First name should have at least 2 characters")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 255, message = "Last name should have at least 2 characters")
    private String lastName;

    @NotBlank
    @Email
    @Pattern(regexp = "[^@ ]+@[^@ ]+\\.[^@ ]+")
    private String email;

    @NotBlank
    @Size(min = 6, max = 16, message = "Password should be 6-16 characters long")
    private String password;

    @NotBlank
    private String matchingPassword;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{11,15}$")
    private String phoneNumber;
}
