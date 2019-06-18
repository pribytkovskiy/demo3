package com.softserveinc.itacademy.bikechampionship.commons.repository;

import com.softserveinc.itacademy.bikechampionship.commons.model.Role;
import com.softserveinc.itacademy.bikechampionship.commons.model.RoleName;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
