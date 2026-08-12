# StoreFlow

A lightweight inventory management application designed for small business owners to efficiently manage stock levels, process sales, and maintain organized product catalogs. Built with **Spring Boot**, **Spring Security**, and **PostgreSQL** following enterprise-grade architecture patterns.

![Login page](Screenshots/Login.jpg)
![Stock overview](Screenshots/Dashboard.jpg)



---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot |
| Security | Spring Security + BCrypt |
| Data Access | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Frontend | Thymeleaf + HTML/CSS |
| Build Tool | Maven |

---

## Features

- **Add, update, and delete inventory items** with persistent storage
- **Smart duplicate handling** — adding an existing item updates quantity instead of creating a duplicate
- **Transaction processing** — sell items by quantity with validation for out-of-stock and insufficient inventory scenarios
- **Search capability** — filter items by name with optional category refinement
- **Visual indicators** — amber highlights for low stock (<5 units), red for out of stock
- **Robust validation** — multi-layer input validation at browser and server levels with user-friendly feedback
- **Enterprise security** — BCrypt password hashing with Spring Security authentication
- **Global exception handling** — centralized error management via `@ControllerAdvice`

---

## Project Structure

```
src/main/java/com/victor/inventorymanagementweb/
├── controllers/
│   ├── InventoryController.java    # Request handling, flash messages, redirects
│   ├── LoginController.java        # Serves login page on GET /login
│   └── GlobalExceptionHandler.java # Catches MethodArgumentTypeMismatchException and general exceptions
├── models/
│   ├── Item.java                   # JPA entity → items table
│   └── OperationResult.java        # Enum for all service outcomes
├── repository/
│   └── ItemRepository.java         # Spring Data JPA — derived queries
├── security/
│   └── SecurityConfig.java         # Filter chain, BCrypt bean, InMemoryUserDetailsManager
└── services/
    └── InventoryService.java       # All business logic and validation
```

---

## Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/VictorStefanS/StoreFlow-Inventory-Management-System.git
cd StoreFlow-Inventory-Management-System
```

2. Create the database:
```sql
CREATE DATABASE inventorydb;
```

3. Configure application properties:
```bash
cp inventory-management-web/src/main/resources/application.properties.example \
   inventory-management-web/src/main/resources/application.properties
```

4. Set environment variables in `application.properties` for database connection and credentials.

5. Build and run:
```bash
cd inventory-management-web
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

---

## Testing

Manual test cases are documented in [TEST_CASES.md](inventory-management-web/TEST_CASES.md) — covering type mismatches, boundary conditions, and critical workflow scenarios.
