package az.example.eventsapp.util;

import az.example.eventsapp.entity.CategoryEntity;
import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.entity.VenueEntity;
import az.example.eventsapp.enums.EventStatus;
import az.example.eventsapp.enums.Role;
import az.example.eventsapp.repository.CategoryRepository;
import az.example.eventsapp.repository.EventRepository;
import az.example.eventsapp.repository.UserRepository;
import az.example.eventsapp.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        loadCategories();
        loadVenues();
        loadUsers();
        loadEvents();
    }

    private void loadCategories() {
        CategoryEntity festival = new CategoryEntity();
        festival.setName("Festival");
        festival.setDescription("Music and cultural festivals");

        CategoryEntity holidays = new CategoryEntity();
        holidays.setName("Holidays");
        holidays.setDescription("Holiday celebrations and events");

        CategoryEntity meeting = new CategoryEntity();
        meeting.setName("Meeting");
        meeting.setDescription("Business and social meetings");

        CategoryEntity camping = new CategoryEntity();
        camping.setName("Camping");
        camping.setDescription("Outdoor camping and adventure activities");

        categoryRepository.saveAll(Arrays.asList(festival, holidays, meeting, camping));
    }

    private void loadVenues() {
        VenueEntity venue1 = new VenueEntity();
        venue1.setName("City Park");
        venue1.setAddress("Neftciler ave");
        venue1.setCapacity(5000);

        VenueEntity venue2 = new VenueEntity();
        venue2.setName("Convention Center");
        venue2.setAddress("Nizami ave");
        venue2.setCapacity(2000);

        venueRepository.saveAll(Arrays.asList(venue1, venue2));
    }

    private void loadUsers() {
        UserEntity organizer = new UserEntity();
        organizer.setName("Nikola");
        organizer.setSurname("Tesla");
        organizer.setTelephoneNumber("0505005050");
        organizer.setEmail("nikola@example.com");
        organizer.setUsername("nikola");
        organizer.setPassword("password");
        organizer.setRole(Role.ORGANIZER);

        UserEntity attendee = new UserEntity();
        attendee.setName("Isaac");
        attendee.setSurname("Newton");
        attendee.setTelephoneNumber("0555555555");
        attendee.setEmail("isaac@example.com");
        attendee.setUsername("isaac");
        attendee.setPassword("password");
        attendee.setRole(Role.ATTENDEE);

        userRepository.saveAll(Arrays.asList(organizer, attendee));
    }

    private void loadEvents() {
        UserEntity organizer = userRepository.findByUsername("nikola");
        CategoryEntity festivalCategory = categoryRepository.findByName("Festival");
        VenueEntity venue1 = venueRepository.findByName("City Park");

        EventEntity event1 = new EventEntity();
        event1.setName("Summer Music Festival");
        event1.setDescription("A fun-filled music festival featuring top artists.");
        event1.setStartDate(LocalDateTime.now().plusDays(10));
        event1.setEndDate(LocalDateTime.now().plusDays(12));
        event1.setCategory(festivalCategory);
        event1.setVenue(venue1);
        event1.setOrganizer(organizer);
        event1.setStatus(EventStatus.ACTIVE);

        eventRepository.save(event1);
    }
}
