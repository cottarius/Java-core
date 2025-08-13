# Java Core Project

Проект демонстрирует работу с пользователями и их продуктами (счета, карты) с использованием Spring Boot, JPA, PostgreSQL и Liquibase.

## Структура проекта

- **User** - модель пользователя (id, username)
- **Product** - модель продукта клиента (id, номер счета, баланс, тип продукта, userId)
- **UserService** - сервис для работы с пользователями
- **ProductService** - сервис для работы с продуктами
- **ProductController** - REST API для работы с продуктами

## API Endpoints

### Продукты

- `GET /api/products` - получить все продукты
- `GET /api/products/{id}` - получить продукт по ID
- `GET /api/products/user/{userId}` - получить все продукты пользователя
- `GET /api/products/account/{accountNumber}` - получить продукт по номеру счета
- `POST /api/products` - создать новый продукт
- `DELETE /api/products/{id}` - удалить продукт

### Создание продукта

```json
{
  "accountNumber": "ACC001",
  "balance": 1000.00,
  "productType": "ACCOUNT",
  "userId": 1
}
```

## Запуск

1. Убедитесь, что PostgreSQL запущен на localhost:5432
2. Создайте базу данных `testdb`
3. Запустите приложение: `mvn spring-boot:run`

## Миграции

База данных автоматически создается и заполняется тестовыми данными через Liquibase:

- Создание таблиц пользователей и продуктов
- Добавление тестовых пользователей
- Добавление тестовых продуктов

## Технологии

- Java 17
- Spring Boot 3.2.6
- Spring Data JPA
- Spring Web
- PostgreSQL
- Liquibase 