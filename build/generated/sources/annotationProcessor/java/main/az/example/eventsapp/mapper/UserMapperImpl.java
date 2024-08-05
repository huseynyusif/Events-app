package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.enums.Role;
import az.example.eventsapp.request.UserRequest;
import az.example.eventsapp.response.UserResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-01T17:03:15+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toUserEntity(UserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setName( userRequest.getName() );
        userEntity.setSurname( userRequest.getSurname() );
        userEntity.setTelephoneNumber( userRequest.getTelephoneNumber() );
        userEntity.setEmail( userRequest.getEmail() );
        userEntity.setUsername( userRequest.getUsername() );
        userEntity.setPassword( userRequest.getPassword() );

        userEntity.setRole( Role.ATTENDEE );

        return userEntity;
    }

    @Override
    public UserResponse toUserResponse(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( userEntity.getId() );
        userResponse.name( userEntity.getName() );
        userResponse.surname( userEntity.getSurname() );
        userResponse.telephoneNumber( userEntity.getTelephoneNumber() );
        userResponse.email( userEntity.getEmail() );
        userResponse.username( userEntity.getUsername() );

        return userResponse.build();
    }
}
