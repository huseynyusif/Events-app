package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.ImageEntity;
import az.example.eventsapp.entity.ReviewEntity;
import az.example.eventsapp.entity.TicketEntity;
import az.example.eventsapp.request.EventRequest;
import az.example.eventsapp.response.EventCardResponse;
import az.example.eventsapp.response.EventResponse;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mappings({
            @Mapping(source = "event.images", target = "images", qualifiedByName = "mapImages"),
            @Mapping(source = "event.category.name", target = "category.name", qualifiedByName = "mapToString"),
            @Mapping(source = "event.category.description", target = "category.description", qualifiedByName = "mapToString"),
            @Mapping(target = "averageRating", source = "reviews", qualifiedByName = "calculateAverageRating")
    })
    EventResponse toEventResponse(EventEntity event, Set<ReviewEntity> reviews);

    EventEntity toEventEntity(EventRequest eventRequest);

    @Mapping(target = "price", source = "tickets", qualifiedByName = "mapPrice")
    @Mapping(target = "imageUrl", source = "images", qualifiedByName = "mapImageUrl")
    EventCardResponse toEventCardResponse(EventEntity event);

    @Named("mapImages")
    default List<String> mapImages(List<ImageEntity> images) {
        return images.stream()
                .map(ImageEntity::getUrl)
                .collect(Collectors.toList());
    }

    @Named("mapPrice")
    default Double mapPrice(List<TicketEntity> tickets) {
        return tickets.stream()
                .map(TicketEntity::getPrice)
                .filter(price -> price != null) // Ensuring there are no null prices
                .min(Double::compare)
                .orElse(0.0);
    }

    @Named("mapImageUrl")
    default String mapImageUrl(List<ImageEntity> images) {
        return images.stream()
                .map(ImageEntity::getUrl)
                .findFirst()
                .orElse("/default/image/path.jpg");
    }

    @Named("calculateAverageRating")
    default Double calculateAverageRating(Set<ReviewEntity> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(ReviewEntity::getStarRating)
                .average()
                .orElse(0.0);
    }

    @Named("mapToString")
    default String mapToString(Map<String, String> value) {
        return value != null ? value.get("en") : null;
    }
}
