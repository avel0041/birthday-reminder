# Приложение "Поздравлятор"

SPA веб-приложение для отслеживания дней рождений с REST API и базой данных.

## Возможности
- Просмотр сегодняшних и ближайших дней рождений
- Добавление, редактирование и удаление записей

## Технологии
- **Backend:** Spring Boot 4.0.2, Java 17, Spring Data JPA, H2/PostgreSQL
- **Frontend:** HTML5, Bootstrap 5, Vanilla JavaScript (Фронтенд написан при помощи DeepSeek AI)
- **База данных:** H2 (разработка)

## Тестовый стенд
- Экземпляр приложения развернут на [сайте](dr.den-art.ru)

## Установка и запуск

### 1. Клонирование и сборка
```bash
git clone <repository-url>
cd BirthdayReminder
mvn clean package
```

### 2. Запуск приложения
```bash
java -jar target/app-0.0.1-SNAPSHOT.jar
```

### 3. Доступ к приложению
- Веб-интерфейс: http://localhost:8080
- REST API: http://localhost:8080/api/birthdays
- H2 Console (dev): http://localhost:8080/h2-console

## Структура проекта
```
src/main/java/com/birthdayreminder/app/
├── BirthdayReminderApplication.java
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
}
```

## API Endpoints
| Метод | Путь | Описание |
|-------|------|----------|
| GET | `/api/birthdays/dashboard` | Главная страница (сегодняшние + ближайшие) |
| GET | `/api/birthdays` | Все дни рождения |
| POST | `/api/birthdays` | Создать запись |
| PUT | `/api/birthdays/{id}` | Обновить запись |
| DELETE | `/api/birthdays/{id}` | Удалить запись |

## Конфигурация
### Основные настройки (application.properties)
```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:birthdaydb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```
