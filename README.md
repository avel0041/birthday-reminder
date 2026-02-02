# Приложение "Поздравлятор"

SPA веб‑приложение для отслеживания дней рождения с REST API, хранением данных в БД и поддержкой фотографий.

## Возможности
- Просмотр сегодняшних и ближайших дней рождений
- Добавление, редактирование и удаление записей
- Загрузка фотографии для человека
- Просмотр профиля

## Технологии
- **Backend:** Spring Boot, Java 17, Spring Data JPA
- **Frontend:** HTML5, Bootstrap 5, Vanilla JavaScript
- **База данных:** PostgreSQL (по умолчанию), H2 (для разработки)

## Запуск

### 1. Клонирование и сборка
```bash
git clone <repository-url>
cd BirthdayReminder/birthday-reminder
mvn clean package
```

### 2. Запуск приложения
```bash
java -jar target/*.jar
```

### 3. Доступ
- Веб‑интерфейс: http://localhost:8080
- REST API: http://localhost:8080/api/birthdays
- H2 Console (dev): http://localhost:8080/h2-console
- Загруженные фото: http://localhost:8080/uploads/people/<id>.<ext>

## Хранение фотографий
- Файлы сохраняются в директорию `uploads/people` относительно рабочей директории приложения.
- URL сохраняется в БД в поле `photoUrl`.
- Ресурсы `/uploads/**` отдаются через `WebConfig`.

## Структура проекта
```
src/main/java/com/birthdayreminder/app/
├── BirthdayReminderApplication.java
├── config/WebConfig.java
├── controller/BirthdayController.java
├── model/Person.java
├── repository/PersonRepository.java
├── service/BirthdayService.java
├── util/DataGenerator.java
└── dto/
    ├── DashboardDTO.java
    └── PersonDTO.java
```

## Модель данных
```java
@Entity
public class Person {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String photoUrl;
}
```

## API Endpoints
| Метод | Путь | Описание |
|-------|------|----------|
| GET | `/api/birthdays/dashboard` | Данные для главной страницы |
| GET | `/api/birthdays` | Все дни рождения |
| GET | `/api/birthdays/{id}` | Профиль человека |
| POST | `/api/birthdays` | Создать запись |
| PUT | `/api/birthdays/{id}` | Обновить запись |
| DELETE | `/api/birthdays/{id}` | Удалить запись |
| POST | `/api/birthdays/{id}/photo` | Загрузить фотографию |

## Конфигурация
### Основные настройки (application.properties)
```properties
server.port=8080
spring.jpa.hibernate.ddl-auto=update
spring.web.resources.static-locations=classpath:/static/,file:./uploads/

# Multipart Upload Limits
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB
```

### Пример PostgreSQL
```properties
spring.datasource.url=jdbc:postgresql://192.168.0.77:9009/postgres
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Пример H2 (для разработки)
```properties
spring.datasource.url=jdbc:h2:mem:birthdaydb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```