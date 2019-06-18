package com.softserveinc.itacademy.bikechampionship.server.converter;

import com.softserveinc.itacademy.bikechampionship.commons.model.Participant;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryResponse;
import com.softserveinc.itacademy.bikechampionship.server.payload.participant.ParticipantResponse;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

public class ParticipantToResponseConverter implements Converter<Participant, ParticipantResponse> {

    private ConversionService conversionService;

    public ParticipantToResponseConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ParticipantResponse convert(Participant participant) {
        final ParticipantResponse participantResponse = new ParticipantResponse();

        participantResponse.setId(participant.getId());
        participantResponse.setFirstName(participant.getFirstName());
        participantResponse.setLastName(participant.getLastName());
        participantResponse.setCompetitionNumber(participant.getCompetitionNumber());
        User user = participant.getUser();
        participantResponse.setUserId(user == null ? null : user.getId());

        if (participant.getTeam() != null) {
            participantResponse.setTeamId(participant.getTeam().getId());
        }

        participantResponse.setEventId(participant.getEvent().getId());
        participantResponse.setCategory(conversionService.convert(participant.getCategory(), CategoryResponse.class));

        return participantResponse;
    }
}
