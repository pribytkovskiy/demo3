package com.softserveinc.itacademy.bikechampionship.server.payload.category;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CategoryRequest {
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;
}
