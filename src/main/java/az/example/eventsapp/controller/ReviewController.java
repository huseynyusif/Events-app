package az.example.eventsapp.controller;

import az.example.eventsapp.exception.UnauthorizedException;
import az.example.eventsapp.request.ReviewRequest;
import az.example.eventsapp.response.ReviewResponse;
import az.example.eventsapp.service.ReviewService;
import az.example.eventsapp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ReviewResponse createReview(@RequestBody ReviewRequest reviewRequest,
                                       Authentication authentication
//                                       @RequestHeader(value = "Authorization") String authorizationHeader){
        ){
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            throw new UnauthorizedException();
//        }
//        // Token-i authorizationHeader-dan cixardiriq
//        String token = authorizationHeader.substring("Bearer ".length());
//
//        // Istifadecinin username-ni tokenden aliriq
//        String username = jwtUtil.getUsernameFromToken(token);
//
//        if (username == null) {
//            throw new UnauthorizedException();
//        }

        return reviewService.createReview(reviewRequest,authentication.getName());
    }

    @GetMapping("/event/{eventId}")
    public List<ReviewResponse> getReviewsByEvent(@PathVariable Long eventId){
        return reviewService.getReviewsByEvent(eventId);
    }

    @DeleteMapping("/delete/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteReviewByAdmin(@PathVariable Long reviewId) {
        reviewService.deleteReviewByAdmin(reviewId);
        return "Comment was successfully deleted";
    }

    @DeleteMapping("/delete-user-review/{reviewId}")
    public void deleteReviewByUser(@PathVariable Long reviewId, Authentication authentication) {
        if (authentication == null) {
            throw new UnauthorizedException();
        }

        reviewService.deleteReviewByUser(reviewId, authentication.getName());
    }

}
