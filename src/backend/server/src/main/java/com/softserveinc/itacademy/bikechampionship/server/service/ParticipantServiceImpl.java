package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.exception.BadRequestException;
import com.softserveinc.itacademy.bikechampionship.commons.model.Participant;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.commons.repository.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserService userService;

    public ParticipantServiceImpl(ParticipantRepository participantRepository,
                                  UserService userService) {
        this.participantRepository = participantRepository;
        this.userService = userService;
    }

    @Override
    public Participant createParticipant(Participant participant) {
        // check that participant doesn't have Id and service will create a new one but not update an existing participant
        if (participant.getId() != null && participant.getId() != 0) {
            throw new IllegalArgumentException("Cannot create new participant with specified Id");
        }
        User user = participant.getUser();
        participantRepository.getByEvent(participant.getEvent())
                .forEach(dbParticipant -> {
                    if (dbParticipant.getCompetitionNumber().equals(participant.getCompetitionNumber())) {
                        throw new BadRequestException("Participant number already occupied");
                    }
                    User dbUser = dbParticipant.getUser();
                    if (user != null && dbUser != null && dbUser.getId().equals(user.getId())) {
                        throw new BadRequestException("Already participating in this event");
                    }
                });

        return participantRepository.save(participant);
    }

    @Override
    @Transactional
    public List<Participant> getParticipantsByUserId(Long id) {
        User user = userService.getUserById(id);
        return user.getParticipants();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Participant> getParticipantsByEventId(Long eventId) {
        return participantRepository.getByEventId(eventId);
    }
}
