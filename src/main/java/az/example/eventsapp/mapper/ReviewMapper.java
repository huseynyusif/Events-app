package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.ReviewEntity;
import az.example.eventsapp.request.ReviewRequest;
import az.example.eventsapp.response.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "userName", source = "user.username")
    ReviewResponse mapToReviewResponse(ReviewEntity reviewEntity);
    ReviewEntity mapToReviewEntity(ReviewRequest reviewRequest);
}
