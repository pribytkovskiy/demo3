package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.exception.BadRequestException;
import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.commons.repository.CategoryRepository;
import com.softserveinc.itacademy.bikechampionship.commons.repository.EventRepository;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventResponse;
import com.softserveinc.itacademy.bikechampionship.server.specification.EventSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EventServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private EventRepository eventRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private EventService eventService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventService = new EventServiceImpl(eventRepository, categoryRepository);
    }

    @Test
    public void whenCategoryDoesNotExistThenCreateNewEventThrowsException() {
        // given
        expectedException.expect(BadRequestException.class);

        Event event = new Event();
        event.setCategories(Collections.singletonList(new Category()));

        // when
        when(categoryRepository.findByName(null))
            .thenReturn(Optional.empty());

        eventService.createEvent(event);

        fail("Expected a BadRequestException to be thrown");
    }

    @Test
    public void whenOneOfCategoriesDoesNotExistThenCreateNewEventThrowsException() {
        // given
        Category category = new Category();
        Event event = new Event();
        event.setCategories(Collections.nCopies(3, category));

        when(categoryRepository.findByName(null))
            .thenReturn(Optional.of(category))
            .thenReturn(Optional.empty())
            .thenReturn(Optional.of(category));

        // when
        try {
            eventService.createEvent(event);
            fail("Expected a BadRequestException to be thrown");
        } catch (BadRequestException e) {
            verify(categoryRepository, atLeastOnce()).findByName(null);
        }
    }

    @Test
    public void whenEventCreatedSuccessfullyThenEventReturned() {
        // given
        Event expected = new Event();
        expected.setCategories(new ArrayList<>());

        // when
        when(eventRepository.save(any(Event.class)))
            .thenReturn(expected);

        Event actual = eventService.createEvent(expected);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void whenGetAllEventsInvokedAndZeroEventsFoundThenEmptyListOfEventResponseIsReturned() {
        // given
        Iterable<EventResponse> expected = new ArrayList<>();

        // when
        when(eventRepository.findAll())
            .thenReturn(new ArrayList<>());

        Iterable actual = eventService.getAllEvents();

        // then
        verify(eventRepository, times(1)).findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void getEventShouldReturnExistingEvent() {
        // given
        Event expected = new Event();
        expected.setId(2L);

        // when
        when(eventRepository.findById(2L))
            .thenReturn(Optional.of(expected));

        Event actual = eventService.getEvent(2L);

        // then
        verify(eventRepository, times(1)).findById(2L);

        assertEquals(expected, actual);
    }

    @Test
    public void whenEditEventThenReturnEvent() {
        // given
        Event expected = new Event();
        expected.setCategories(new ArrayList<>());

        when(eventRepository.save(any(Event.class))).thenReturn(expected);

        // when
        Event actual = eventService.editEvent(expected);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void getPageShouldThrowExceptionWhenPageIsLessThanOne() {
        //given
        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Page cannot be less than one. Page: 0");
        //when
        eventService.getPage(0, 12, null);
    }

    @Test
    public void getPageShouldThrowExceptionWhenSizeIsLessThanOne() {
        //given
        expectedException.expect(BadRequestException.class);
        expectedException.expectMessage("Size cannot be less than one. Size: 0");
        //when
        eventService.getPage(1, 0, null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getPageShouldReturnEventsSuccessfully() {
        //given
        Page<Event> expected = Page.empty();
        when(eventRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expected);
        //when
        Page<Event> actual = eventService.getPage(1, 1, new EventSpecification(null));
        //then
        assertEquals(expected, actual);
        verify(eventRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }
}
