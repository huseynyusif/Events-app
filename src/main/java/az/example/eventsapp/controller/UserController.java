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

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.registerUser(userRequest);
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
//        }
//
//        String jwtToken = jwtUtil.generateToken(authenticationRequest.getUsername());
//        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
//    }

    @PostMapping("/login")
    public String login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest request) throws Exception {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            return "Login successful";
        } else {
            return "Login failed";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return "Logout successful";
    }

    @PutMapping("/update")
    public UserResponse updateUser(@RequestBody @Valid UserUpdateRequest userRequest,
                                                   Authentication authentication) {
        return userService.updateUser(userRequest,authentication.getName());
    }

    @PostMapping("/delete")
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
