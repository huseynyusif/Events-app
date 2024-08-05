package az.example.eventsapp.mapper;

import az.example.eventsapp.entity.CategoryEntity;
import az.example.eventsapp.request.CategoryRequest;
import az.example.eventsapp.response.CategoryResponse;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-31T01:41:53+0400",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryEntity toEntity(CategoryRequest categoryRequest) {
        if ( categoryRequest == null ) {
            return null;
        }

        CategoryEntity.CategoryEntityBuilder categoryEntity = CategoryEntity.builder();

        categoryEntity.name( CategoryMapper.stringToMap( categoryRequest.getName() ) );
        categoryEntity.description( CategoryMapper.stringToMap( categoryRequest.getDescription() ) );

        return categoryEntity.build();
    }

    @Override
    public CategoryResponse toResponse(CategoryEntity categoryEntity) {
        if ( categoryEntity == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.name( CategoryMapper.mapToString( categoryEntity.getName() ) );
        categoryResponse.description( CategoryMapper.mapToString( categoryEntity.getDescription() ) );
        categoryResponse.id( categoryEntity.getId() );

        return categoryResponse.build();
    }

    @Override
    public void updateCategoryFromDto(CategoryRequest categoryRequest, CategoryEntity category) {
        if ( categoryRequest == null ) {
            return;
        }

        if ( category.getName() != null ) {
            Map<String, String> map = CategoryMapper.stringToMap( categoryRequest.getName() );
            if ( map != null ) {
                category.getName().clear();
                category.getName().putAll( map );
            }
        }
        else {
            Map<String, String> map = CategoryMapper.stringToMap( categoryRequest.getName() );
            if ( map != null ) {
                category.setName( map );
            }
        }
        if ( category.getDescription() != null ) {
            Map<String, String> map1 = CategoryMapper.stringToMap( categoryRequest.getDescription() );
            if ( map1 != null ) {
                category.getDescription().clear();
                category.getDescription().putAll( map1 );
            }
        }
        else {
            Map<String, String> map1 = CategoryMapper.stringToMap( categoryRequest.getDescription() );
            if ( map1 != null ) {
                category.setDescription( map1 );
            }
        }
    }
}
