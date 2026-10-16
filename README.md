# 🛒 Retail Inventory Management System

A full-stack, enterprise-grade **Retail Inventory Management System** built with **Spring Boot**, **Spring Data JPA**, **Thymeleaf**, and **MySQL**, styled with **Bootstrap 5**.

Designed for college academic evaluation (**FA 1 & FA 2**), implementing clean layered architecture, Object-Oriented Programming (OOP) design patterns, server-side validation, and global exception handling.

---

## 🛠️ Tech Stack

- **Backend Framework:** Spring Boot 3.2.5 (Java 17+)
- **ORM / Persistence:** Spring Data JPA / Hibernate
- **Database:** MySQL 8.x (`retail_inventory_db`)
- **Template Engine:** Thymeleaf
- **UI & Styling:** Bootstrap 5 (CDN)
- **Build Tool:** Apache Maven (with Maven Wrapper `mvnw.cmd` / `./mvnw`)

---

## ✨ Features (FA 1 & FA 2)

### 🔹 FA 1: Foundation & Core Inventory Management
- **Product Catalog & Dashboard:** Displays all products in a structured Bootstrap table showing SKU, Product Name, Category, Unit Price (₹), Quantity, Total Value (₹), and Stock Status.
- **Dynamic Inventory Valuation:** Computes `getTotalValue()` (`unitPrice * quantity`) on the fly, eliminating redundant database storage.
- **Stock Status Badges:** Visual indicators highlighting stock health:
  - 🟢 **In Stock** badge when healthy.
  - 🔴 **Low Stock** badge when `quantity <= reorderLevel`.
- **KPI Summary Cards:** Dashboard metrics showing Total Products count, Total Inventory Valuation (₹), and Active Low Stock Alerts.
- **Database Auto-Provisioning:** Auto-creates database (`createDatabaseIfNotExist=true`) and updates schema (`ddl-auto=update`).

### 🔹 FA 2: OOP Principles, Advanced Modules & Robustness
- **Object-Oriented Programming (OOP) Architecture:**
  - **Inheritance:** `@MappedSuperclass BaseItem` defines common retail attributes (`productName`, `skuCode`, `category`). `Product` extends `BaseItem` using constructor chaining (`super(...)`).
  - **Encapsulation:** Private attributes in `Product` (`id`, `unitPrice`, `quantity`, `reorderLevel`) accessed via public getters, setters, and business helper methods (`getTotalValue()`, `isLowStock()`).
  - **Abstraction:** `ProductService` interface defines the business operations contract, decoupling web controllers from database implementation.
  - **Polymorphism / Method Overriding:** `ProductServiceImpl` provides concrete implementations of the interface methods (`@Override`).
- **Search Module:** Case-insensitive search bar filtering inventory by **Product Name** or **Category** using custom JPQL (`GET /products?keyword=...`).
- **Edit & Restock Module:** Update item pricing, reorder thresholds, or increment warehouse stock (`GET /products/edit/{id}` & `POST /products/update/{id}`).
- **Delete Module:** Remove discontinued products with client-side JavaScript confirmation dialog (`GET /products/delete/{id}`).
- **Input Validation:**
  - Validates that `productName`, `skuCode`, and `category` are not blank.
  - Ensures `unitPrice > 0`.
  - Ensures `quantity >= 0` and `reorderLevel >= 0`.
  - Displays red Bootstrap alert banners (`<div class="alert alert-danger">`) with exact error explanations if validation fails.
- **Global Exception Handling:**
  - Custom `ProductNotFoundException`.
  - `@ControllerAdvice GlobalExceptionHandler` intercepts missing ID lookups and unexpected database exceptions, rendering a clean `error.html` page with a **"Back to Inventory Dashboard"** button.

---

## 🏛️ System Architecture (MVC)

```mermaid
flowchart TD
    subgraph Client Layer
        Browser["🌐 Web Browser"]
    end

    subgraph Presentation & Controller Layer
        PC["ProductController.java (@Controller)"]
        GEH["GlobalExceptionHandler.java (@ControllerAdvice)"]
        PNF["ProductNotFoundException.java"]
        GEH -->|Catches| PNF
        GEH -->|Renders| ERR["error.html"]
    end

    subgraph Service Layer (Abstraction & Polymorphism)
        PSI["ProductService.java (Interface)"]
        PSIMPL["ProductServiceImpl.java (@Service)"]
        PSI -->|Implemented by| PSIMPL
    end

    subgraph Data Access Layer
        PR["ProductRepository.java (@Repository)"]
        DB[("🗄️ MySQL Database\nretail_inventory_db")]
    end

    subgraph Views (Thymeleaf + Bootstrap 5)
        V1["products-list.html (Dashboard + KPI + Search)"]
        V2["add-product.html (Validation Alerts)"]
        V3["edit-product.html (Restock & Edit)"]
    end

    Browser -->|HTTP Request| PC
    PC --> PSI
    PSIMPL --> PR
    PR -->|Hibernate / JPA| DB
    PC --> V1
    PC --> V2
    PC --> V3
```

---

## 📁 Directory Structure

```
retail-inventory/
├── .mvn/                                      # Maven Wrapper files
├── src/
│   └── main/
│       ├── java/com/college/inventory/
│       │   ├── RetailInventoryApplication.java    # Spring Boot Main Entry Point
│       │   ├── controller/
│       │   │   └── ProductController.java         # MVC Controller (Endpoints & Validation)
│       │   ├── entity/
│       │   │   ├── BaseItem.java                  # @MappedSuperclass - Inheritance
│       │   │   └── Product.java                   # JPA Entity with Domain Helpers
│       │   ├── exception/
│       │   │   ├── GlobalExceptionHandler.java    # @ControllerAdvice Exception Interceptor
│       │   │   └── ProductNotFoundException.java  # Custom Unchecked Exception
│       │   ├── repository/
│       │   │   └── ProductRepository.java         # Spring Data JPA with JPQL Search
│       │   └── service/
│       │       ├── ProductService.java            # Service Interface (Abstraction)
│       │       └── impl/
│       │           └── ProductServiceImpl.java    # Concrete Implementation (Polymorphism)
│       └── resources/
│           ├── application.properties             # Database connection & Hibernate config
│           └── templates/
│               ├── add-product.html               # Add Product Form
│               ├── edit-product.html              # Edit & Restock Form
│               ├── error.html                     # Custom Error Page
│               └── products-list.html             # Dashboard with Table & Search
├── .gitignore                                 # Git Ignore rules
├── mvnw                                       # Maven Wrapper (Linux/Mac)
├── mvnw.cmd                                   # Maven Wrapper (Windows)
├── pom.xml                                    # Maven Dependencies
└── README.md                                  # Project Documentation
```

---

## 🚀 Getting Started Locally

### Prerequisites
- **Java Development Kit (JDK):** Version 17 or higher
- **MySQL Server:** Running on `localhost:3306`

---

### Step 1: Database Setup
The database is configured with `createDatabaseIfNotExist=true` in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/retail_inventory_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=admin@2026
```
*(MySQL will automatically create `retail_inventory_db` on first run!)*

---

### Step 2: Run the Application

Using the included Maven Wrapper:

- **On Windows (PowerShell / Command Prompt):**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```

- **On macOS / Linux:**
  ```bash
  ./mvnw spring-boot:run
  ```

---

### Step 3: Open in Browser

Open your browser and navigate to:
👉 **[http://localhost:8080](http://localhost:8080)** (automatically redirects to `/products`)

---

## 📌 Endpoint Mappings Reference

| HTTP Method | URL Path | Action / Purpose | View Rendered |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Root URL redirect | Redirects to `/products` |
| `GET` | `/products` | Inventory Dashboard with KPI cards & Search | `products-list.html` |
| `GET` | `/products?keyword=...` | Search products by Name or Category | `products-list.html` |
| `GET` | `/products/add` | Display blank product registration form | `add-product.html` |
| `POST` | `/products/add` | Validate and insert new product | Redirects to `/products` |
| `GET` | `/products/edit/{id}` | Display edit form with pre-filled details | `edit-product.html` |
| `POST` | `/products/update/{id}` | Validate and update product / restock | Redirects to `/products` |
| `GET` | `/products/delete/{id}` | Delete product by ID | Redirects to `/products` |

---

## 🎓 Viva Key Takeaways

1. **Inheritance & `@MappedSuperclass`:**
   `BaseItem` is designated as `@MappedSuperclass`. It abstracts common retail fields (`productName`, `skuCode`, `category`) without generating a separate database table. Hibernate incorporates these columns directly into the `products` table.
2. **Abstraction & Loose Coupling:**
   `ProductController` relies on the `ProductService` interface rather than `ProductServiceImpl`. This adheres to the Dependency Inversion Principle, facilitating unit testing and swappable business logic.
3. **Encapsulation & Helper Methods:**
   `getTotalValue()` and `isLowStock()` encapsulate business computation directly inside `Product.java`. This guarantees data integrity and prevents derived state from being stored redundantly in the database.
4. **Custom JPQL Search:**
   `ProductRepository` implements `@Query` using JPQL with `LOWER()` and `LIKE` to perform case-insensitive partial searches across both inherited and child fields.
5. **Dual-Layer Validation & Global Exception Handling:**
   User errors trigger on-form red Bootstrap alerts, while system/ID errors are caught globally by `@ControllerAdvice` (`GlobalExceptionHandler`) to display a graceful `error.html` page.
