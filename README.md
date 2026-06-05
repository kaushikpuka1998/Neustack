# Neustack — E-Commerce Store API

> A take-home assignment: e-commerce backend with cart management, checkout, discount system, user management, and product catalogue — all in-memory, no database needed.

**Stack:** Java 17 · Spring Boot 4 · Gradle · Lombok · Springdoc OpenAPI (Swagger UI) · React + TypeScript (Vite)


## Example:
<img width="1060" height="885" alt="Screenshot 2026-06-06 at 2 50 52 AM" src="https://github.com/user-attachments/assets/8c4a695e-e470-405a-8c0f-5dcd5b98ca23" />
<img width="1074" height="574" alt="Screenshot 2026-06-06 at 2 50 00 AM" src="https://github.com/user-attachments/assets/0279ae73-ab9b-4cca-809c-d60937d25fcf" />
<img width="1113" height="784" alt="Screenshot 2026-06-06 at 2 49 46 AM" src="https://github.com/user-attachments/assets/5cead476-08bf-4464-95d6-2b2d4667896e" />
<img width="1590" height="748" alt="Screenshot 2026-06-06 at 2 47 29 AM" src="https://github.com/user-attachments/assets/4154bb87-3b98-4b24-8e26-b56eae02033e" />
<img width="1733" height="872" alt="Screenshot 2026-06-06 at 2 47 17 AM" src="https://github.com/user-attachments/assets/166632f8-2569-45ca-ba9d-8696c203f23d" />


---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Reference](#api-reference)
  - [Users](#users)
  - [Products](#products)
  - [Cart](#cart)
  - [Checkout](#checkout)
  - [Orders](#orders)
  - [Admin](#admin)
- [Running Tests](#running-tests)
- [Swagger UI](#swagger-ui)
- [Design Decisions](#design-decisions)

---

## Features

- User registration, login, and username availability check
- Product catalogue — add and fetch products
- Per-user cart — add items, view cart, remove items
- Checkout with optional discount coupon code (validated before applying)
- Discount system — every Nth order qualifies for a coupon; admin generates it explicitly
- Order history — fetch any order by ID or all orders for a user
- Admin — generate discount codes, validate coupon codes, view cart totals
- Fully in-memory storage (no database needed)
- Interactive Swagger UI out of the box

---

## Tech Stack

| Layer      | Technology                           |
|------------|--------------------------------------|
| Language   | Java 17                              |
| Framework  | Spring Boot 4.0.6 (Spring MVC)       |
| Build Tool | Gradle 8 (wrapper included)          |
| Utilities  | Lombok                               |
| API Docs   | Springdoc OpenAPI 2.8.5 (Swagger UI) |
| Testing    | JUnit 5 · Spring Boot Test · Mockito |
| Frontend   | React + TypeScript (Vite)            |

---

## Project Structure

```
Neustack/
├── src/
│   ├── main/
│   │   ├── java/com/kgstrivers/neustack/
│   │   │   ├── CONTROLLERS/
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── CartController.java
│   │   │   │   ├── CheckoutController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   └── UserController.java
│   │   │   ├── SERVICES/
│   │   │   │   ├── CartService.java
│   │   │   │   ├── DiscountService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── ProductService.java
│   │   │   │   └── UserService.java
│   │   │   ├── ENTITIES/            # Domain models + request/response DTOs
│   │   │   └── NeustackApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/kgstrivers/neustack/
├── frontend/                        # React + TypeScript (Vite)
├── build.gradle
├── README.md
└── DECISIONS.md
```

---

## Prerequisites

| Tool    | Version | Notes                                    |
|---------|---------|------------------------------------------|
| Java    | 17+     | [Download Temurin](https://adoptium.net) |
| Gradle  | 8+      | Use `./gradlew` — no install needed      |
| Node.js | 18+     | Only needed for the frontend             |

---

## Getting Started

### Backend

```bash
# 1. Clone
git clone https://github.com/kaushikpuka1998/Neustack.git
cd Neustack

# 2. Run (Linux / macOS)
./gradlew bootRun

# 2. Run (Windows)
gradlew.bat bootRun
```

Server starts at → **`http://localhost:8080`**

### Frontend (optional)

```bash
cd frontend
npm install
npm run dev
```

Frontend starts at → **`http://localhost:5173`**

---

## Configuration

Discount policy is configured in `src/main/resources/application.properties`:

```properties
# Every Nth order qualifies for a coupon
discount.nth-order=5

# Discount percentage applied at checkout
discount.percentage=10
```

---

## API Reference

**Base URL:** `http://localhost:8080`

All responses follow a consistent envelope:
```json
{ "success": true, "data": { ... }, "message": null }
{ "success": false, "data": null, "message": "Error description" }
```

---

### Users

#### `POST /users/add` — Register a User

```json
// Request
{ "username": "kaushik", "password": "secret123", "email": "k@example.com" }

// Response 201
{ "success": true, "data": { "userId": "u-001", "username": "kaushik" } }
```

#### `POST /users/check-username` — Check Username Availability

```json
// Request
{ "username": "kaushik" }

// Response 200 — available
{ "success": true, "data": true, "message": "Username available" }

// Response 200 — taken
{ "success": true, "data": false, "message": "Username already taken" }
```

#### `POST /users/login` — Login

```json
// Request
{ "username": "kaushik", "password": "secret123" }

// Response 200
{ "success": true, "data": { "userId": "u-001", "username": "kaushik" } }
```

#### `GET /users/{userId}` — Get User by ID

```json
// Response 200
{ "success": true, "data": { "userId": "u-001", "username": "kaushik" } }
```

#### `GET /users` — Get All Users

```json
// Response 200
{ "success": true, "data": [ { "userId": "u-001", "username": "kaushik" } ] }
```

#### `GET /users/{userId}/orders` — Get All Orders for a User

```json
// Response 200
{ "success": true, "data": [ { "orderId": "ORD-001", "total": 3998.00 } ] }
```

---

### Products

#### `POST /products` — Add a Product

```json
// Request
{ "name": "Wireless Headphones", "price": 1999.00, "description": "Noise cancelling" }

// Response 201
{ "success": true, "data": { "productId": "p-001", "name": "Wireless Headphones", "price": 1999.00 } }
```

#### `GET /products/{id}` — Get Product by ID

```json
// Response 200
{ "success": true, "data": { "productId": "p-001", "name": "Wireless Headphones", "price": 1999.00 } }
```

#### `GET /products` — Get All Products

```json
// Response 200
{ "success": true, "data": [ { "productId": "p-001", "name": "Wireless Headphones", "price": 1999.00 } ] }
```

---

### Cart

#### `POST /cart/add` — Add Item to Cart

```json
// Request
{ "userId": "u-001", "productId": "p-001", "quantity": 2 }

// Response 201
{
  "success": true,
  "data": {
    "cartId": "cart-001",
    "cartItems": [ { "productId": "p-001", "quantity": 2, "price": 1999.00 } ]
  }
}
```

#### `GET /cart/{userId}` — Get Cart Items

```json
// Response 200
{
  "success": true,
  "data": [ { "productId": "p-001", "quantity": 2, "price": 1999.00 } ]
}
```

#### `DELETE /cart/remove` — Remove Item from Cart

```json
// Request
{ "cartId": "cart-001", "productId": "p-001" }

// Response 200
{
  "success": true,
  "data": []
}
```

---

### Checkout

#### `POST /checkout` — Place Order

`discountCode` is optional — omit or pass `null` for a full-price order.

```json
// Request
{ "userId": "u-001", "discountCode": "SAVE10" }

// Response 201 — with valid coupon
{
  "success": true,
  "data": {
    "orderId": "ORD-005",
    "userId": "u-001",
    "totalAmount": 3598.20,
    "discountApplied": 399.80,
    "couponUsed": "SAVE10"
  }
}

// Response 201 — no coupon
{
  "success": true,
  "data": {
    "orderId": "ORD-006",
    "userId": "u-001",
    "totalAmount": 3998.00,
    "discountApplied": 0.0,
    "couponUsed": null
  }
}
```

| Status | Reason |
|--------|--------|
| `500`  | Cart is empty or coupon is invalid / already used |

---

### Orders

#### `GET /orders/{orderId}` — Get Order by ID

```json
// Response 201
{
  "success": true,
  "data": { "orderId": "ORD-005", "userId": "u-001", "totalAmount": 3598.20 }
}
```

---

### Admin

#### `GET /admin/cart/{userId}` — Get Cart Total for a User

```json
// Response 200
{ "success": true, "data": 3998.00 }
```

#### `POST /admin/generate-discount-code` — Generate a Discount Code

Generates a coupon if the Nth-order condition is satisfied.

```json
// Request
{ "everyNthOrder": 5, "percentage": 10 }

// Response 201
{
  "success": true,
  "data": { "code": "SAVE10", "percentage": 10, "status": "ACTIVE" }
}
```

#### `POST /admin/discount-code/` — Validate / Fetch a Discount Code

```json
// Request
{ "code": "SAVE10", "userId": "u-001" }

// Response 200 — valid
{
  "success": true,
  "data": { "code": "SAVE10", "percentage": 10, "status": "ACTIVE" }
}

// Response 500 — invalid or used
{ "success": false, "data": null, "message": "Discount code not found or already used" }
```

---

## Running Tests

```bash
./gradlew test
```

Test coverage includes:

- Nth-order discount trigger (boundary conditions)
- Coupon validation: active, invalid, already-used
- Checkout total calculation with and without discount
- Cart operations: add item, remove item, empty-cart guard
- User and product service logic

HTML test report:

```
build/reports/tests/test/index.html
```

---

## Swagger UI

Start the backend, then open:

```
http://localhost:8080/swagger-ui/index.html
```

Every endpoint is documented interactively — no Postman needed.

OpenAPI JSON spec: `http://localhost:8080/v3/api-docs`

---

## Design Decisions

See [DECISIONS.md](./DECISIONS.md) for a detailed write-up of 7 architectural decisions — each with context, options considered, the choice made, and trade-offs.
