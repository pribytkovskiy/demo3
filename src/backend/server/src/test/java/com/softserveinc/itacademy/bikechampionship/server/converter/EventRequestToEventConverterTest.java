package com.softserveinc.itacademy.bikechampionship.server.converter;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryRequest;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class EventRequestToEventConverterTest {

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private static EventRequestToEventConverter eventRequestToEventConverter;

    private EventRequest eventRequest;
    private Event eventExpected;
    private Event eventActual;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        eventRequest = new EventRequest();
        eventExpected = new Event();
        eventRequest.setCategories(new ArrayList<>());
    }
    
    @Test
    public void testConvertCategoriesList () {
        eventActual = new Event();
        
        List<CategoryRequest> categoryRequests = new ArrayList<>();
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("111");
        categoryRequest.setId(1L);
        categoryRequests.add(categoryRequest);
        
        eventRequest.setCategories(categoryRequests);
        
        List<Category> categories = new ArrayList<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("222");
        categories.add(category);
        
        eventExpected.setCategories(categories);
        
        when(conversionService.convert(any(EventRequest.class), eq(Event.class))).thenReturn(eventExpected);
        
        
        eventActual = conversionService.convert(eventRequest, Event.class);
        
        Assert.assertEquals(eventExpected.getCategories(), eventActual.getCategories());
    }

    @Test
    public void testConvertDate() {
        //given
        String str = "2018-09-05 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        eventRequest.setDateTime(dateTime);
        eventExpected.setDateTime(dateTime);

        //when
//        eventActual = eventRequestToEventConverter.convert(eventRequest);
        when(conversionService.convert(any(EventRequest.class), eq(Event.class))).thenReturn(eventExpected);
    
        eventActual = conversionService.convert(eventRequest, Event.class);

        //then
        Assert.assertEquals(eventExpected.getDateTime(), eventActual.getDateTime());
    }

    @Test
    public void testConvertName() {
        //given
        eventRequest.setName("event");
        eventExpected.setName("event");

        //when
        eventActual = eventRequestToEventConverter.convert(eventRequest);

        //then
        Assert.assertEquals(eventExpected.getName(), eventActual.getName());
    }

    @Test
    public void testConvertDescription() {
        //given
        eventRequest.setDescription("description");
        eventExpected.setDescription("description");

        //when
        eventActual = eventRequestToEventConverter.convert(eventRequest);

        //then
        Assert.assertEquals(eventExpected.getDescription(), eventActual.getDescription());
    }

    @Test
    public void testConvertId() {

        //given
        eventRequest.setId(1L);
        eventExpected.setId(1L);

        //when
        eventActual = eventRequestToEventConverter.convert(eventRequest);

        //then
        Assert.assertEquals(eventExpected.getId(), eventActual.getId());
    }

    @Test
    public void testIfEventNameIsNull() {
        Assert.assertNull(eventRequest.getName());
    }
}