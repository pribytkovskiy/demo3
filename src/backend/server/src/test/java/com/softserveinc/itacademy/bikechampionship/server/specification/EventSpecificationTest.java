package com.softserveinc.itacademy.bikechampionship.server.specification;

import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class EventSpecificationTest {
    @Mock
    private Root<Event> root;
    @Mock
    private CriteriaQuery<?> query;
    @Mock
    private CriteriaBuilder builder;

    @Test
    public void toPredicateShouldReturnNullWhenEventSearchRequestIsNull() {
        //given
        EventSpecification eventSpecification = new EventSpecification(null);
        //when
        Predicate result = eventSpecification.toPredicate(root, query, builder);
        //then
        assertNull(result);
    }
}