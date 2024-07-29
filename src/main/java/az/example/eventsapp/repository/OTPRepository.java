package az.example.eventsapp.repository;

import az.example.eventsapp.entity.OTPEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTPEntity,Long> {
    Optional<OTPEntity> findByUsernameAndOtp(String username, String otp);
    Optional<OTPEntity> findByUsername(String username);
}
