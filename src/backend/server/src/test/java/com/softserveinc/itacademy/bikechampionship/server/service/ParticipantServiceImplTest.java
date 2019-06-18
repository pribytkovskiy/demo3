package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.commons.model.Participant;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.commons.repository.ParticipantRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ParticipantServiceImplTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private UserService userService;

    @Mock
    private EventService eventService;

    @InjectMocks
    private ParticipantServiceImpl participantService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createParticipantTest() {
        // given
        User user = new User();
        user.setId(10L);

        Event event = new Event();
        event.setId(20L);
        event.setParticipants(new ArrayList<>());

        Participant notSavedParticipant = new Participant();
        notSavedParticipant.setFirstName("Вася");
        notSavedParticipant.setLastName("Конькобежец");
        notSavedParticipant.setCompetitionNumber(50);
        notSavedParticipant.setUser(user);
        notSavedParticipant.setEvent(event);

        Participant savedParticipant = new Participant();
        savedParticipant.setId(1L);
        savedParticipant.setFirstName("Вася");
        savedParticipant.setLastName("Конькобежец");
        savedParticipant.setCompetitionNumber(50);
        savedParticipant.setUser(user);
        savedParticipant.setEvent(event);

        when(participantRepository.save(any(Participant.class))).thenReturn(savedParticipant);
        when(eventService.getEvent(anyLong())).thenReturn(event);

        Participant actualParticipant = participantService.createParticipant(notSavedParticipant);

        assertEquals(savedParticipant, actualParticipant);

        verify(participantRepository, times(1)).save(notSavedParticipant);
    }

    @Test
    public void shouldNotCreateParticipantWithSpecifiedId() {
        // given a participant to save with some id
        Participant notSavedParticipant = new Participant();
        notSavedParticipant.setId(9L);

        when(participantRepository.save(any(Participant.class))).thenReturn(new Participant());

        try {
            participantService.createParticipant(notSavedParticipant);
            fail("IllegalArgumentException was expected but nothing was thrown");
        } catch (IllegalArgumentException actual) {
            assertEquals(actual.getClass(), IllegalArgumentException.class);
            // check there is no attempt to save participant with specified Id
            verify(participantRepository, never()).save(any());
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but " + e.getClass() + " was thrown");
        }
    }

    @Test
    public void getParticipantByUserIdShouldReturnParticipants() {
        //given
        User user = new User();
        user.setParticipants(Arrays.asList(new Participant(), new Participant()));
        when(userService.getUserById(anyLong())).thenReturn(user);
        //when
        List<Participant> participants = participantService.getParticipantsByUserId(1L);
        //then
        assertEquals(user.getParticipants().size(), participants.size());
        verify(userService, times(1)).getUserById(anyLong());
    }
}
