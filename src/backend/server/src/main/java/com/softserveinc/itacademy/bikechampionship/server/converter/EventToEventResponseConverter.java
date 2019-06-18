package com.softserveinc.itacademy.bikechampionship.server.converter;

import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryResponse;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventResponse;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

public class EventToEventResponseConverter implements Converter<Event, EventResponse> {
    private ConversionService conversionService;

    public EventToEventResponseConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public EventResponse convert(Event event) {
        List<CategoryResponse> categoryResponses = event.getCategories().stream()
                .map(category -> conversionService.convert(category, CategoryResponse.class))
                .collect(Collectors.toList());
        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(event.getId());
        eventResponse.setName(event.getName());
        eventResponse.setDescription(event.getDescription());
        eventResponse.setDateTime(event.getDateTime());
        eventResponse.setManagerId(event.getManager().getId());
        eventResponse.setCategories(categoryResponses);
        return eventResponse;
    }
}
