package az.example.eventsapp.service;

import az.example.eventsapp.entity.CategoryEntity;
import az.example.eventsapp.exception.CategoryNotFoundException;
import az.example.eventsapp.mapper.CategoryMapper;
import az.example.eventsapp.repository.CategoryRepository;
import az.example.eventsapp.request.CategoryRequest;
import az.example.eventsapp.response.CategoryResponse;
import az.example.eventsapp.util.LocaleResolverUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final LocaleResolverUtil localeResolverUtil;

    public void createCategory(CategoryRequest categoryCreateRequest) {
        CategoryEntity categoryEntity = categoryMapper.toEntity(categoryCreateRequest);
        categoryRepository.save(categoryEntity);
    }

    public List<CategoryResponse> getAllCategory(Locale locale) {
        return categoryRepository.findAll()
                .stream()
                .map(categoryEntity -> localeResolverUtil.setForLocalCategory(categoryEntity,locale))
                .toList();
    }

//    public List<CategoryResponse> getAllCategory(String language) {
//        List<CategoryEntity> categories = categoryRepository.findAll();
//        return categories.stream()
//                .map(category -> {
//                    CategoryResponse response = categoryMapper.toResponse(category);
//                    localeResolverUtil.setForLocalCategory(response, language);
//                    return response;
//                })
//                .collect(Collectors.toList());
//    }

    public void updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        categoryMapper.updateCategoryFromDto(categoryRequest,category);
        categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);

    }

}
