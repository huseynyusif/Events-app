package az.example.eventsapp.repository;

import az.example.eventsapp.entity.UserTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicketEntity,Long> {
}
