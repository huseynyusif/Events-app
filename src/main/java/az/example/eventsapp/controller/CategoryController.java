package az.example.eventsapp.controller;

import az.example.eventsapp.request.CategoryRequest;
import az.example.eventsapp.response.CategoryResponse;
import az.example.eventsapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public void createCategory(@RequestBody CategoryRequest categoryCreateRequest) {
        categoryService.createCategory(categoryCreateRequest);
    }

    @GetMapping("/all")
    public List<CategoryResponse> getAllCategory(@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return categoryService.getAllCategory(locale != null ? locale : Locale.ENGLISH);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{category-id}")
    public void updateCategory(@PathVariable(name = "category-id") Long categoryId, @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(categoryId, categoryRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{category-id}")
    public void deleteCategory(@PathVariable(name = "category-id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
