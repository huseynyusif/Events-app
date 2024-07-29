package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.ReviewEntity;
import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.request.ReviewRequest;
import az.example.eventsapp.response.ReviewResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-29T01:52:06+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewResponse mapToReviewResponse(ReviewEntity reviewEntity) {
        if ( reviewEntity == null ) {
            return null;
        }

        ReviewResponse.ReviewResponseBuilder reviewResponse = ReviewResponse.builder();

        reviewResponse.eventId( reviewEntityEventId( reviewEntity ) );
        reviewResponse.userName( reviewEntityUserUsername( reviewEntity ) );
        reviewResponse.id( reviewEntity.getId() );
        reviewResponse.comment( reviewEntity.getComment() );
        if ( reviewEntity.getStarRating() != null ) {
            reviewResponse.starRating( reviewEntity.getStarRating() );
        }

        return reviewResponse.build();
    }

    @Override
    public ReviewEntity mapToReviewEntity(ReviewRequest reviewRequest) {
        if ( reviewRequest == null ) {
            return null;
        }

        ReviewEntity reviewEntity = new ReviewEntity();

        reviewEntity.setComment( reviewRequest.getComment() );
        reviewEntity.setStarRating( reviewRequest.getStarRating() );

        return reviewEntity;
    }

    private Long reviewEntityEventId(ReviewEntity reviewEntity) {
        if ( reviewEntity == null ) {
            return null;
        }
        EventEntity event = reviewEntity.getEvent();
        if ( event == null ) {
            return null;
        }
        Long id = event.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String reviewEntityUserUsername(ReviewEntity reviewEntity) {
        if ( reviewEntity == null ) {
            return null;
        }
        UserEntity user = reviewEntity.getUser();
        if ( user == null ) {
            return null;
        }
        String username = user.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
