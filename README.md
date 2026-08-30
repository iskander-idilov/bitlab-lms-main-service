# Bitlab LMS — Main Service

REST API для управления образовательным контентом Bitlab LMS: курсы, главы и уроки. Один из микросервисов платформы Bitlab LMS для Bitlab Academy — отвечает за структуру и содержимое учебных программ.

## Содержание

- [Стек технологий](#стек-технологий)
- [Архитектура](#архитектура)
- [Быстрый старт](#быстрый-старт)
- [Docker](#docker)
- [API документация](#api-документация)
- [Основные эндпоинты](#основные-эндпоинты)
- [Тестирование](#тестирование)
- [Логирование](#логирование)
- [Структура проекта](#структура-проекта)
- [Roadmap](#roadmap)

## Стек технологий

- **Java 17**
- **Spring Boot 4.1.0** (Web, Data JPA, Validation)
- **PostgreSQL** — основная база данных
- **Liquibase** — управление миграциями схемы БД
- **MapStruct** — маппинг между Entity и DTO
- **Lombok** — сокращение boilerplate-кода
- **Springdoc OpenAPI (Swagger)** — автогенерация API-документации
- **SLF4J + Logback** — логирование
- **JUnit 5 + Mockito** — unit-тестирование
- **Maven** — сборка проекта
- **Docker** — контейнеризация

## Архитектура

Сервис построен по классической слоистой архитектуре:

```
Controller → Service → Mapper + Repository → Entity → PostgreSQL
                ↑
              DTO (Request/Response)
```

- **Controller** — принимает HTTP-запросы, работает только с DTO
- **Service** — бизнес-логика, единственный слой, работающий и с DTO, и с Entity
- **Mapper** (MapStruct) — преобразование Entity ↔ DTO
- **Repository** (Spring Data JPA) — доступ к БД
- **GlobalExceptionHandler** — централизованная обработка ошибок (`@RestControllerAdvice`)

### Доменная модель

```
Course (курс)
  └── Chapter (глава)
        └── Lesson (урок)
```

Каждая глава принадлежит курсу, каждый урок — главе (связи `@ManyToOne`).

## Быстрый старт

### Требования

- Java 17+
- Maven (или используйте `./mvnw`)
- PostgreSQL (локально или через Docker)

### Настройка окружения

Сервис ожидает переменные окружения для подключения к БД:

```bash
export DB_USERNAME=lms_user
export DB_PASSWORD=ваш_пароль
```

### Запуск

```bash
./mvnw clean install
./mvnw spring-boot:run
```

Приложение поднимется на `http://localhost:8080`.

## Docker

Образ доступен на Docker Hub:

```bash
docker pull idiloviskander/bitlab-lms-main-service:1.0
```

### Сборка образа локально

```bash
./mvnw clean package -DskipTests
docker build -t idiloviskander/bitlab-lms-main-service:1.0 .
```

## API документация

После запуска приложения Swagger UI доступен по адресу:

```
http://localhost:8080/swagger-ui/index.html
```

Там можно посмотреть полный список эндпоинтов, их параметры, тела запросов/ответов и коды ошибок, а также протестировать запросы прямо из браузера.

## Основные эндпоинты

| Метод | Путь | Описание |
|---|---|---|
| `POST` | `/courses` | Создать курс |
| `GET` | `/courses/{id}` | Получить курс по id |
| `PATCH` | `/courses/{id}` | Частично обновить курс |
| `DELETE` | `/courses/{id}` | Удалить курс |
| `POST` | `/courses/{courseId}/chapters` | Создать главу в курсе |
| `GET` | `/courses/{courseId}/chapters/{id}` | Получить главу |
| `PATCH` | `/courses/{courseId}/chapters/{id}` | Частично обновить главу |
| `DELETE` | `/courses/{courseId}/chapters/{id}` | Удалить главу |
| `POST` | `/chapters/{chapterId}/lessons` | Создать урок в главе |
| `GET` | `/chapters/{chapterId}/lessons/{id}` | Получить урок |
| `PATCH` | `/chapters/{chapterId}/lessons/{id}` | Частично обновить урок |
| `DELETE` | `/chapters/{chapterId}/lessons/{id}` | Удалить урок |

Полный список — в Swagger UI.

### Обработка ошибок

Все ошибки возвращаются в едином формате через `GlobalExceptionHandler`:

```json
{
  "message": "Course not found with id: 999",
  "status": 404,
  "timestamp": "2026-08-18T10:15:30"
}
```

- `404` — сущность не найдена
- `400` — ошибка валидации входных данных
- `500` — непредвиденная ошибка сервера (стектрейс в ответе клиенту не отображается, только в логах сервера)

## Тестирование

Проект покрыт unit-тестами (JUnit 5 + Mockito):

- **Service-слой** — тесты на `CourseService`, `ChapterService`, `LessonService` с моками `Repository`/`Mapper`, включая проверку исключений при отсутствующих сущностях
- **Controller-слой** — тесты через `MockMvc` и `@WebMvcTest`, включая проверку валидации запросов

Запуск тестов:

```bash
./mvnw test
```

### Логирование

Используется SLF4J с тремя уровнями:

- **INFO** — ключевые события (создание/обновление/удаление сущностей)
- **DEBUG** — подробные данные запросов (для отладки)
- **ERROR** — все перехваченные исключения

## Структура проекта

```
src/main/java/kz/bitlab/springboot/mainservice/
├── controller/       # REST-контроллеры
├── service/          # Бизнес-логика
├── mapper/           # MapStruct-мапперы
├── entity/           # JPA-сущности
├── dto/
│   ├── request/       # Create/Update DTO
│   └── response/       # Response DTO
├── repository/       # Spring Data JPA репозитории
├── exception/         # GlobalExceptionHandler, ErrorResponse
└── config/            # Конфигурация (OpenAPI и др.)
```

## Roadmap

Проект разрабатывается в рамках практикума поэтапно:

- [x] Sprint 1 — CRUD для Course / Chapter / Lesson, Swagger, логирование, unit-тесты, Docker
- [ ] Интеграция с User Service (аутентификация, роли, JWT)
- [ ] Интеграция с File Service (хранение файлов уроков)
- [ ] Spring Security
- [ ] Docker Compose для оркестрации всех сервисов платформы

---

Часть экосистемы **Bitlab LMS** — учебной платформы Bitlab Academy, построенной на микросервисной архитектуре.