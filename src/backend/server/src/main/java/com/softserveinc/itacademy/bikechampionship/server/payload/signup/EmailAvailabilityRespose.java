package com.softserveinc.itacademy.bikechampionship.server.payload.signup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailAvailabilityRespose {
    private Boolean available;
}
