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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public ReviewResponse createReview(ReviewRequest reviewRequest, String username){
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        reviewRepository.findByEventIdAndUserId(reviewRequest.getEventId(), user.getId())
                .ifPresent(review -> {
                    throw new ReviewAlreadyExistsException();
                });

        EventEntity event = eventRepository.findById(reviewRequest.getEventId())
                .orElseThrow(EventNotFoundException::new);

        ReviewEntity reviewEntity = reviewMapper.mapToReviewEntity(reviewRequest);
        reviewEntity.setEvent(event);
        reviewEntity.setUser(user);

        reviewRepository.save(reviewEntity);

        return reviewMapper.mapToReviewResponse(reviewEntity);
    }

    public List<ReviewResponse> getReviewsByEvent(Long eventId) {
        Set<ReviewEntity> reviews = reviewRepository.findByEventId(eventId);
        return reviews.stream()
                .map(reviewMapper::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    public void deleteReviewByAdmin(Long reviewId) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        reviewRepository.delete(reviewEntity);
    }

    public void deleteReviewByUser(Long reviewId, String username) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        if (!reviewEntity.getUser().getUsername().equals(username)) {
            throw new UnauthorizedActionException();
        }

        reviewRepository.delete(reviewEntity);
    }
}
