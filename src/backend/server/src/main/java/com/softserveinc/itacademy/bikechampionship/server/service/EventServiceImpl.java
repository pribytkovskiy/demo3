package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.exception.BadRequestException;
import com.softserveinc.itacademy.bikechampionship.commons.exception.ResourceNotFoundException;
import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.commons.repository.CategoryRepository;
import com.softserveinc.itacademy.bikechampionship.commons.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private CategoryRepository categoryRepository;

    public EventServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Event createEvent(Event event) {
        event.setCategories(event.getCategories()
            .stream()
            .map(c -> categoryRepository.findByName(c.getName())
                .orElseThrow(() -> new BadRequestException("No such category!")))
            .collect(Collectors.toList()));
        event = eventRepository.save(event);
        return event;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
    }

    @Override
    public Event editEvent(Event updatingEvent) {
        return eventRepository.save(updatingEvent);
    }

    @Override
    @Transactional
    public Page<Event> getPage(int page, int size, Specification<Event> specification) {
        if (page < 1) {
            throw new BadRequestException("Page cannot be less than one. Page: " + page);
        }
        if (size < 1) {
            throw new BadRequestException("Size cannot be less than one. Size: " + size);
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "id");
        return eventRepository.findAll(specification, pageable);
    }
}
