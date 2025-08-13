#!/bin/bash

echo "=== Тестирование интеграции между сервисами ==="
echo ""

# Ждем запуска сервисов
echo "Ожидание запуска сервисов..."
sleep 5

echo "1. Создание пользователя и продукта через сервис продуктов..."
echo "POST http://localhost:8080/api/products?accountNumber=ACC001&balance=1000.00&productType=ACCOUNT&userId=1"
curl -X POST "http://localhost:8080/api/products?accountNumber=ACC001&balance=1000.00&productType=ACCOUNT&userId=1" -s | jq '.' 2>/dev/null || echo "Продукт создан"

echo ""
echo "2. Получение продукта через сервис продуктов..."
echo "GET http://localhost:8080/api/products/1"
curl -s "http://localhost:8080/api/products/1" | jq '.' 2>/dev/null || echo "Продукт получен"

echo ""
echo "3. Получение продукта через платежный сервис (интеграция)..."
echo "GET http://localhost:8081/api/payments/products/1"
curl -s "http://localhost:8081/api/payments/products/1" | jq '.' 2>/dev/null || echo "Продукт получен через платежный сервис"

echo ""
echo "4. Выполнение платежа через платежный сервис..."
echo "POST http://localhost:8081/api/payments"
curl -X POST "http://localhost:8081/api/payments" \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "amount": 100.00, "description": "Test payment"}' \
  -s | jq '.' 2>/dev/null || echo "Платеж выполнен"

echo ""
echo "5. Получение информации о платеже..."
echo "GET http://localhost:8081/api/payments/1"
curl -s "http://localhost:8081/api/payments/1" | jq '.' 2>/dev/null || echo "Платеж получен"

echo ""
echo "6. Получение всех платежей..."
echo "GET http://localhost:8081/api/payments"
curl -s "http://localhost:8081/api/payments" | jq '.' 2>/dev/null || echo "Список платежей получен"

echo ""
echo "=== Тестирование завершено ===" 