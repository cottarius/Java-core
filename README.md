# Java Core - Модульная архитектура с платежным сервисом

Этот проект демонстрирует модульную архитектуру с двумя сервисами:
- **Product Service** (порт 8080) - сервис управления продуктами
- **Payment Service** (порт 8081) - платежный сервис с интеграцией

## Структура проекта

```
Java-core/
├── pom.xml (родительский pom)
├── product-service/ (модуль сервиса продуктов)
│   ├── pom.xml
│   └── src/main/java/com/example/product/
│       ├── MainApp.java
│       ├── controller/ProductController.java
│       ├── model/User.java, Product.java
│       ├── repository/UserRepository.java, ProductRepository.java
│       └── service/UserService.java, ProductService.java
└── payment-service/ (модуль платежного сервиса)
    ├── pom.xml
    └── src/main/java/com/example/payment/
        ├── PaymentApplication.java
        ├── controller/PaymentController.java
        ├── model/Payment.java, Product.java
        ├── repository/PaymentRepository.java
        ├── service/PaymentService.java
        ├── client/ProductServiceClient.java
        └── dto/PaymentRequest.java, PaymentResponse.java, ErrorResponse.java
```

## Требования

- Java 17
- Maven 3.6+
- PostgreSQL

## Настройка базы данных

### База данных для сервиса продуктов
```sql
CREATE DATABASE product_db;
```

### База данных для платежного сервиса
```sql
CREATE DATABASE payment_db;
```

## Запуск сервисов

### 1. Запуск сервиса продуктов
```bash
cd product-service
mvn spring-boot:run
```
Сервис будет доступен на http://localhost:8080

### 2. Запуск платежного сервиса
```bash
cd payment-service
mvn spring-boot:run
```
Сервис будет доступен на http://localhost:8081

## API Endpoints

### Сервис продуктов (порт 8080)

#### Создание продукта
```bash
POST /api/products?accountNumber=ACC001&balance=1000.00&productType=ACCOUNT&userId=1
```

#### Получение продукта по ID
```bash
GET /api/products/1
```

#### Получение продуктов пользователя
```bash
GET /api/products/user/1
```

#### Получение продукта по номеру счета
```bash
GET /api/products/account/ACC001
```

### Платежный сервис (порт 8081)

#### Обработка платежа
```bash
POST /api/payments
Content-Type: application/json

{
  "productId": 1,
  "amount": 100.00,
  "description": "Payment for services"
}
```

#### Получение платежа по ID
```bash
GET /api/payments/1
```

#### Получение всех платежей
```bash
GET /api/payments
```

#### Получение платежей по продукту
```bash
GET /api/payments/product/1
```

#### Получение платежей по статусу
```bash
GET /api/payments/status/COMPLETED
```

#### Получение продуктов пользователя через платежный сервис
```bash
GET /api/payments/products/user/1
```

#### Получение продукта по ID через платежный сервис
```bash
GET /api/payments/products/1
```

#### Получение продукта по номеру счета через платежный сервис
```bash
GET /api/payments/products/account/ACC001
```

## Особенности реализации

### Интеграция между сервисами
- Платежный сервис использует `RestTemplate` для интеграции с сервисом продуктов
- Конфигурация URL сервиса продуктов настраивается через `product.service.url` в `application.yml`

### Обработка ошибок
- Оба сервиса возвращают структурированные ошибки в формате JSON
- Платежный сервис обрабатывает ошибки как на своей стороне, так и на стороне сервиса продуктов

### Процесс исполнения платежа
1. Проверка существования продукта
2. Проверка достаточности средств
3. Создание записи о платеже
4. Обновление статуса платежа

### Валидация
- Использование Jakarta Validation для валидации входящих запросов
- Проверка корректности данных на уровне сервиса

## Тестирование

### Тест интеграции
1. Запустите оба сервиса
2. Создайте продукт через сервис продуктов
3. Выполните платеж через платежный сервис
4. Проверьте, что платежный сервис может получить информацию о продукте

### Пример тестового сценария
```bash
# 1. Создание продукта
curl -X POST "http://localhost:8080/api/products?accountNumber=ACC001&balance=1000.00&productType=ACCOUNT&userId=1"

# 2. Выполнение платежа
curl -X POST "http://localhost:8081/api/payments" \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "amount": 100.00, "description": "Test payment"}'

# 3. Получение информации о продукте через платежный сервис
curl "http://localhost:8081/api/payments/products/1"
```

## Конфигурация

### Настройка портов
- Сервис продуктов: `server.port=8080`
- Платежный сервис: `server.port=8081`

### Настройка баз данных
- Сервис продуктов: `product_db`
- Платежный сервис: `payment_db`

### Настройка интеграции
- URL сервиса продуктов в платежном сервисе: `product.service.url=http://localhost:8080` 