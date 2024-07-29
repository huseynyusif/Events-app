package az.example.eventsapp.util;

import az.example.eventsapp.entity.CategoryEntity;
import az.example.eventsapp.mapper.CategoryMapper;
import az.example.eventsapp.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LocaleResolverUtil {

    private final CategoryMapper categoryMapper;

//    public CategoryResponse setForLocalCategory(CategoryEntity categoryEntity, Locale locale) {
//        CategoryResponse response = categoryMapper.toResponse(categoryEntity);
//        String language = locale.getLanguage();
//        Map<String, String> nameMap = categoryEntity.getName();
//        Map<String, String> descriptionMap = categoryEntity.getDescription();
//
//        response.setName(nameMap.getOrDefault(language, nameMap.get("en")));
//        response.setDescription(descriptionMap.getOrDefault(language, descriptionMap.get("en")));
//
//        return response;
//    }

    public CategoryResponse setForLocalCategory(CategoryEntity categoryEntity, Locale locale) {
        CategoryResponse response = categoryMapper.toResponse(categoryEntity);
        String language = locale.getLanguage();
        Map<String, String> nameMap = categoryEntity.getName();
        Map<String, String> descriptionMap = categoryEntity.getDescription();

        response.setName(nameMap.getOrDefault(language, nameMap.get("en")));
        response.setDescription(descriptionMap.getOrDefault(language, descriptionMap.get("en")));

        return response;
    }

}
