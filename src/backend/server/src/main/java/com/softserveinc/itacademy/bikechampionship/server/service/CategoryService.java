package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category createCategory(Category newCategory);

    Category editCategory(Category updatedCategory);

    Category getCategory(Long categoryId);
}
