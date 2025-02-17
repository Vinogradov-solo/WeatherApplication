package com.vinogradov.weather.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vinogradov.weather.model.WeatherHistory;
import com.vinogradov.weather.service.WeatherService;

import java.util.List;

import static com.vinogradov.weather.utils.Utils.formatLocalDateTime;
import static com.vinogradov.weather.utils.Utils.showErrorMessage;

@Route("")
public class WeatherView extends VerticalLayout {

    private final WeatherService weatherService;

    public WeatherView(WeatherService weatherService) {
        this.weatherService = weatherService;

        TextField latitudeField = new TextField("Широта");
        TextField longitudeField = new TextField("Долгота");
        Button getWeatherButton = new Button("Показать погоду");

        Div weatherDiv = new Div();
        Div historyDiv = new Div();

        getWeatherButton.addClickListener(e -> {
            String latitude = latitudeField.getValue();
            String longitude = longitudeField.getValue();
            if (latitude.isEmpty() || longitude.isEmpty()) {
                showErrorMessage("Введите координаты!");
                return;
            }

            // Запрашиваем погоду через сервис
            WeatherHistory weather = weatherService.getWeather(latitude, longitude);

            Div weatherText = new Div(new Text(weather.getDescription()));
            weatherText.getStyle()
                    .set("font-size", "90px")
                    .set("font-weight", "bold")
                    .set("font-family", "'Roboto', sans-serif")
                    .set("color", "#333333");
            Div tempText = new Div(new Text(String.format("Температура: %.1f°C", weather.getTemperature())));
            tempText.getStyle()
                    .set("font-size", "50px")
                    .set("font-weight", "normal")
                    .set("font-family", "'Arial', sans-serif")
                    .set("color", "#666666");
            Div feelsLikeText = new Div(new Text(String.format("Ощущается как: %.1f°C", weather.getFeelsLike())));
            feelsLikeText.getStyle()
                    .set("font-size", "50px")
                    .set("font-weight", "normal")
                    .set("font-family", "'Arial', sans-serif")
                    .set("color", "#666666");
            Image weatherImage = new Image("https://openweathermap.org/img/wn/" + weather.getIconCode() + "@2x.png", "Погода");
            weatherImage.setWidth("300px");
            weatherImage.setHeight("300px");

            removeAll(); // Убираем старые компоненты
            add(weatherText, tempText, feelsLikeText, weatherImage);

            // Показываем историю запросов
            showHistory(historyDiv);
        });

        // Добавляем UI-компоненты
        add(latitudeField, longitudeField, getWeatherButton, weatherDiv);
    }

    private void showHistory(Div historyDiv) {
        historyDiv.removeAll();

        List<WeatherHistory> historyList = weatherService.getHistory();
        for (WeatherHistory history : historyList) {
            Div historyEntry = new Div(new Text(String.format(
                    "%s | Широта: %s, Долгота: %s, Погода: %s, Температура: %.1f°C, Ощущается как: %.1f°C",
                    formatLocalDateTime(history.getRequestTime()),
                    history.getLatitude(),
                    history.getLongitude(),
                    history.getDescription(),
                    history.getTemperature(),
                    history.getFeelsLike()
            )));
            historyEntry.getStyle()
                    .set("font-size", "14px")
                    .set("color", "#333333");
            historyDiv.add(historyEntry);
            add(historyDiv);
        }
    }
}
