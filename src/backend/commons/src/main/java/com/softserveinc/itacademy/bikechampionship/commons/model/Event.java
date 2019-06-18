package com.softserveinc.itacademy.bikechampionship.commons.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDateTime dateTime;

    @Lob
    private String description;

    @ManyToOne
    private Championship championship;

    @ManyToMany
    private List<Category> categories;

    @ManyToOne
    private User manager;

    @OneToMany(mappedBy = "event")
    private List<Participant> participants;
}
