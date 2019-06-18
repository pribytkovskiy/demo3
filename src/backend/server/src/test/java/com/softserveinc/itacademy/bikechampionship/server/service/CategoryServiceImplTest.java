package com.softserveinc.itacademy.bikechampionship.server.service;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.commons.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    public void whenGetAllCategoriesInvokedAndZeroCategoriesFoundThenEmptyListIsReturned() {
        // when
        when(categoryRepository.findAll())
            .thenReturn(Collections.emptyList());

        List<Category> actual = categoryService.getAllCategories();

        // then
        verify(categoryRepository, times(1)).findAll();
        assertNotNull(actual);
    }

    @Test
    public void whenGetAllCategoriesInvokedAndThreeCategoriesFoundThenListOfThreeCategoriesIsReturned() {
        // given
        int expected = 3;

        // when
        when(categoryRepository.findAll())
            .thenReturn(Collections.nCopies(3, null));

        int actual = categoryService.getAllCategories().size();

        // then
        verify(categoryRepository, times(1)).findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateCategoriesWhenValidRequestGet() {
        Category expectedCategory = new Category();

        when(categoryRepository.save(any(Category.class))).thenReturn(expectedCategory);

        Category actualCategory = categoryService.createCategory(expectedCategory);

        verify(categoryRepository, times(1)).save(any(Category.class));

        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    public void testCreateCategoryWhenIdNotNullShouldReturnException() {
        expectedException.expect(IllegalArgumentException.class);

        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.save(any(Category.class))).thenReturn(new Category());

        categoryService.createCategory(category);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    public void whenEditCategoryThenReturnCategory() {
        //given
        Category expected = new Category();
        expected.setName("name");

        when(categoryRepository.save(any(Category.class))).thenReturn(expected);

        //when
        Category actual = categoryService.editCategory(expected);

        //then
        assertEquals(expected, actual);

    }
}
