package com.vinogradov.weather.controller;

import com.vinogradov.weather.model.WeatherHistory;
import com.vinogradov.weather.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // Получить погоду по координатам (и сохранить в БД)
    @GetMapping
    public WeatherHistory getWeather(@RequestParam String lat, @RequestParam String lon) {
        return weatherService.getWeather(lat, lon);
    }

    // Получить историю запросов
    @GetMapping("/history")
    public List<WeatherHistory> getWeatherHistory() {
        return weatherService.getHistory();
    }
}