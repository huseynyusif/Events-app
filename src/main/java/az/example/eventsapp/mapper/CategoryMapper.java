package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.CategoryEntity;
import az.example.eventsapp.request.CategoryRequest;
import az.example.eventsapp.response.CategoryResponse;
import org.mapstruct.*;

import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(source = "name", target = "name",qualifiedByName = "stringToMap")
    @Mapping(source = "description", target = "description",qualifiedByName = "stringToMap")
    CategoryEntity toEntity(CategoryRequest categoryRequest);

    @Mapping(source = "name", target = "name",  qualifiedByName = "mapToString")
    @Mapping(source = "description", target = "description",  qualifiedByName = "mapToString")
    CategoryResponse toResponse(CategoryEntity categoryEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "name", target = "name", qualifiedByName = "stringToMap")
    @Mapping(source = "description", target = "description", qualifiedByName = "stringToMap")
    void updateCategoryFromDto(CategoryRequest categoryRequest, @MappingTarget CategoryEntity category);

    @Named("stringToMap")
    static Map<String, String> stringToMap(String value) {
        return Map.of("en", value);
    }

    @Named("mapToString")
    static String mapToString(Map<String, String> map) {
        return map != null ? map.getOrDefault("en", "default") : "default";
    }
}
