package com.softserveinc.itacademy.bikechampionship.server.converter;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.server.payload.category.CategoryResponse;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventToEventResponseConverterTest {

    @Mock
    ConversionService conversionService;

    @InjectMocks
    private EventToEventResponseConverter converter;

    @Test
    public void converterShouldReturnConvertedEvent() {
        //given
        User manager = new User();
        manager.setId(1L);
        Event event = new Event();
        event.setId(1L);
        event.setName("Super event");
        event.setDateTime(LocalDateTime.MAX);
        event.setDescription("Super description");
        event.setManager(manager);
        event.setCategories(Collections.singletonList(new Category()));

        when(conversionService.convert(any(Category.class), any()))
            .thenReturn(new CategoryResponse(1L, "categoryResponse"));

        //when
        EventResponse eventResponse = converter.convert(event);
        //then
        verify(conversionService, timeout(1)).convert(any(Category.class), any());
        assertThat(eventResponse, hasProperty("id", equalTo(event.getId())));
        assertThat(eventResponse, hasProperty("name", equalTo(event.getName())));
        assertThat(eventResponse, hasProperty("dateTime", equalTo(event.getDateTime())));
        assertThat(eventResponse, hasProperty("description", equalTo(event.getDescription())));
        assertThat(eventResponse, hasProperty("managerId", equalTo(event.getManager().getId())));
        assertThat(eventResponse, hasProperty("categories", hasSize(1)));
        assertNotNull(eventResponse.getCategories().get(0));
    }
}