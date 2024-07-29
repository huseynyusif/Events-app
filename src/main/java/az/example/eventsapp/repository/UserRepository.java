package az.example.eventsapp.repository;

import az.example.eventsapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByTelephoneNumber(String telephoneNumber);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
