package com.softserveinc.itacademy.bikechampionship.server.payload.event;

import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EventRequest {

    private Long id;

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotNull
    private LocalDateTime dateTime;

    @NotBlank
    private String description;

    @NotNull
    private Long managerId;

    @Valid
    private List<CategoryRequest> categories;


}
