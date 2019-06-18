package com.softserveinc.itacademy.bikechampionship.commons.repository;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByName(String name);

    Boolean existsByName(String name);
}
