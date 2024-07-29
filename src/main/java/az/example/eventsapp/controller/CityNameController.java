package az.example.eventsapp.controller;

import az.example.eventsapp.enums.City;
import az.example.eventsapp.service.CityNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class CityNameController {

    private final CityNameService cityNameService;

    @GetMapping("/cities")
    public List<String> getCities() {
        return Arrays.stream(City.values())
                .map(cityNameService::getCityName)
                .collect(Collectors.toList());
    }
}
