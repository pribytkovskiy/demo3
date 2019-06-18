package com.softserveinc.itacademy.bikechampionship.server.converter;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.commons.model.Participant;
import com.softserveinc.itacademy.bikechampionship.commons.model.Team;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.participant.ParticipantRequest;
import org.springframework.core.convert.converter.Converter;

public class RequestToParticipantConverter implements Converter<ParticipantRequest, Participant> {

    @Override
    public Participant convert(ParticipantRequest participantRequest) {
        final Participant participant = new Participant();

        participant.setId(participantRequest.getId());
        participant.setFirstName(participantRequest.getFirstName());
        participant.setLastName(participantRequest.getLastName());
        participant.setCompetitionNumber(participantRequest.getCompetitionNumber());

        if (participantRequest.getUserId() != null && participantRequest.getUserId() > 0L) {
            User user = new User();
            user.setId(participantRequest.getUserId());
            participant.setUser(user);
        }
        if (participantRequest.getTeamId() != null && participantRequest.getTeamId() > 0L) {
            Team team = new Team();
            team.setId(participantRequest.getTeamId());
            participant.setTeam(team);
        }

        Event event = new Event();
        event.setId(participantRequest.getEventId());
        participant.setEvent(event);

        Category category = new Category();
        category.setId(participantRequest.getCategoryId());
        participant.setCategory(category);

        return participant;
    }
}
