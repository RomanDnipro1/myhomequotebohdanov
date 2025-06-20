# MyHomeQuote - User Results Management API

Spring Boot REST API для управління результатами користувачів по рівнях.

## Опис

Додаток надає REST API для:
- Збереження результатів користувачів по рівнях
- Отримання топ результатів по користувачах
- Отримання топ результатів по рівнях
- Опціональне обмеження кількості збережених результатів

## Технології

- **Java 11**
- **Spring Boot 2.7.18**
- **Maven**
- **Embedded Tomcat**

## Вимоги

- Java 11 або вище
- Maven 3.6+

## Швидкий старт

### 1. Збірка проекту

```bash
chmod +x build.sh
./build.sh
```

### 2. Запуск додатку

```bash
chmod +x run.sh
./run.sh
```

Або вручну:
```bash
java -jar target/myhomequotebohdanov-1.0-SNAPSHOT.jar
```

Додаток запуститься на порту 8080.

## API Endpoints

### Збереження результату
```
PUT /api/setinfo
Content-Type: application/json

{
  "user_id": 1,
  "level_id": 1,
  "result": 100
}
```

### Отримання результатів користувача
```
GET /api/userinfo/{user_id}
GET /api/userinfo/{user_id}?topSize=10
```

### Отримання результатів рівня
```
GET /api/levelinfo/{level_id}
GET /api/levelinfo/{level_id}?topSize=10
```

## Приклади використання

### Збереження результату
```bash
curl -X PUT "http://localhost:8080/api/setinfo" \
  -H "Content-Type: application/json" \
  -d '{"user_id": 1, "level_id": 1, "result": 100}'
```

### Отримання топ-5 результатів користувача
```bash
curl "http://localhost:8080/api/userinfo/1?topSize=5"
```

### Отримання топ-10 результатів рівня
```bash
curl "http://localhost:8080/api/levelinfo/1?topSize=10"
```

## Особливості реалізації

### Сортування
- Результати сортируються за спаданням (найкращі спочатку)
- При однакових результатах сортування по ID (спадання)

### Продуктивність
- **getTopResultsByUser**: O(k) - оптимізовано для швидкого читання
- **getTopResultsByLevel**: O(n log n + k) - сортування при кожному запиті
- **setResult**: O(n) для user, O(1) для level

### Обмеження результатів
- За замовчуванням: без обмежень
- Можна увімкнути через код сервісу (опціонально)
- Зберігаються тільки топ-N результатів

## Структура проекту

```
src/
├── main/
│   ├── java/com/example/myhomequotebohdanov/
│   │   ├── controller/
│   │   │   └── UserResultController.java
│   │   ├── model/
│   │   │   └── UserResult.java
│   │   ├── service/
│   │   │   └── UserResultService.java
│   │   └── MyhomequoteApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/myhomequotebohdanov/
        └── service/
            └── UserResultServiceTest.java
```

## Тестування

Запуск тестів:
```bash
mvn test
```

## Логування

Для включення детального логування:
```bash
java -jar target/myhomequotebohdanov-1.0-SNAPSHOT.jar \
  --logging.level.com.example.myhomequotebohdanov=DEBUG
```

## Автор

Bohdanov Roman
