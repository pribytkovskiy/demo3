package com.softserveinc.itacademy.bikechampionship.server.payload.event;

import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EventResponse {
    private Long id;
    private String name;
    private LocalDateTime dateTime;
    private String description;
    private Long managerId;
    private List<CategoryResponse> categories;
}
