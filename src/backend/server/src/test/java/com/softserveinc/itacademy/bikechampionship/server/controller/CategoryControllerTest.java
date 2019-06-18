package com.softserveinc.itacademy.bikechampionship.server.controller;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryResponse;
import com.softserveinc.itacademy.bikechampionship.server.service.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CategoryControllerTest extends AbstractRestControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(null)))
            .andExpect(status().isOk());

    }

    @Test
    public void whenCategoryServiceReturnsZeroCategoriesThenEmptyListReturnedAsJsonAndStatusIsOk() throws Exception {
        // when
        when(categoryService.getAllCategories())
            .thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/categories/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        //then
        verify(categoryService, times(1)).getAllCategories();
        verifyNoMoreInteractions(categoryService);
        verifyNoMoreInteractions(conversionService);
    }

    @Test
    public void whenCategoryServiceReturnsThreeCategoriesThenListOfThreeCategoryResponseReturnedAsJson() throws Exception {
        //given
        CategoryResponse cr1 = new CategoryResponse(1L, "first test category");
        CategoryResponse cr2 = new CategoryResponse(2L, "second test category");
        CategoryResponse cr3 = new CategoryResponse(3L, "third test category");

        // when
        when(categoryService.getAllCategories())
            .thenReturn(Collections.nCopies(3, new Category()));
        when(conversionService.convert(any(Category.class), eq(CategoryResponse.class)))
            .thenReturn(cr1)
            .thenReturn(cr2)
            .thenReturn(cr3);
        mockMvc.perform(get("/api/categories/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("first test category")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].name", is("second test category")))
            .andExpect(jsonPath("$[2].id", is(3)))
            .andExpect(jsonPath("$[2].name", is("third test category")));

        //then
        verify(categoryService, times(1)).getAllCategories();
        verifyNoMoreInteractions(categoryService);
        verify(conversionService, times(3)).convert(any(Category.class), eq(CategoryResponse.class));
        verifyNoMoreInteractions(conversionService);
    }

    @Test
    public void testCreateCategories() throws Exception {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setId(1L);
        categoryRequest.setName("1111");

        Category category = new Category();
        category.setId(1L);

        CategoryResponse categoryResponse = new CategoryResponse(1L, "111");

        when(conversionService.convert(any(CategoryRequest.class), eq(Category.class))).thenReturn(category);
        when(categoryService.createCategory(any(Category.class))).thenReturn(category);
        when(conversionService.convert(any(Category.class), eq(CategoryResponse.class))).thenReturn(categoryResponse);

        mockMvc.perform(post("/api/categories").with(user("123").roles("ADMIN"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(categoryRequest)))
            .andExpect(status().isCreated());
    }

    @Test
    public void testEditCategory() throws Exception {
        Category oldCategory = new Category();
        oldCategory.setId(1L);
        oldCategory.setName("old name");

        Category newCategory = new Category();
        newCategory.setId(1L);
        newCategory.setName("new name");

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setId(1L);
        categoryRequest.setName("old category");

        CategoryResponse categoryResponse = new CategoryResponse(1L, "new name");

        when(conversionService.convert(any(CategoryRequest.class), eq(Category.class))).thenReturn(oldCategory);
        when(categoryService.editCategory(any(Category.class))).thenReturn(newCategory);
        when(conversionService.convert(any(Category.class), eq(CategoryResponse.class))).thenReturn(categoryResponse);

        mockMvc.perform(put("/api/categories").with(user("123").roles("ADMIN"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(categoryRequest)))
            .andExpect(status().isOk());

        verify(conversionService, times(1)).convert(any(CategoryRequest.class), eq(Category.class));
        verify(categoryService, times(1)).editCategory(any(Category.class));
        verify(conversionService, times(1)).convert(any(Category.class), eq(CategoryResponse.class));

    }

}