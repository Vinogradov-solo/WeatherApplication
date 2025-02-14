package com.vinogradov.weather.repository;

import com.vinogradov.weather.model.WeatherHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherHistoryRepository extends JpaRepository<WeatherHistory, Long> {
}
