package com.softserveinc.itacademy.bikechampionship.server.bootstrap;

import com.softserveinc.itacademy.bikechampionship.commons.model.Category;
import com.softserveinc.itacademy.bikechampionship.commons.model.Event;
import com.softserveinc.itacademy.bikechampionship.commons.model.Participant;
import com.softserveinc.itacademy.bikechampionship.commons.model.Role;
import com.softserveinc.itacademy.bikechampionship.commons.model.RoleName;
import com.softserveinc.itacademy.bikechampionship.commons.model.User;
import com.softserveinc.itacademy.bikechampionship.commons.repository.CategoryRepository;
import com.softserveinc.itacademy.bikechampionship.commons.repository.EventRepository;
import com.softserveinc.itacademy.bikechampionship.commons.repository.ParticipantRepository;
import com.softserveinc.itacademy.bikechampionship.commons.repository.RoleRepository;
import com.softserveinc.itacademy.bikechampionship.commons.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class Bootstrap implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private CategoryRepository categoryRepository;
    private EventRepository eventRepository;
    private ParticipantRepository participantRepository;

    public Bootstrap(RoleRepository roleRepository,
                     UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     CategoryRepository categoryRepository,
                     EventRepository eventRepository,
                     ParticipantRepository participantRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
    }

    @Override
    public void run(String... args) {
        // BOOTSTRAP ROLES
        Role roleAdmin = saveRole(RoleName.ROLE_ADMIN);
        Role roleManager = saveRole(RoleName.ROLE_MANAGER);
        Role roleUser = saveRole(RoleName.ROLE_USER);
        // BOOTSTRAP USERS
        User admin = saveUser("admin@admin.com",
                "admin",
                "Admin",
                "Adminovich",
                "+380991234567",
                roleAdmin, roleManager, roleUser);
        User manager1 = saveUser("manager1@manager1.com",
                "manager1",
                "Manager",
                "One",
                "+380991112233",
                roleManager, roleUser);
        User manager2 = saveUser("manager2@manager2.com",
                "manager2",
                "Manager",
                "Two",
                "+380661112233",
                roleManager, roleUser);
        User user1 = saveUser("user1@user1.com",
                "user1",
                "User",
                "One",
                "+380881212123",
                roleUser);
        User user2 = saveUser("user2@user2.com",
                "user2",
                "User",
                "Two",
                "+380991212123",
                roleUser);
        User user3 = saveUser("user3@user3.com",
                "user3",
                "User",
                "Three",
                "+380993212121",
                roleUser);
        // BOOTSTRAP CATEGORIES
        Category categoryXC = saveCategory("XC");
        Category categoryRoad = saveCategory("Road");
        Category categoryDownhill = saveCategory("Donwhill");
        Category categoryFreeride = saveCategory("Freeride");
        Category categoryFreestyle = saveCategory("Freestyle");
        // BOOTSTRAP EVENT
        Event event1 = saveEvent("Dnipro Open 2018",
                "First Dnipro Open race by Sid Meyer.\nLocation: Pridnik.",
                LocalDateTime.now().plusMinutes(5),
                admin,
                categoryDownhill, categoryFreeride, categoryFreestyle, categoryRoad, categoryXC);
        Event event2 = saveEvent("Mariupol Autumn Race 2018",
                "Mariupol Autumn Race 2018 race by MariupolRaceTeam.\nLocation: Mariupol.",
                LocalDateTime.now().plusHours(1),
                manager1,
                categoryDownhill, categoryFreeride);
        Event event3 = saveEvent("Dnipro Winter Cup 2018/2019",
                "First Dnipro Winter race by DniproBike.com.ua.\nLocation: Parus-2.",
                LocalDateTime.now().plusWeeks(1),
                manager2,
                categoryFreeride, categoryFreestyle);
        Event event4 = saveEvent("Dnipro Winter Cup 2018/2019",
                "Second Dnipro Winter race by DniproBike.com.ua.\nLocation: Pobeda-4.",
                LocalDateTime.now().plusMonths(1),
                manager2,
                categoryXC);
        Event event5 = saveEvent("Dnipro Winter Cup 2019/2020",
                "Third Dnipro Winter race by DniproBike.com.ua.\nLocation: Solnechnii-2.",
                LocalDateTime.now().plusYears(1),
                manager2,
                categoryDownhill);
        Event event6 = saveEvent("Dnipro ATB CUP 2019",
                "First Dnipro ATB CUP race by ATB.\nLocation: Tunneljnaya balka.",
                LocalDateTime.now().plusMinutes(10),
                manager1,
                categoryRoad, categoryXC, categoryFreestyle);
        Event event7 = saveEvent("Dnipro ATB CUP 2019",
                "Second Dnipro ATB CUP race by ATB.\nLocation: Tunneljnaya balka.",
                LocalDateTime.now().plusWeeks(3),
                admin,
                categoryRoad, categoryXC);
        //BOOTSTRAP PARTICIPANTS
        saveParticipant("Admin", "Adminych", event1, 1, categoryXC, admin);
        saveParticipant("Manager", "One", event1, 777, categoryDownhill, admin);
        saveParticipant("Manager", "Two", event1, 101, categoryFreeride, admin);
        saveParticipant("Registered", "User1", event1, 111, categoryRoad, user1);
        saveParticipant("Registered", "User2", event1, 222, categoryXC, user2);
        saveParticipant("Unregistered", "Participant", event1, 123, categoryRoad, null);
        saveParticipant("Unregistered", "Participant2", event1, 321, categoryFreestyle, null);

        saveParticipant("Registered", "User2", event3, 222, categoryRoad, user2);
        saveParticipant("Registered", "User3", event3, 333, categoryRoad, user3);
        saveParticipant("Unregistered", "Participant", event2, 123, categoryDownhill, null);
        saveParticipant("Unregistered", "Participant2", event2, 321, categoryFreestyle, null);
    }

    private Role saveRole(RoleName roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    private User saveUser(String email, String password, String firstName, String lastName, String phone, Role... roles) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phone);
        user.setRoles(Arrays.stream(roles).collect(Collectors.toSet()));
        return userRepository.save(user);
    }

    private Category saveCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    private Event saveEvent(String name, String description, LocalDateTime dateTime, User manager, Category... categories) {
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        event.setCategories(Arrays.asList(categories));
        event.setManager(manager);
        event.setDateTime(dateTime);
        return eventRepository.save(event);
    }

    private Participant saveParticipant(String firstName, String lastName, Event event, Integer competitionNumber, Category category, User user) {
        Participant participant = new Participant();
        participant.setFirstName(firstName);
        participant.setLastName(lastName);
        participant.setEvent(event);
        participant.setCompetitionNumber(competitionNumber);
        participant.setCategory(category);
        participant.setUser(user);
        return participantRepository.save(participant);
    }
}
