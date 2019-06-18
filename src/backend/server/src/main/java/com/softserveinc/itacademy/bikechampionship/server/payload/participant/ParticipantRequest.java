package com.softserveinc.itacademy.bikechampionship.server.payload.participant;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class ParticipantRequest {
    @Positive
    private Long id;
    @Size(max = 255)
    private String firstName;
    @Size(max = 255)
    private String lastName;
    @NotNull
    @Positive
    private Integer competitionNumber;
    private Long userId;
    private Long teamId;
    @NotNull
    @Positive
    private Long eventId;
    @NotNull
    @Positive
    private Long categoryId;
}
