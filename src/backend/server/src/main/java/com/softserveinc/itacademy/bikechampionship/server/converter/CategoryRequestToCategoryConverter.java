package com.softserveinc.itacademy.bikechampionship.server.converter;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryRequest;
import org.springframework.core.convert.converter.Converter;

public class CategoryRequestToCategoryConverter implements Converter<CategoryRequest, Category> {

    @Override
    public Category convert(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setId(categoryRequest.getId());
        category.setName(categoryRequest.getName());
        return category;
    }
}
