# Быстрый старт

## 1. Подготовка базы данных
```sql
CREATE DATABASE product_db;
CREATE DATABASE payment_db;
```

## 2. Запуск сервиса продуктов
```bash
cd product-service
mvn spring-boot:run
```
Сервис запустится на http://localhost:8080

## 3. Запуск платежного сервиса (в новом терминале)
```bash
cd payment-service
mvn spring-boot:run
```
Сервис запустится на http://localhost:8081

## 4. Тестирование интеграции
```bash
./test-integration.sh
```

## Основные API endpoints

### Сервис продуктов (порт 8080)
- `POST /api/products` - создание продукта
- `GET /api/products/{id}` - получение продукта
- `GET /api/products/user/{userId}` - продукты пользователя

### Платежный сервис (порт 8081)
- `POST /api/payments` - выполнение платежа
- `GET /api/payments/{id}` - получение платежа
- `GET /api/payments/products/{id}` - получение продукта через платежный сервис

## Пример создания платежа
```bash
curl -X POST "http://localhost:8081/api/payments" \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "amount": 100.00, "description": "Test payment"}'
``` 