package com.softserveinc.itacademy.bikechampionship.server.converter;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryResponse;
import org.springframework.core.convert.converter.Converter;

public class CategoryToCategoryResponseConverter implements Converter<Category, CategoryResponse> {
    @Override
    public CategoryResponse convert(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}