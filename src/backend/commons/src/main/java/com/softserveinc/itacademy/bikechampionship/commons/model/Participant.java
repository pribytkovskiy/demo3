package com.softserveinc.itacademy.bikechampionship.commons.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"team", "lap", "category"})
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private Integer competitionNumber;

    @ManyToOne
    private User user;

    @ManyToOne
    private Team team;

    @OneToMany
    private List<Lap> lap;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Category category;
}
