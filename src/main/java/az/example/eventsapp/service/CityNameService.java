package az.example.eventsapp.service;

import az.example.eventsapp.enums.City;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityNameService {

    private final MessageSource messageSource;

    public String getCityName(City city) {
        return messageSource.getMessage("city." + city.name(), null, LocaleContextHolder.getLocale());
    }
}
