package az.example.eventsapp.repository;

import az.example.eventsapp.entity.ReviewEntity;
import az.example.eventsapp.response.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
    Optional<ReviewEntity> findByEventIdAndUserId(Long eventId, Long userId);

    Set<ReviewEntity> findByEventId(Long eventId);

    @Query("SELECT AVG(r.starRating) FROM ReviewEntity r WHERE r.event.id = :eventId")
    Double findAverageRatingByEventId(@Param("eventId") Long eventId);
}
