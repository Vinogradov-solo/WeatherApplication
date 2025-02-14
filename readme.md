# Weather Application

Это приложение отображает погоду по координатам с использованием OpenWeatherMap API. Также сохраняет историю запросов в базе данных PostgreSQL.

## Технологии:
- Java 17
- Spring Boot
- Vaadin
- PostgreSQL
- Docker

## Как запустить приложение в Docker

### 1. Клонируйте репозиторий:
```bash
git clone https://github.com/your-repo/weather-app.git
cd weather-app
```
### 2. Запустите приложение:
```bash
docker-compose up --build -d
```
Это автоматически:

- Поднимет контейнер с PostgreSQL.
- Соберёт и запустит Java-приложение.

### 3. Откройте приложение:
Перейдите в браузере по адресу:
http://localhost:8080

## Конфигурация базы данных
Приложение использует PostgreSQL с параметрами:

- Порт: 5438
- База данных: weatherdb
- Пользователь: postgres
- Пароль: 12345
## Запуск без Docker (альтернативный вариант)
Если хотите запустить вручную:

- Установите PostgreSQL и создайте базу weatherdb.
- Сконфигурируйте application.properties (порт 5438).
- Соберите JAR:
```bash
mvn clean package -DskipTests
```
- Запустите приложение:
```bash
java -jar target/weather-0.0.1-SNAPSHOT.jar
```