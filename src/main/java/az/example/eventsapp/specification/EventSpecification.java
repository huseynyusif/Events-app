package az.example.eventsapp.specification;

import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.ReviewEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventSpecification {

    public static Specification<EventEntity> buildSpecification(String name, LocalDateTime startDateFrom, LocalDateTime startDateTo, BigDecimal minPrice, BigDecimal maxPrice,String venueName) {
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

    public static Specification<EventEntity> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.join("tickets").get("price"), minPrice, maxPrice);
    }

    public static Specification<EventEntity> hasVenueName(String venueName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.join("venue").get("name"), "%" + venueName + "%");
    }

    public static Specification<EventEntity> isSortedByStarRating() {
        return (root, query, criteriaBuilder) -> {
            // join to the reviews collection
            Join<EventEntity, ReviewEntity> reviewJoin = root.join("reviews", JoinType.LEFT);

            query.orderBy(criteriaBuilder.desc(criteriaBuilder.avg(reviewJoin.get("starRating"))));

            return query.getRestriction();
        };
    }

    public static Specification<EventEntity> isLatestEvents() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createdDate")));
            return query.getRestriction();
        };
    }
}
