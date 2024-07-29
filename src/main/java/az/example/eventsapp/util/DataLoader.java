package az.example.eventsapp.util;

import az.example.eventsapp.entity.CategoryEntity;
import az.example.eventsapp.entity.EventEntity;
import az.example.eventsapp.entity.UserEntity;
import az.example.eventsapp.entity.VenueEntity;
import az.example.eventsapp.enums.EventStatus;
import az.example.eventsapp.enums.Role;
import az.example.eventsapp.repository.CategoryRepository;
import az.example.eventsapp.repository.EventRepository;
import az.example.eventsapp.repository.UserRepository;
import az.example.eventsapp.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        loadCategories();
    }

    private void loadCategories() {
        if (categoryRepository.count() == 0) {
            List<CategoryEntity> categories = new ArrayList<>();

            categories.add(CategoryEntity.builder()
                    .name(Map.of(
                            "en", "Camping",
                            "ru", "Кемпинг",
                            "az", "Düşərgə"))
                    .description(Map.of(
                            "en", "Enjoy nature and outdoor activities with our camping events.",
                            "ru", "Наслаждайтесь природой и активным отдыхом с нашими кемпинг-событиями.",
                            "az", "Təbiətdən və açıq hava fəaliyyətlərindən zövq alın bizim düşərgə tədbirlərimizlə."))
                    .build());

            categories.add(CategoryEntity.builder()
                    .name(Map.of(
                            "en", "Conferences",
                            "ru", "Конференции",
                            "az", "Konfranslar"))
                    .description(Map.of(
                            "en", "Attend insightful and professional conferences on various topics.",
                            "ru", "Посещайте познавательные и профессиональные конференции по различным темам.",
                            "az", "Müxtəlif mövzularda ətraflı və peşəkar konfranslara qatılın."))
                    .build());

            categories.add(CategoryEntity.builder()
                    .name(Map.of(
                            "en", "Summer parties",
                            "ru", "Летние вечеринки",
                            "az", "Yay partiləri"))
                    .description(Map.of(
                            "en", "Celebrate the summer season with lively parties and social events.",
                            "ru", "Отпразднуйте летний сезон с живыми вечеринками и социальными мероприятиями.",
                            "az", "Yay mövsümünü canlı partilər və sosial tədbirlərlə qeyd edin."))
                    .build());

            categories.add(CategoryEntity.builder()
                    .name(Map.of(
                            "en", "Concert",
                            "ru", "Концерт",
                            "az", "Konsert"))
                    .description(Map.of(
                            "en", "Enjoy music and performances from various artists and bands.",
                            "ru", "Наслаждайтесь музыкой и выступлениями различных артистов и групп.",
                            "az", "Müxtəlif ifaçılardan və qruplardan musiqi və performanslardan zövq alın."))
                    .build());

            categories.add(CategoryEntity.builder()
                    .name(Map.of(
                            "en", "Hiking",
                            "ru", "Пеший туризм",
                            "az", "Piyada yürüş"))
                    .description(Map.of(
                            "en", "Explore nature trails and enjoy adventurous hikes.",
                            "ru", "Исследуйте природные тропы и наслаждайтесь приключенческими походами.",
                            "az", "Təbiət yollarını kəşf edin və macəra dolu yürüşlərin dadını çıxarın."))
                    .build());

            categories.add(CategoryEntity.builder()
                    .name(Map.of(
                            "en", "Another",
                            "ru", "Другой",
                            "az", "Digər"))
                    .description(Map.of(
                            "en", "Discover various other events and activities.",
                            "ru", "Откройте для себя различные другие события и мероприятия.",
                            "az", "Müxtəlif digər tədbirləri və fəaliyyətləri kəşf edin."))
                    .build());

            categoryRepository.saveAll(categories);
        }
        }
    }