package com.softserveinc.itacademy.bikechampionship.server.payload.participant;

import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryResponse;
import lombok.Data;

@Data
public class ParticipantResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer competitionNumber;
    private Long userId;
    private Long teamId;
    private Long eventId;
    private CategoryResponse category;
}
