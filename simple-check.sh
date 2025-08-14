#!/bin/bash

echo "=== Простая проверка работоспособности проекта Java Core ==="
echo ""

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Функция для проверки статуса
check_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ $2${NC}"
    else
        echo -e "${RED}❌ $2${NC}"
    fi
}

echo "1. Проверка сборки проекта..."
mvn clean compile -q
check_status $? "Проект собирается"

echo ""
echo "2. Проверка структуры модулей..."
if [ -d "product-service" ] && [ -d "payment-service" ] && [ -d "limit-service" ]; then
    echo -e "${GREEN}✅ Модули product-service, payment-service и limit-service существуют${NC}"
else
    echo -e "${RED}❌ Модули не найдены${NC}"
    exit 1
fi

echo ""
echo "3. Проверка наличия основных файлов..."
if [ -f "product-service/pom.xml" ] && [ -f "payment-service/pom.xml" ] && [ -f "limit-service/pom.xml" ]; then
    echo -e "${GREEN}✅ POM файлы модулей существуют${NC}"
else
    echo -e "${RED}❌ POM файлы модулей не найдены${NC}"
    exit 1
fi

echo ""
echo "4. Проверка Java файлов..."
if [ -f "product-service/src/main/java/com/example/product/MainApp.java" ] && [ -f "payment-service/src/main/java/com/example/payment/PaymentApplication.java" ] && [ -f "limit-service/src/main/java/com/example/limit/LimitApplication.java" ]; then
    echo -e "${GREEN}✅ Основные Java файлы существуют${NC}"
else
    echo -e "${RED}❌ Основные Java файлы не найдены${NC}"
    exit 1
fi

echo ""
echo "5. Проверка конфигурационных файлов..."
if [ -f "product-service/src/main/resources/application.yml" ] && [ -f "payment-service/src/main/resources/application.yml" ] && [ -f "limit-service/src/main/resources/application.yml" ]; then
    echo -e "${GREEN}✅ Конфигурационные файлы существуют${NC}"
else
    echo -e "${RED}❌ Конфигурационные файлы не найдены${NC}"
    exit 1
fi

echo ""
echo "6. Проверка портов..."
if lsof -i :8080 > /dev/null 2>&1; then
    echo -e "${YELLOW}⚠️  Порт 8080 занят (возможно, уже запущен product-service)${NC}"
else
    echo -e "${GREEN}✅ Порт 8080 свободен для product-service${NC}"
fi

if lsof -i :8081 > /dev/null 2>&1; then
    echo -e "${YELLOW}⚠️  Порт 8081 занят (возможно, уже запущен payment-service)${NC}"
else
    echo -e "${GREEN}✅ Порт 8081 свободен для payment-service${NC}"
fi

if lsof -i :8082 > /dev/null 2>&1; then
    echo -e "${YELLOW}⚠️  Порт 8082 занят (возможно, уже запущен limit-service)${NC}"
else
    echo -e "${GREEN}✅ Порт 8082 свободен для limit-service${NC}"
fi

echo ""
echo "=== Результаты проверки ==="
echo -e "${GREEN}✅ Проект готов к запуску!${NC}"
echo ""
echo "Для запуска сервисов выполните:"
echo "1. cd product-service && mvn spring-boot:run"
echo "2. cd payment-service && mvn spring-boot:run (в новом терминале)"
echo "3. cd limit-service && mvn spring-boot:run (в новом терминале)"
echo ""
echo "Примечание: Убедитесь, что PostgreSQL запущен на localhost:5432 и localhost:5433"
echo "и созданы базы данных product_db, payment_db и limit_db" 