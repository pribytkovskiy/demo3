package com.softserveinc.itacademy.bikechampionship.commons.repository;

import com.softserveinc.itacademy.bikechampionship.commons.model.Championship;
import org.springframework.data.repository.CrudRepository;

public interface ChampionshipRepository extends CrudRepository<Championship, Long> {
}
