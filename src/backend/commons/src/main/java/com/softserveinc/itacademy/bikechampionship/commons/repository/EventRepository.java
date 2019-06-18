package com.softserveinc.itacademy.bikechampionship.commons.repository;

import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
}
