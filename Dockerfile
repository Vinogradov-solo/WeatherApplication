# Используем образ Maven для сборки
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем весь проект в контейнер
COPY . .

# Собираем JAR-файл, пропуская тесты (если тесты нужны, удали -DskipTests)
RUN mvn clean package -DskipTests

# Используем минимальный образ JDK 17 для запуска
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR из предыдущего контейнера
COPY --from=build /app/target/weather-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт 8080
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
