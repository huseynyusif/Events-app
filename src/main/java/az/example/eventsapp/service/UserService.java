package az.example.eventsapp.service;

import az.example.eventsapp.entity.OTPEntity;
import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.enums.Role;
import az.example.eventsapp.enums.UserStatus;
import az.example.eventsapp.exception.*;
import az.example.eventsapp.mapper.UserMapper;
import az.example.eventsapp.repository.OTPRepository;
import az.example.eventsapp.repository.UserRepository;
import az.example.eventsapp.request.*;
import az.example.eventsapp.response.UserResponse;
import az.example.eventsapp.util.EmailHelper;
import az.example.eventsapp.util.OTPUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailHelper emailService;
    private final OTPRepository otpRepository;

    public UserResponse registerUser(UserRequest userRequest) {
        UserEntity userEntity = userMapper.toUserEntity(userRequest);
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userEntity.setRole(Role.ATTENDEE);
        userEntity.setStatus(UserStatus.ACTIVE);
        UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toUserResponse(savedUser);
    }

    public UserResponse updateUser(UserUpdateRequest userRequest, String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (userRequest.getName() != null) {
            userEntity.setName(userRequest.getName());
        }

        if (userRequest.getSurname() != null) {
            userEntity.setSurname(userRequest.getSurname());
        }

        if (userRequest.getTelephoneNumber() != null) {
            if (userRepository.existsByTelephoneNumber(userRequest.getTelephoneNumber())){
                throw new TelephoneNumberExistsException();
            }
            userEntity.setTelephoneNumber(userRequest.getTelephoneNumber());
        }

        if (userRequest.getEmail() != null) {
            if (userRepository.existsByEmail(userRequest.getEmail())){
                throw new EmailExistsException();
            }
            userEntity.setEmail(userRequest.getEmail());
        }

        if (userRequest.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        UserEntity updateUser = userRepository.save(userEntity);
        return userMapper.toUserResponse(updateUser);
    }

    public void deleteUser(UserDeleteRequest userDeleteRequest, String name,
                           HttpServletRequest request, HttpServletResponse response) {
        UserEntity userEntity = userRepository.findByUsername(name)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(userDeleteRequest.getPassword(), userEntity.getPassword())){
            throw new IncorrectPasswordException();
        }

        userEntity.setStatus(UserStatus.INACTIVE);
        userRepository.save(userEntity);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    public void generateAndSendOtp(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        String otp = OTPUtil.generateOTP();

        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setUsername(username);
        otpEntity.setOtp(otp);
        otpEntity.setExpirationTime(LocalDateTime.now().plusMinutes(15)); // OTP valid for 15 minutes
        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(userEntity.getEmail(), otp);
    }

    public void verifyOtp(OTPVerificationRequest otpVerificationRequest) {
        OTPEntity otpEntity = otpRepository.findByUsernameAndOtp(otpVerificationRequest.getUsername(), otpVerificationRequest.getOtp())
                .orElseThrow(InvalidOTPException::new);

        if (otpEntity.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new OTPExpiredException();
        }

        otpEntity.setVerified(true);
        otpRepository.save(otpEntity);
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        UserEntity userEntity = userRepository.findByUsername(passwordResetRequest.getUsername())
                .orElseThrow(UserNotFoundException::new);

        OTPEntity otpEntity = otpRepository.findByUsername(passwordResetRequest.getUsername())
                .orElseThrow(OTPNotVerifiedException::new);

        if (!otpEntity.isVerified()) {
            throw new OTPNotVerifiedException();
        }

        userEntity.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        userRepository.save(userEntity);

        // optionally
        otpRepository.delete(otpEntity);
    }
}
