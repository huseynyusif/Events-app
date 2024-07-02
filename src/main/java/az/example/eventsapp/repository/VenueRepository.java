package az.example.eventsapp.repository;

import az.example.eventsapp.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity,Long> {
    VenueEntity findByName(String name);
}
