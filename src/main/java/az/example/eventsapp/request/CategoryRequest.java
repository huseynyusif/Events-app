package az.example.eventsapp.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private String name;
    private String nameRu;
    private String nameAz;
    private String description;
    private String descriptionRu;
    private String descriptionAz;
}
