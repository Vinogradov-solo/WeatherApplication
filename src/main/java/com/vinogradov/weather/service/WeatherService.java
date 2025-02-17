package com.vinogradov.weather.service;

import com.vinogradov.weather.model.WeatherHistory;
import com.vinogradov.weather.repository.WeatherHistoryRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeatherService {

    private final String apiKey;
    private final WeatherHistoryRepository repository;
    private final RestTemplate restTemplate;

    public WeatherService(@Value("${weather.api.key}") String apiKey, WeatherHistoryRepository repository) {
        this.apiKey = apiKey;
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    public WeatherHistory getWeather(String latitude, String longitude) {
        String url = UriComponentsBuilder.fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .queryParam("lang", "ru")
                .toUriString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonResponse = new JSONObject(response);

            String weatherDescription = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            double temp = jsonResponse.getJSONObject("main").getDouble("temp");
            double feelsLike = jsonResponse.getJSONObject("main").getDouble("feels_like");
            String iconCode = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("icon");

            // Создаём и сохраняем объект WeatherHistory
            WeatherHistory history = new WeatherHistory(latitude, longitude, weatherDescription, temp, feelsLike, LocalDateTime.now(), iconCode);
            repository.save(history);

            return history;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки данных: " + e.getMessage());
        }
    }

    public List<WeatherHistory> getHistory() {
        return repository.findAll();
    }
}