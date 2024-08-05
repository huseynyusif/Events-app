package az.example.eventsapp.service;

import az.example.eventsapp.entity.CategoryEntity;
import az.example.eventsapp.exception.CategoryNotFoundException;
import az.example.eventsapp.mapper.CategoryMapper;
import az.example.eventsapp.repository.CategoryRepository;
import az.example.eventsapp.request.CategoryRequest;
import az.example.eventsapp.response.CategoryResponse;
import az.example.eventsapp.util.LocaleResolverUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final LocaleResolverUtil localeResolverUtil;

    public void createCategory(CategoryRequest categoryCreateRequest) {
        log.info("Creating a new category with name: {}", categoryCreateRequest.getName());

        CategoryEntity categoryEntity = categoryMapper.toEntity(categoryCreateRequest);
        categoryRepository.save(categoryEntity);

        log.info("Category created with ID: {}", categoryEntity.getId());
    }

    public List<CategoryResponse> getAllCategory(Locale locale) {
        log.info("Fetching all categories for locale: {}", locale);

        List<CategoryResponse> categories = categoryRepository.findAll()
                .stream()
                .map(categoryEntity -> localeResolverUtil.setForLocalCategory(categoryEntity, locale))
                .toList();

        log.info("Fetched {} categories for locale: {}", categories.size(), locale);
        return categories;
    }

//    public List<CategoryResponse> getAllCategory(String language) {
//    log.info("Fetching all categories for language: {}", language);
//        List<CategoryEntity> categories = categoryRepository.findAll();
//        List<CategoryResponse> categoryResponses = categories.stream()
//                .map(category -> {
//                    CategoryResponse response = categoryMapper.toResponse(category);
//                    localeResolverUtil.setForLocalCategory(response, language);
//                    return response;
//                })
//                .collect(Collectors.toList());
//
//        log.info("Fetched {} categories for language: {}", categoryResponses.size(), language);
//        return categoryResponses;
//    }

    public void updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        log.info("Updating category with ID: {}", categoryId);

        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", categoryId);
                    return new CategoryNotFoundException();
                });

        if (categoryRequest.getName() != null) {
            log.info("Updating category name to: {}", categoryRequest.getName());
            category.getName().put("en", categoryRequest.getName());
        }
        if (categoryRequest.getNameRu() != null) {
            log.info("Updating category name in Russian to: {}", categoryRequest.getNameRu());
            category.getName().put("ru", categoryRequest.getNameRu());
        }
        if (categoryRequest.getNameAz() != null) {
            log.info("Updating category name in Azerbaijani to: {}", categoryRequest.getNameAz());
            category.getName().put("az", categoryRequest.getNameAz());
        }

        if (categoryRequest.getDescription() != null) {
            log.info("Updating category description to: {}", categoryRequest.getDescription());
            category.getDescription().put("en", categoryRequest.getDescription());
        }
        if (categoryRequest.getDescriptionRu() != null) {
            log.info("Updating category description in Russian to: {}", categoryRequest.getDescriptionRu());
            category.getDescription().put("ru", categoryRequest.getDescriptionRu());
        }
        if (categoryRequest.getDescriptionAz() != null) {
            log.info("Updating category description in Azerbaijani to: {}", categoryRequest.getDescriptionAz());
            category.getDescription().put("az", categoryRequest.getDescriptionAz());
        }

        categoryRepository.save(category);
        log.info("Category with ID: {} updated successfully", categoryId);
    }

    public void deleteCategory(Long categoryId) {
        log.info("Deleting category with ID: {}", categoryId);
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", categoryId);
                    return new CategoryNotFoundException();
                });

        categoryRepository.delete(category);
        log.info("Category with ID: {} deleted successfully", categoryId);
    }

}
