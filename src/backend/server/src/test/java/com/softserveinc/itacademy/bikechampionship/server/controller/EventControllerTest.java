package com.softserveinc.itacademy.bikechampionship.server.controller;

import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventResponse;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventSearchRequest;
import com.softserveinc.itacademy.bikechampionship.server.service.EventService;
import com.softserveinc.itacademy.bikechampionship.server.specification.EventSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.softserveinc.itacademy.bikechampionship.server.controller.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class EventControllerTest {
    @Mock
    private EventService eventService;
    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private EventController eventController;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    public void testGetAllEvents() throws Exception {
        mockMvc.perform(get("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(null)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetEventById() throws Exception {
        final Long eventId = 2L;
        final String eventName = "Dnipro Open 2018";

        Event expectedEvent = new Event();
        expectedEvent.setId(eventId);
        expectedEvent.setName(eventName);

        EventResponse expectedDto = new EventResponse();
        expectedDto.setId(eventId);
        expectedDto.setName(eventName);

        when(eventService.getEvent(eventId)).thenReturn(expectedEvent);
        when(conversionService.convert(any(Event.class), eq(EventResponse.class))).thenReturn(expectedDto);

        mockMvc.perform(get("/api/events/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(2)))
            .andExpect(jsonPath("name", is(eventName)));
    }
    
    @Test
    public void testCreateEvent() throws Exception {
        //given
        List<CategoryRequest> categoryRequests = new ArrayList<>();
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setId(2L);
        categoryRequest.setName("2222");
        categoryRequests.add(categoryRequest);

        EventRequest eventRequest = new EventRequest();
        eventRequest.setId(1L);
        eventRequest.setName("2222");
        eventRequest.setDateTime(LocalDateTime.now());
        eventRequest.setManagerId(2L);
        eventRequest.setDescription("22222");
        eventRequest.setCategories(categoryRequests);

        Event event = new Event();
        event.setId(1L);

        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(1L);

        //when
        when(conversionService.convert(any(EventRequest.class), eq(Event.class))).thenReturn(event);
        when(eventService.createEvent(any(Event.class))).thenReturn(event);
        when(conversionService.convert(any(Event.class), eq(EventResponse.class))).thenReturn(eventResponse);

        mockMvc.perform(post("/api/events").with(user("123").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(eventRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testEditEvent() throws Exception {
        Event oldEvent = new Event();
        oldEvent.setId(1L);
        oldEvent.setName("old name");

        Event newEvent = new Event();
        newEvent.setId(1L);
        newEvent.setName("new name");

        EventRequest eventRequest = new EventRequest();
        eventRequest.setId(1L);
        eventRequest.setName("old event");
        eventRequest.setDateTime(LocalDateTime.now());
        eventRequest.setManagerId(2L);
        eventRequest.setDescription("super description");

        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(1L);
        eventResponse.setName("new name");

        when(conversionService.convert(any(EventRequest.class), eq(Event.class))).thenReturn(oldEvent);
        when(eventService.editEvent(any(Event.class))).thenReturn(newEvent);
        when(conversionService.convert(any(Event.class), eq(EventResponse.class))).thenReturn(eventResponse);

        mockMvc.perform(put("/api/events").with(user("123").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(eventRequest)))
                .andExpect(status().isOk());

        verify(conversionService, times(1)).convert(any(EventRequest.class), eq(Event.class));
        verify(eventService, times(1)).editEvent(any(Event.class));
        verify(conversionService, times(1)).convert(any(Event.class), eq(EventResponse.class));
    }

    @Test
    public void getPageShouldReturnConvertedPageWithEvents() throws Exception {
        EventSearchRequest searchRequest = new EventSearchRequest();
        searchRequest.setName("Name");
        searchRequest.setDateTime(LocalDateTime.parse("2018-09-19T13:03:22"));
        searchRequest.setDescription("Description");
        searchRequest.setChampionshipId(1L);
        searchRequest.setCategoryIds(Arrays.asList(1L, 2L));
        searchRequest.setManagerId(1L);
        searchRequest.setParticipantIds(Arrays.asList(1L, 2L));
        EventSpecification eventSpecification = new EventSpecification(searchRequest);

        when(eventService.getPage(1, 1, eventSpecification)).thenReturn(Page.empty());
        //when
        mockMvc.perform(get("/api/events/page")
                .param("page", "1")
                .param("size", "1")
                .param("name", "Name")
                .param("dateTime", "2018-09-19T13:03:22")
                .param("description", "Description")
                .param("championshipId", "1")
                .param("categoryIds", "1", "2")
                .param("managerId", "1")
                .param("participantIds", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", equalTo(0)));
        //then
        verify(eventService, times(1)).getPage(1, 1, eventSpecification);
        verify(conversionService, never()).convert(any(), any());
    }
}