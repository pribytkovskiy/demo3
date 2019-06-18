package com.softserveinc.itacademy.bikechampionship.commons.repository;

import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.commons.model.Participant;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParticipantRepository extends CrudRepository<Participant, Long> {

    List<Participant> getByUser(User user);

    List<Participant> getByEvent(Event event);

    List<Participant> getByEventId(Long eventId);
}
