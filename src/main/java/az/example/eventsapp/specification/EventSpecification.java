package az.example.eventsapp.specification;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.ReviewEntity;
import az.example.eventsapp.entity.TicketEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EventSpecification {

    public static Specification<EventEntity> buildSpecification(String name, LocalDateTime startDateFrom, LocalDateTime startDateTo, Double minPrice, Double maxPrice,String venueName) {
        Specification<EventEntity> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and(hasName(name));
        }

        if (startDateFrom != null && startDateTo != null) {
            spec = spec.and(hasStartDateBetween(startDateFrom, startDateTo));
        }

        if (minPrice != null && maxPrice != null) {
            spec = spec.and(hasPriceBetween(minPrice, maxPrice));
        }
        if (venueName != null && !venueName.isEmpty()) {
            spec = spec.and(hasVenueName(venueName));
        }

        return spec;
    }

    public static Specification<EventEntity> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<EventEntity> hasStartDateBetween(LocalDateTime startDateFrom, LocalDateTime startDateTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("startDate"), startDateFrom, startDateTo);
    }

    public static Specification<EventEntity> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            // SubQuery to get the minimum price of tickets associated with the event
            Subquery<Double> subquery = query.subquery(Double.class);
            Root<TicketEntity> ticketRoot = subquery.from(TicketEntity.class);
            subquery.select(criteriaBuilder.min(ticketRoot.get("price")))
                    .where(criteriaBuilder.equal(ticketRoot.get("event"), root));

            // Main query condition: check if the event has any ticket price within the specified range
            return criteriaBuilder.between(subquery, minPrice, maxPrice);
        };
    }

    public static Specification<EventEntity> hasVenueName(String venueName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.join("venue").get("name"), "%" + venueName + "%");
    }

    public static Specification<EventEntity> isSortedByStarRating() {
        return (root, query, criteriaBuilder) -> {
            // Check if we're fetching data and not just counting
            if (query.getResultType() != Long.class) {
                // Join to the reviews collection
                Join<EventEntity, ReviewEntity> reviewJoin = root.join("reviews", JoinType.LEFT);

                // Calculate the average star rating
                Expression<Double> averageRating = criteriaBuilder.avg(reviewJoin.get("starRating"));

                // Group by event to ensure correct averaging
                query.groupBy(root.get("id"));

                // Order by average star rating descending
                query.orderBy(criteriaBuilder.desc(averageRating));
            }

            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<EventEntity> isLatestEvents() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createdDate")));
            return query.getRestriction();
        };
    }
}
