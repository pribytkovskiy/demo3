package com.softserveinc.itacademy.bikechampionship.server.payload.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @NotBlank
    private Long id;

    @NotBlank
    @Pattern(regexp = "[^@ ]+@[^@ ]+\\.[^@ ]+")
    @Size(min = 2, max = 255, message = "Last name should have at least 2 characters")
    private String email;

    @NotBlank
    @Size(min = 2, max = 255, message = "First name should have at least 2 characters")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 255, message = "First name should have at least 2 characters")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{11,15}$")
    private String phoneNumber;
}
