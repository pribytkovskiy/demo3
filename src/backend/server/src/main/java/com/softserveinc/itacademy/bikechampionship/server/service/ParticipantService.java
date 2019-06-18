package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.model.Participant;

import java.util.List;

public interface ParticipantService {
    Participant createParticipant(Participant participant);

    List<Participant> getParticipantsByUserId(Long id);

    List<Participant> getParticipantsByEventId(Long eventId);
}
