package com.softserveinc.itacademy.bikechampionship.server.converter;


import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventRequest;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

public class EventRequestToEventConverter implements Converter<EventRequest, Event> {
    private ConversionService conversionService;

    public EventRequestToEventConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Event convert(EventRequest eventRequest) {
        Event event = new Event();
        event.setId(eventRequest.getId());
        event.setName(eventRequest.getName());
        event.setDateTime(eventRequest.getDateTime());
        event.setDescription(eventRequest.getDescription());
        List<Category> list = eventRequest.getCategories().stream()
                .map(categoryRequest -> conversionService.convert(categoryRequest, Category.class))
                .collect(Collectors.toList());
        User manager = new User();
        manager.setId(eventRequest.getManagerId());
        event.setManager(manager);
        event.setCategories(list);
        return event;
    }
}
