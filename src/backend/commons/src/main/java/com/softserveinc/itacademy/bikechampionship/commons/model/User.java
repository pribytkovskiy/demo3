package com.softserveinc.itacademy.bikechampionship.commons.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @NaturalId(mutable = true)
    private String email;

    private String phoneNumber;
    private String password;

    @Lob
    private Byte[] avatar;

    @ManyToMany
    private Set<Role> roles = new HashSet<>();

    @OneToMany
    private List<Event> events;

    @OneToMany(mappedBy = "user")
    private List<Participant> participants;
}