package com.softserveinc.itacademy.bikechampionship.server.controller;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryResponse;
import com.softserveinc.itacademy.bikechampionship.server.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@Api(value = "/api/categories")
public class CategoryController {
    private ConversionService conversionService;
    private CategoryService categoryService;

    public CategoryController(ConversionService conversionService, CategoryService categoryService) {
        this.conversionService = conversionService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @ApiOperation(value = "to retrieve all categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories()
            .stream()
            .map(c -> conversionService.convert(c, CategoryResponse.class))
            .collect(Collectors.toList()));
    }

    @PostMapping
    @ApiOperation(value = "to create new category")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Category newCategory = conversionService.convert(categoryRequest, Category.class);
        newCategory = categoryService.createCategory(newCategory);
        CategoryResponse newCategoryResponse = conversionService.convert(newCategory, CategoryResponse.class);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/categories")
            .buildAndExpand().toUri();
        return ResponseEntity.created(location).body(newCategoryResponse);
    }

    @ApiOperation(value = "updating category's name")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CategoryResponse> editCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Category updatingCategory = conversionService.convert(categoryRequest, Category.class);
        updatingCategory = categoryService.editCategory(updatingCategory);
        CategoryResponse updatingCategoryResponse = conversionService.convert(updatingCategory, CategoryResponse.class);
        return ResponseEntity.ok(updatingCategoryResponse);
    }

    @ApiOperation(value = "getting a category by id")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        Category category = categoryService.getCategory(categoryId);
        CategoryResponse categoryResponse = conversionService.convert(category, CategoryResponse.class);
        return ResponseEntity.ok(categoryResponse);
    }
}