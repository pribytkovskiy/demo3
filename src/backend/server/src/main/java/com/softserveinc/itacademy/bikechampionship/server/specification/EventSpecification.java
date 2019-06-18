package com.softserveinc.itacademy.bikechampionship.server.specification;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category_;
import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.commons.model.Event_;
import com.softserveinc.itacademy.bikechampionship.commons.model.Participant_;
import com.softserveinc.itacademy.bikechampionship.server.payload.event.EventSearchRequest;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

@EqualsAndHashCode
public class EventSpecification implements Specification<Event> {
    private final EventSearchRequest searchRequest;

    public EventSpecification(EventSearchRequest searchRequest) {
        this.searchRequest = searchRequest;
    }

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (searchRequest != null) {
            Stream.Builder<Predicate> streamBuilder = Stream.builder();

            if (!StringUtils.isEmpty(searchRequest.getName())) {
                streamBuilder.add(builder.equal(root.get(Event_.name), searchRequest.getName()));
            }
            if (searchRequest.getDateTime() != null) {
                streamBuilder.add(builder.greaterThanOrEqualTo(root.get(Event_.dateTime), searchRequest.getDateTime()));
            }
            if (!StringUtils.isEmpty(searchRequest.getDescription())) {
                streamBuilder.add(builder.equal(root.get(Event_.description), searchRequest.getDescription()));
            }
            if (searchRequest.getChampionshipId() != null) {
                streamBuilder.add(builder.equal(root.get(Event_.championship), searchRequest.getChampionshipId()));
            }
            if (searchRequest.getCategoryIds() != null) {
                streamBuilder.add(root.join(Event_.categories).get(Category_.id).in(searchRequest.getCategoryIds()));
            }
            if (searchRequest.getManagerId() != null) {
                streamBuilder.add(builder.equal(root.get(Event_.manager), searchRequest.getManagerId()));
            }
            if (searchRequest.getParticipantIds() != null) {
                streamBuilder.add(root.join(Event_.participants).get(Participant_.id).in(searchRequest.getParticipantIds()));
            }
            return builder.and(streamBuilder.build().toArray(Predicate[]::new));
        }
        return null;
    }
}
