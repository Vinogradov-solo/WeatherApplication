package com.vinogradov.weather.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class WeatherHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String latitude;
    private String longitude;
    private String description;
    private double temperature;
    private double feelsLike;
    private LocalDateTime requestTime;
    private String iconCode;

    public WeatherHistory(String latitude, String longitude, String description, double temperature, double feelsLike, LocalDateTime requestTime, String iconCode) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.requestTime = requestTime;
        this.iconCode = iconCode;
    }
}

