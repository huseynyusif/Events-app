package az.example.eventsapp.service;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.ReviewEntity;
import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.exception.*;
import az.example.eventsapp.mapper.ReviewMapper;
import az.example.eventsapp.repository.EventRepository;
import az.example.eventsapp.repository.ReviewRepository;
import az.example.eventsapp.repository.UserRepository;
import az.example.eventsapp.request.ReviewRequest;
import az.example.eventsapp.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public ReviewResponse createReview(ReviewRequest reviewRequest, String username){
        log.info("Creating review for event ID: {} by user: {}",reviewRequest.getEventId(),username);

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with Username: {}",username);
                    return new UserNotFoundException();
                });

        reviewRepository.findByEventIdAndUserId(reviewRequest.getEventId(), user.getId())
                .ifPresent(review -> {
                    log.warn("Review already exists for event ID: {} by user ID: {}", reviewRequest.getEventId(), user.getId());
                    throw new ReviewAlreadyExistsException();
                });

        EventEntity event = eventRepository.findById(reviewRequest.getEventId())
                .orElseThrow(() -> {
                    log.error("Event not found with ID: {}", reviewRequest.getEventId());
                    return new EventNotFoundException();
                });

        ReviewEntity reviewEntity = reviewMapper.mapToReviewEntity(reviewRequest);
        reviewEntity.setEvent(event);
        reviewEntity.setUser(user);

        reviewRepository.save(reviewEntity);

        log.info("Review created successfully for event ID: {} by user: {}", reviewRequest.getEventId(), username);
        return reviewMapper.mapToReviewResponse(reviewEntity);
    }

    public List<ReviewResponse> getReviewsByEvent(Long eventId) {
        log.info("Fetching reviews for event ID: {}", eventId);

        Set<ReviewEntity> reviews = reviewRepository.findByEventId(eventId);
        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(reviewMapper::mapToReviewResponse)
                .collect(Collectors.toList());

        log.info("Retrieved {} reviews for event ID: {}", reviewResponses.size(), eventId);
        return reviewResponses;
    }

    public void deleteReviewByAdmin(Long reviewId) {
        log.info("Admin is deleting review with ID: {}", reviewId);

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.error("Review not found with ID: {}", reviewId);
                    return new ReviewNotFoundException();
                });

        reviewRepository.delete(reviewEntity);
        log.info("Review with ID: {} deleted by admin", reviewId);
    }

    public void deleteReviewByUser(Long reviewId, String username) {
        log.info("User: {} is attempting to delete review with ID: {}", username, reviewId);

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.error("Review not found with ID: {}", reviewId);
                    return new ReviewNotFoundException();
                });

        if (!reviewEntity.getUser().getUsername().equals(username)) {
            log.warn("Unauthorized action: User: {} attempted to delete review with ID: {}", username, reviewId);
            throw new UnauthorizedActionException();
        }

        reviewRepository.delete(reviewEntity);
        log.info("Review with ID: {} successfully deleted by user: {}", reviewId, username);
    }
}
