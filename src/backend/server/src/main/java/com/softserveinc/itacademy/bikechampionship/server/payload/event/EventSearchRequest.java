package com.softserveinc.itacademy.bikechampionship.server.payload.event;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventSearchRequest implements Serializable {

    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;
    private String description;
    private Long championshipId;
    private List<Long> categoryIds;
    private Long managerId;
    private List<Long> participantIds;
}
