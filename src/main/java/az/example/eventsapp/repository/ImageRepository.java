package az.example.eventsapp.repository;

import az.example.eventsapp.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity,Long> {
    List<ImageEntity> findByEventId(Long eventId);
}
