package az.example.eventsapp.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends GenericException{
    private static final String CATEGORY_NOT_FOUND = "Category not found";

    public CategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), CATEGORY_NOT_FOUND);
    }
}
