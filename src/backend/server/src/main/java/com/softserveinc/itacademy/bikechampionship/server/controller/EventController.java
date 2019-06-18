package com.softserveinc.itacademy.bikechampionship.server.controller;

import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventResponse;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventSearchRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.participant.ParticipantResponse;
import com.softserveinc.itacademy.bikechampionship.server.service.EventService;
import com.softserveinc.itacademy.bikechampionship.server.service.ParticipantService;
import com.softserveinc.itacademy.bikechampionship.server.specification.EventSpecification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@Api(value = "/api/events")
public class EventController {
    private EventService eventService;
    private ParticipantService participantService;
    private ConversionService conversionService;

    public EventController(EventService eventService,
                           ParticipantService participantService,
                           ConversionService conversionService) {
        this.eventService = eventService;
        this.participantService = participantService;
        this.conversionService = conversionService;
    }

    @ApiOperation(value = "getting all available events")
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> eventResponses = eventService.getAllEvents()
                .stream().map(event -> conversionService.convert(event, EventResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventResponses);
    }

    @ApiOperation(value = "getting an event by id")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long eventId) {
        Event event = eventService.getEvent(eventId);
        EventResponse eventResponse = conversionService.convert(event, EventResponse.class);
        return ResponseEntity.ok(eventResponse);
    }

    @ApiOperation(value = "create new event")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        Event newEvent = conversionService.convert(eventRequest, Event.class);
        newEvent = eventService.createEvent(newEvent);
        EventResponse newEventResponse = conversionService.convert(newEvent, EventResponse.class);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/events/{id}")
                .buildAndExpand(newEventResponse.getId()).toUri();
        return ResponseEntity.created(location).body(newEventResponse);
    }

    @ApiOperation(value = "updating event's info")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EventResponse> editEvent(@Valid @RequestBody EventRequest eventRequest) {
        Event updatingEvent = conversionService.convert(eventRequest, Event.class);
        updatingEvent = eventService.editEvent(updatingEvent);
        EventResponse updatingEventResponse = conversionService.convert(updatingEvent, EventResponse.class);
        return ResponseEntity.ok(updatingEventResponse);
    }

    @ApiOperation(value = "get events page with filtering")
    @GetMapping("/page")
    public ResponseEntity<Page<EventResponse>> getEventsPage(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            EventSearchRequest eventSearchRequest) {

        Page<EventResponse> eventResponses = eventService
                .getPage(page, size, new EventSpecification(eventSearchRequest))
                .map(event -> conversionService.convert(event, EventResponse.class));
        return ResponseEntity.ok(eventResponses);
    }

    @ApiOperation(value = "get participants list of specified event")
    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getEventParticipants(@PathVariable Long eventId) {
        return ResponseEntity.ok(participantService.getParticipantsByEventId(eventId)
                .stream()
                .map(participant -> conversionService.convert(participant, ParticipantResponse.class))
                .collect(Collectors.toList()));
    }
}
