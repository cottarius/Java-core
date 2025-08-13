#!/bin/bash

echo "=== Проверка работоспособности проекта Java Core ==="
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
if [ -d "product-service" ] && [ -d "payment-service" ]; then
    echo -e "${GREEN}✅ Модули product-service и payment-service существуют${NC}"
else
    echo -e "${RED}❌ Модули не найдены${NC}"
    exit 1
fi

echo ""
echo "3. Проверка наличия основных файлов..."
if [ -f "product-service/pom.xml" ] && [ -f "payment-service/pom.xml" ]; then
    echo -e "${GREEN}✅ POM файлы модулей существуют${NC}"
else
    echo -e "${RED}❌ POM файлы модулей не найдены${NC}"
    exit 1
fi

echo ""
echo "4. Проверка Docker..."
if command -v docker &> /dev/null; then
    echo -e "${GREEN}✅ Docker установлен${NC}"
else
    echo -e "${RED}❌ Docker не установлен${NC}"
    exit 1
fi

echo ""
echo "5. Запуск PostgreSQL в Docker..."
docker-compose down -v 2>/dev/null
docker-compose up -d postgres
check_status $? "PostgreSQL запущен"

echo ""
echo "6. Ожидание готовности PostgreSQL..."
sleep 10
if docker-compose exec postgres pg_isready -U postgres > /dev/null 2>&1; then
    echo -e "${GREEN}✅ PostgreSQL готов к работе${NC}"
else
    echo -e "${RED}❌ PostgreSQL не готов${NC}"
    exit 1
fi

echo ""
echo "7. Проверка баз данных..."
if docker-compose exec postgres psql -U postgres -lqt | cut -d \| -f 1 | grep -qw product_db; then
    echo -e "${GREEN}✅ База данных product_db создана${NC}"
else
    echo -e "${RED}❌ База данных product_db не создана${NC}"
fi

if docker-compose exec postgres psql -U postgres -lqt | cut -d \| -f 1 | grep -qw payment_db; then
    echo -e "${GREEN}✅ База данных payment_db создана${NC}"
else
    echo -e "${RED}❌ База данных payment_db не создана${NC}"
fi

echo ""
echo "8. Проверка портов..."
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

echo ""
echo "=== Результаты проверки ==="
echo -e "${GREEN}✅ Проект готов к запуску!${NC}"
echo ""
echo "Для запуска сервисов выполните:"
echo "1. cd product-service && mvn spring-boot:run"
echo "2. cd payment-service && mvn spring-boot:run (в новом терминале)"
echo "3. ./test-integration.sh (для тестирования интеграции)"
echo ""
echo "Для остановки PostgreSQL: docker-compose down" 