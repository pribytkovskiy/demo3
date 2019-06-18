package com.softserveinc.itacademy.bikechampionship.server.controller;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.commons.model.Participant;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryResponse;
import com.softserveinc.itacademy.bikechampionship.server.payload.participant.ParticipantRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.participant.ParticipantResponse;
import com.softserveinc.itacademy.bikechampionship.server.service.ParticipantService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantControllerTest extends AbstractRestControllerTest {
    @Mock
    private ParticipantService participantService;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private ParticipantController participantController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(participantController).build();
    }

    @Test
    public void whenParticipantSuccessfullyCreatedThenParticipantIsReturnedAndServicesAreInvoked() throws Exception {
        // given
        Event event = new Event();
        event.setId(20L);

        Category category = new Category();
        category.setId(7L);

        CategoryResponse categoryResponse = new CategoryResponse(7L, null);

        ParticipantRequest givenParticipantRequest = new ParticipantRequest();
        givenParticipantRequest.setFirstName("Вася");
        givenParticipantRequest.setLastName("Конькобежец");
        givenParticipantRequest.setCompetitionNumber(50);
        givenParticipantRequest.setEventId(5L);
        givenParticipantRequest.setCategoryId(7L);

        Participant notSavedParticipant = new Participant();
        notSavedParticipant.setFirstName("Вася");
        notSavedParticipant.setLastName("Конькобежец");
        notSavedParticipant.setCompetitionNumber(50);
        notSavedParticipant.setEvent(event);
        notSavedParticipant.setCategory(category);

        Participant savedParticipant = new Participant();
        savedParticipant.setId(1L);
        savedParticipant.setFirstName("Вася");
        savedParticipant.setLastName("Конькобежец");
        savedParticipant.setCompetitionNumber(50);
        savedParticipant.setEvent(event);
        savedParticipant.setCategory(category);

        ParticipantResponse expectedParticipantResponse = new ParticipantResponse();
        expectedParticipantResponse.setId(1L);
        expectedParticipantResponse.setFirstName("Вася");
        expectedParticipantResponse.setLastName("Конькобежец");
        expectedParticipantResponse.setCompetitionNumber(50);
        expectedParticipantResponse.setEventId(5L);
        expectedParticipantResponse.setCategory(categoryResponse);

        // when
        when(conversionService.convert(any(ParticipantRequest.class), eq(Participant.class))).thenReturn(notSavedParticipant);
        when(participantService.createParticipant(any(Participant.class))).thenReturn(savedParticipant);
        when(conversionService.convert(any(Participant.class), eq(ParticipantResponse.class))).thenReturn(expectedParticipantResponse);

        mockMvc.perform(
                post("/api/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(givenParticipantRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(is(1)))
                .andExpect(jsonPath("firstName").value(is("Вася")))
                .andExpect(jsonPath("userId").value(nullValue()))
                .andExpect(jsonPath("eventId").value(is(5)));

        // check json deserialization from the request
        verify(conversionService, times(1)).convert(givenParticipantRequest, Participant.class);

        // check created Participant object before save
        verify(participantService, times(1)).createParticipant(notSavedParticipant);
    }

    @Test
    public void getParticipantsByUserIdShouldReturnParticipants() throws Exception {
        //given
        List<Participant> participants = Arrays.asList(new Participant(), new Participant());
        ParticipantResponse clob = new ParticipantResponse();
        clob.setFirstName("Clob");
        when(participantService.getParticipantsByUserId(anyLong())).thenReturn(participants);
        when(conversionService.convert(any(), any())).thenReturn(clob);
        //when
        mockMvc.perform(get("/api/participants").param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is(clob.getFirstName())))
                .andExpect(jsonPath("$[1].firstName", is(clob.getFirstName())));
        //then
        verify(participantService, times(1)).getParticipantsByUserId(anyLong());
        verify(conversionService, times(2)).convert(any(), any());
    }
}
