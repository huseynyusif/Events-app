package az.example.eventsapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_tickets")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    private TicketEntity ticket;

    private String type; // example: VIP/GA Zona-1

    private Double price;

    private String customerCode;

}