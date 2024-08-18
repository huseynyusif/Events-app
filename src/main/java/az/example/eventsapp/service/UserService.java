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
import az.example.eventsapp.response.AuthenticationResponse;
import az.example.eventsapp.response.UserResponse;
import az.example.eventsapp.util.EmailHelper;
import az.example.eventsapp.util.JwtUtil;
import az.example.eventsapp.util.OTPUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailHelper emailService;
    private final OTPRepository otpRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserResponse registerUser(UserRequest userRequest) {
        log.info("Registering user with username: {}", userRequest.getUsername());
        UserEntity userEntity = userMapper.toUserEntity(userRequest);
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userEntity.setRole(Role.ATTENDEE);
        userEntity.setStatus(UserStatus.ACTIVE);
        UserEntity savedUser = userRepository.save(userEntity);
        log.info("User registered successfully with ID: {}", savedUser.getId());
        return userMapper.toUserResponse(savedUser);
    }

    public AuthenticationResponse authenticateAndGenerateJwtToken(String username, String password) {
        log.info("Authenticating user with username: {}", username);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for username: {}", username);
            throw new UnauthorizedException();
        }

        String jwtToken = jwtUtil.generateToken(username);
        log.info("Generated JWT token for username: {}", username);
        return new AuthenticationResponse(jwtToken);
    }

    public String login(String username, String password, HttpServletRequest request) throws Exception {
        log.info("Attempting to log in user with username: {}", username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            log.info("User logged in successfully: {}", username);
            return "Login successful";
        } else {
            log.warn("Login failed for username: {}", username);
            return "Login failed";
        }
    }

    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Logging out user");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        log.info("User logged out successfully");
        return "Logout successful";
    }

    public UserResponse updateUser(UserUpdateRequest userRequest, String username) {
        log.info("Updating user with username: {}", username);
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UserNotFoundException();
                });

        if (userRequest.getName() != null) {
            userEntity.setName(userRequest.getName());
        }

        if (userRequest.getSurname() != null) {
            userEntity.setSurname(userRequest.getSurname());
        }

        if (userRequest.getTelephoneNumber() != null) {
            if (userRepository.existsByTelephoneNumber(userRequest.getTelephoneNumber())){
                log.warn("Telephone number already exists: {}", userRequest.getTelephoneNumber());
                throw new TelephoneNumberExistsException();
            }
            userEntity.setTelephoneNumber(userRequest.getTelephoneNumber());
        }

        if (userRequest.getEmail() != null) {
            if (userRepository.existsByEmail(userRequest.getEmail())){
                log.warn("CustomEmail already exists: {}", userRequest.getEmail());
                throw new EmailExistsException();
            }
            userEntity.setEmail(userRequest.getEmail());
        }

        if (userRequest.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        UserEntity updateUser = userRepository.save(userEntity);
        log.info("User updated successfully: {}", username);
        return userMapper.toUserResponse(updateUser);
    }

    public void deleteUser(UserDeleteRequest userDeleteRequest, String name,
                           HttpServletRequest request, HttpServletResponse response) {
        log.info("Deleting user with username: {}", name);
        UserEntity userEntity = userRepository.findByUsername(name)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", name);
                    return new UserNotFoundException();
                });

        if (!passwordEncoder.matches(userDeleteRequest.getPassword(), userEntity.getPassword())){
            log.warn("Incorrect password provided for user: {}", name);
            throw new IncorrectPasswordException();
        }

        userEntity.setStatus(UserStatus.INACTIVE);
        userRepository.save(userEntity);
        log.info("User set to inactive: {}", name);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            log.info("User logged out after account deletion: {}", name);
        }
    }

    public void generateAndSendOtp(String username) {
        log.info("Generating OTP for username: {}", username);
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UserNotFoundException();
                });

        String otp = OTPUtil.generateOTP();

        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setUsername(username);
        otpEntity.setOtp(otp);
        otpEntity.setExpirationTime(LocalDateTime.now().plusMinutes(15)); // OTP valid for 15 minutes
        otpRepository.save(otpEntity);
        log.info("OTP generated and saved for username: {}", username);

        emailService.sendOtpEmail(userEntity.getEmail(), otp);
        log.info("OTP sent to email: {}", userEntity.getEmail());
    }

    public void verifyOtp(OTPVerificationRequest otpVerificationRequest) {
        log.info("Verifying OTP for username: {}", otpVerificationRequest.getUsername());
        OTPEntity otpEntity = otpRepository.findByUsernameAndOtp(otpVerificationRequest.getUsername(), otpVerificationRequest.getOtp())
                .orElseThrow(() -> {
                    log.error("Invalid OTP for username: {}", otpVerificationRequest.getUsername());
                    return new InvalidOTPException();
                });

        if (otpEntity.getExpirationTime().isBefore(LocalDateTime.now())) {
            log.warn("OTP expired for username: {}", otpVerificationRequest.getUsername());
            throw new OTPExpiredException();
        }

        otpEntity.setVerified(true);
        otpRepository.save(otpEntity);
        log.info("OTP verified successfully for username: {}", otpVerificationRequest.getUsername());
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        log.info("Resetting password for username: {}", passwordResetRequest.getUsername());
        UserEntity userEntity = userRepository.findByUsername(passwordResetRequest.getUsername())
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", passwordResetRequest.getUsername());
                    return new UserNotFoundException();
                });

        OTPEntity otpEntity = otpRepository.findByUsername(passwordResetRequest.getUsername())
                .orElseThrow(() -> {
                    log.error("OTP not verified for username: {}", passwordResetRequest.getUsername());
                    return new OTPNotVerifiedException();
                });

        if (!otpEntity.isVerified()) {
            log.warn("Attempt to reset password with unverified OTP for username: {}", passwordResetRequest.getUsername());
            throw new OTPNotVerifiedException();
        }

        userEntity.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        userRepository.save(userEntity);
        log.info("Password reset successfully for username: {}", passwordResetRequest.getUsername());

        // optionally
        otpRepository.delete(otpEntity);
        log.info("OTP deleted after password reset for username: {}", passwordResetRequest.getUsername());
    }
}
