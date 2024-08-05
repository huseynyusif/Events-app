package az.example.eventsapp.controller;

import az.example.eventsapp.request.*;
import az.example.eventsapp.response.AuthenticationResponse;
import az.example.eventsapp.response.UserResponse;
import az.example.eventsapp.service.UserService;
import az.example.eventsapp.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.registerUser(userRequest);
    }

    @PostMapping("/login/jwt")
    public AuthenticationResponse loginJwt(@RequestBody AuthenticationRequest authenticationRequest) {
        return userService.authenticateAndGenerateJwtToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest request) throws Exception {
        return userService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword(), request);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        return userService.logout(request,response);
    }

    @PutMapping("/update")
    public UserResponse updateUser(@RequestBody @Valid UserUpdateRequest userRequest,
                                                   Authentication authentication) {
        return userService.updateUser(userRequest,authentication.getName());
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteUser(@RequestBody @Valid UserDeleteRequest userDeleteRequest,
                                             Authentication authentication,
                                             HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(userDeleteRequest,authentication.getName(),request,response);
        return "User account deleted successfully";
    }

    @PostMapping("/request-otp")
    public String requestOtp(@RequestBody @Valid OTPRequest otpRequest) {
        userService.generateAndSendOtp(otpRequest.getUsername());
        return "OTP sent to registered email";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody @Valid OTPVerificationRequest otpVerificationRequest) {
        userService.verifyOtp(otpVerificationRequest);
        return "OTP verified";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody @Valid PasswordResetRequest passwordResetRequest) {
        userService.resetPassword(passwordResetRequest);
        return "Password reset successful";
    }
}
