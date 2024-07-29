package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.request.UserRequest;
import az.example.eventsapp.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "role", constant = "ATTENDEE"),
            @Mapping(target = "reviews", ignore = true),
            @Mapping(target = "payments", ignore = true),
            @Mapping(target = "favoriteEvents", ignore = true),
            @Mapping(target = "organizedEvents", ignore = true)
    })
    UserEntity toUserEntity(UserRequest userRequest);

    UserResponse toUserResponse(UserEntity userEntity);
}
