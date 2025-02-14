package com.vinogradov.weather.rest;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vinogradov.weather.model.WeatherHistory;
import com.vinogradov.weather.repository.WeatherHistoryRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

import static com.vinogradov.weather.utils.Utils.formatLocalDateTime;
import static com.vinogradov.weather.utils.Utils.showErrorMessage;

@Route("")
public class WeatherView extends VerticalLayout {

    private final String apiKey;
    private final WeatherHistoryRepository weatherHistoryRepository;

    // Внедрение API-ключа из application.properties
    public WeatherView(@Value("${weather.api.key}") String apiKey, WeatherHistoryRepository weatherHistoryRepository) {
        this.apiKey = apiKey;
        this.weatherHistoryRepository = weatherHistoryRepository;

        // Создаем текстовые поля для ввода координат
        TextField latitudeField = new TextField("Широта");
        TextField longitudeField = new TextField("Долгота");
        Button getWeatherButton = new Button("Показать погоду");

        getWeatherButton.addClickListener(e -> {
            try {
                String latitude = latitudeField.getValue();
                String longitude = longitudeField.getValue();
                if (latitude.isEmpty() || longitude.isEmpty()) {
                    showErrorMessage("Пожалуйста, введите координаты (широту и долготу)");
                    return;
                }
                showWeather(latitude, longitude);
            } catch (Exception ex) {
                showErrorMessage("Ошибка: " + ex.getMessage());
            }
        });

        // Добавляем компоненты на экран
        add(latitudeField, longitudeField, getWeatherButton);
    }

    private void showWeather(String latitude, String longitude) {
        String url = UriComponentsBuilder.fromUriString("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .queryParam("lang", "ru")
                .toUriString();

        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            // Разбираем JSON-ответ
            JSONObject jsonResponse = new JSONObject(response);
            String weatherDescription = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            double temp = jsonResponse.getJSONObject("main").getDouble("temp");
            double feelsLike = jsonResponse.getJSONObject("main").getDouble("feels_like");
            String iconCode = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("icon");

            weatherHistoryRepository.save(new WeatherHistory(latitude, longitude, weatherDescription, temp, feelsLike, LocalDateTime.now()));

            Div weatherText = new Div(new Text(weatherDescription));
            weatherText.getStyle()
                    .set("font-size", "90px")
                    .set("font-weight", "bold")
                    .set("font-family", "'Roboto', sans-serif")
                    .set("color", "#333333");
            Div tempText = new Div(new Text(String.format("Температура: %.1f°C", temp)));
            tempText.getStyle()
                    .set("font-size", "50px")
                    .set("font-weight", "normal")
                    .set("font-family", "'Arial', sans-serif")
                    .set("color", "#666666");
            Div feelsLikeText = new Div(new Text(String.format("Ощущается как: %.1f°C", feelsLike)));
            feelsLikeText.getStyle()
                    .set("font-size", "50px")
                    .set("font-weight", "normal")
                    .set("font-family", "'Arial', sans-serif")
                    .set("color", "#666666");
            Image weatherImage = new Image("https://openweathermap.org/img/wn/" + iconCode + "@2x.png", "Погода");
            weatherImage.setWidth("300px");
            weatherImage.setHeight("300px");

            removeAll(); // Убираем старые компоненты
            add(weatherText, tempText, feelsLikeText, weatherImage);

            // Показываем историю запросов
            showHistory();
        } catch (Exception e) {
        showErrorMessage("Ошибка при получении данных о погоде: " + e.getMessage());
        }
    }

    private void showHistory() {
        Div historyHeader = new Div(new Text("История запросов"));
        historyHeader.getStyle()
                .set("font-size", "25px")
                .set("font-weight", "bold")
                .set("font-family", "'Arial', sans-serif")
                .set("color", "#333333");
        Div historyDiv = new Div();
        historyDiv.getStyle().set("margin-left", "20px");

        weatherHistoryRepository.findAll().forEach(history -> {
            Div historyEntry = new Div(new Text(String.format("%s  |  Широта: %s, Долгота: %s, Погода: %s, Температура: %.1f°C, Ощущается как: %.1f°C",
                    formatLocalDateTime(history.getRequestTime()), history.getLatitude(), history.getLongitude(), history.getDescription(), history.getTemperature(), history.getFeelsLike())));
            historyEntry.getStyle()
                    .set("font-size", "20px")
                    .set("font-weight", "normal")
                    .set("font-family", "'Arial', sans-serif")
                    .set("color", "#333333");
            historyDiv.add(historyEntry);
        });

        add(historyHeader, historyDiv);
    }
}