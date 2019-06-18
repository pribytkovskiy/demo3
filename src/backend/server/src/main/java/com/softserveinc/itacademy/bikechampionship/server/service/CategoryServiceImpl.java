package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.exception.ResourceNotFoundException;
import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.commons.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();

        categoryRepository.findAll().forEach(categoryList::add);
        return categoryList;
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if (category.getId() != null && category.getId() != 0) {
            throw new IllegalArgumentException("Cannot create new category with specified Id");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category editCategory(Category updatedCategory) {
        return categoryRepository.save(updatedCategory);
    }

    @Override
    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Event", "id", categoryId));
    }
}
