package az.example.eventsapp.entity;


import az.example.events.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String telephoneNumber;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "attendee")
    private Set<TicketEntity> tickets;

    @OneToMany(mappedBy = "user")
    private Set<ReviewEntity> reviews;

    @OneToMany(mappedBy = "user")
    private Set<PaymentEntity> payments;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<EventEntity> favoriteEvents;

    @OneToMany(mappedBy = "organizer")
    private Set<EventEntity> organizedEvents;
}
