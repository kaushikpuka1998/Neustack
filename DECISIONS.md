# Design Decisions

This document records the key architectural and implementation decisions made while building the Neustack e-commerce API. Each decision includes the problem context, options evaluated, the chosen approach, and the trade-offs considered.

---

## Decision 1: Language and Framework — Java + Spring Boot over TypeScript/Node.js

**Context:**
The assignment listed TypeScript/Node.js as the team's primary stack, but explicitly permitted any technology. I needed to choose between aligning with their stack versus demonstrating deeper skill in a language I use daily.

**Options Considered:**
- **Option A — TypeScript + Express/Fastify:** Aligns with Neustack's primary stack. However, I would have spent significant time on boilerplate and type-safety concerns at the cost of producing thinner business logic and fewer tests.
- **Option B — Java 17 + Spring Boot 4:** My daily driver at work (Guidewire). Enables me to write cleaner layered architecture, richer business logic, and thorough unit tests under time pressure.

**Choice:** Java 17 + Spring Boot 4

**Why:**
The assignment explicitly states "use what lets you demonstrate your skills best." The quality of business logic, test coverage, and architectural decisions matters more than language alignment. Choosing TypeScript under time pressure would have resulted in shallower code. Spring Boot 4 also gave me Swagger UI (via springdoc-openapi) for free, which serves as the Postman collection alternative the FAQ mentions.

**Trade-off:** The reviewer may be less fluent in Java when reading the code, but the layered structure (controller → service → store) is idiomatic across any language.

---

## Decision 2: In-Memory Store — ConcurrentHashMap over Plain HashMap

**Context:**
The assignment required an in-memory solution without a database. Application data such as carts, orders, and discount codes are stored in memory.

**Options Considered:**
- **Option A — Plain HashMap: Simple implementation, but not safe for concurrent access in a multi-threaded Spring Boot application.
- **Option B — `ConcurrentHashMap`: Provides thread-safe access to shared in-memory data structures without requiring explicit synchronization.
- **Option C — Embedded H2 database:** Persistent and transactional, but the spec explicitly says in-memory is fine and a database is not needed. Overkill for an assignment.

**Choice:** Option B

**Why:**
Used  `ConcurrentHashMap` to safely manage shared application state in memory. Since the application serves requests concurrently, ConcurrentHashMap helps prevent data corruption while keeping the implementation lightweight and aligned with the assignment requirements.

---

## Decision 3: Discount Code Generation — Admin-Triggered, Not Auto-Generated at Checkout

**Context:**
Every Nth order is eligible for a coupon. The question was: should the system auto-issue the coupon at the moment of the qualifying checkout, or should the admin explicitly call a generation endpoint?

**Options Considered:**
- **Option A — Auto-generate at checkout:** When the Nth order completes, automatically generate and return a coupon code inline. Simpler UX, but tightly couples the checkout flow with the discount issuance logic.
- **Option B — Admin-triggered via a dedicated endpoint (`POST /api/admin/discount/generate`):** Admin checks if the condition is met and explicitly generates the code. The assignment spec explicitly calls for this admin API.

**Choice:** Option B — Admin-triggered generation

**Why:**
The spec defines a specific admin API for this. Beyond spec compliance, this design is also more realistic: in production e-commerce, discount codes are issued by business logic controlled by administrators, not automatically emitted mid-checkout. It separates concerns cleanly — the checkout flow only validates and applies codes, never creates them. This makes both flows independently testable.

**Trade-off:** Requires the admin to proactively call the endpoint. This is acceptable given the spec's intent.

---

## Decision 4: Coupon Code Lifecycle — One-Time Use with Explicit State

**Context:**
The spec does not explicitly state whether a coupon can be used multiple times. I had to define the coupon lifecycle.

**Options Considered:**
- **Option A — Multi-use codes:** A generated coupon remains valid indefinitely. Simple to implement but allows a single customer to claim the discount on every subsequent order, which defeats the "reward the Nth customer" intent.
- **Option B — One-time use:** After a coupon is successfully applied at checkout, it is marked `USED` and rejected on any subsequent attempt.

**Choice:** Option A — Multiple Time

---

## Decision 5: Layered Architecture — Controller → Service → Store

**Context:**
I needed to decide how to structure the code: flat (logic in controllers) or layered.

**Options Considered:**
- **Option A — Logic directly in controllers:** Faster to write, but business logic becomes untestable without a full HTTP context. Any change to the checkout flow requires understanding the controller.
- **Option B — Controller → Service → Store (repository-like) layers:** Controllers only handle HTTP concerns (request parsing, response shaping). Services hold all business logic. The in-memory store is accessed only via service methods.

**Choice:** Option B — strict layered architecture

**Why:**
The most critical logic in this assignment — the Nth-order counter check, coupon validation, discount calculation — lives in `CheckoutService` and `DiscountService`. By isolating it there, I can unit test it with plain JUnit + Mockito without spinning up a Spring context. When the reviewer asks "how would you add a database?" the answer is: swap the in-memory store implementation — nothing else changes. The controller and service layers are untouched.

---

## Decision 6: Discount Percentage and N-Value — Configurable via api service

**Context:**
The assignment specifies "every Nth order gets x% discount" but does not fix the values of N or x. I needed to decide whether to hardcode them or make them configurable.

**Options Considered:**
- **Option A — Hardcoded values:** Faster to write, but any change to the discount policy requires a code change and recompile.
- **Option B — Externalized to API Controller.

**Choice:** Option B — Externalized to API Controller

**Why:**
Discount configuration is externalized to a dedicated API service, allowing discount rules and code generation policies to be managed centrally without requiring application redeployment. This ensures consistency, flexibility, and a single source of truth across all consuming services.


---

## Decision 7: API Documentation — Swagger UI via Springdoc OpenAPI

**Context:**
The assignment FAQ says: "If no frontend, provide Postman collection or similar." I built a frontend, but needed a reliable way to document and test all APIs independently.

**Options Considered:**
- **Option A — Postman collection JSON file:** Manually maintained, can drift from the actual implementation.
- **Option B — Swagger UI via `springdoc-openapi-starter-webmvc-ui`:** Auto-generates interactive documentation from the running application's controller annotations. Always in sync with the code.

**Choice:** Option B — Swagger UI

**Why:**
Swagger UI is automatically available at `http://localhost:8080/swagger-ui/index.html` once the server starts. Every endpoint, request body, and response schema is documented without any manual effort. It also serves as a live interactive testing tool — the reviewer can call any API directly from the browser without needing Postman installed. This is a strictly better developer experience than a static JSON collection, and it never goes stale.

---

## Summary Table

| # | Decision | Choice |
|---|----------|--------|
| 1 | Language/framework | Java 17 + Spring Boot 4 |
| 2 | Coupon generation trigger | Admin-triggered via dedicated endpoint |
| 3 |Code structure | Controller → Service → Store layering |
| 4 | Code structure | Controller → Service → Store layering |
| 5 | Discount config (N, x%) | Externalized to exposed by API |
| 6 | API documentation | Swagger UI via Springdoc OpenAPI |
