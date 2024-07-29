package az.example.eventsapp.repository;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity,Long> , JpaSpecificationExecutor<EventEntity>, PagingAndSortingRepository<EventEntity,Long> {
    List<EventEntity> findByStatus(EventStatus status);


    List<EventEntity> findAllByOrderByCreatedDateDesc();

    List<EventEntity> findByCategoryId (Long categoryId);

    List<EventEntity> findByNameContainingIgnoreCase(String name);

    List<EventEntity> findTop10ByNameContainingIgnoreCase(String query);

    List<EventEntity> findByOrganizerUsername(String username);
}
