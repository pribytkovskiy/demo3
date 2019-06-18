package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface EventService {

    Event createEvent(Event event);

    List<Event> getAllEvents();

    Event getEvent(Long eventId);

    Event editEvent(Event updatingEvent);

    Page<Event> getPage(int page, int size, Specification<Event> specification);
}
