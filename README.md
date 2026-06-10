# FoodNote - Full Stack Food Sharing Platform

## Project Overview

**FoodNote** is a full-stack web application for exploring, sharing, and discovering food experiences. It provides users with tools to publish recipe posts, track dietary history, manage nutritional data, and engage with content through comments, likes, and views. The platform includes a dedicated admin panel for content moderation and data visualization.

---

## Demo
[![Demo Video](https://img.youtube.com/vi/yIlt_EJRIQU/0.jpg)](https://youtu.be/yIlt_EJRIQU)

---

## Key Features

- User registration, login, and JWT-based authentication
- Create, edit, and delete food posts and recipes (Cookbook, Gourmet)
- Browse and search food categories
- Nutritional information tracking per recipe
- Dietary history logging and analysis
- Comment system with nested (parent/child) replies
- Like and view interaction tracking
- Data export via Excel (EasyExcel / Apache POI)
- Admin dashboard with role-based access control
- Data visualization for administrators and regular users

---

## Technology Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.2, REST API |
| ORM | MyBatis 3 |
| Authentication | JWT (jjwt 0.11) |
| Frontend | Vue.js, HTML, CSS, JavaScript |
| Database | MySQL |
| Export | EasyExcel, Apache POI, iText PDF |
| Build Tool | Maven |
| Utilities | Lombok, Apache Commons Lang3, Fastjson2 |

---

## Technology Stack Details

### Backend

**Spring Boot 3.2 / Java 17**
The backend is built on Spring Boot 3.2 with Java 17, using the auto-configuration model to wire up the web server, database connection pool, and middleware. The layered package structure (controller / service / mapper) enforces a clean separation between HTTP handling, business logic, and data access.

**MyBatis 3**
MyBatis is used as the persistence layer. SQL statements are maintained in XML mapper files, giving full control over query design while keeping the service layer free of raw SQL. Custom `QueryDto` objects carry pagination and filter parameters, which are translated into SQL `LIMIT`/`OFFSET` clauses at the mapper level.

**Spring AOP**
Two custom aspects handle cross-cutting concerns without polluting controller or service code:
- `@Pager` — intercepts any service method annotated with it, automatically converts the incoming `current` page number into a SQL-compatible offset before the query executes.
- `@Protector` — validates the JWT token and enforces role-based access control (user vs. admin) on individual endpoints, short-circuiting the call and returning an error response if authorization fails.

**JWT Authentication (jjwt 0.11)**
Stateless authentication is implemented with JSON Web Tokens. On login the server signs a token containing the user ID and role. A `HandlerInterceptor` (`JwtInterceptor`) runs before every protected request, parses the token, and stores the resolved identity in a `ThreadLocal` holder (`LocalThreadHolder`) so any downstream service method can read the current user without an additional database call.

**Data Export: EasyExcel, Apache POI, iText**
The admin panel supports exporting records to Excel. EasyExcel (Alibaba, 3.3.2) is used for streaming large datasets with low memory overhead. Apache POI 5.2.5 handles programmatic workbook manipulation where fine-grained control is needed. iText 5 provides PDF generation for printable reports.

**Fastjson2**
Alibaba Fastjson2 handles JSON serialization in scenarios where the standard Jackson integration is bypassed — primarily when writing error responses directly to the `HttpServletResponse` writer inside the JWT interceptor.

---

### Frontend

**Vue.js 2.6 (Options API) + Vue Router**
The frontend is a single-page application built with Vue 2.6 using the Options API. Vue Router 3.2 manages client-side navigation between the user portal and the admin panel, with route guards checking the decoded JWT token before allowing access to protected pages.

**Element UI 2.15**
Element UI provides the component library — tables, forms, dialogs, pagination controls, and layout grids. The admin management views (user, cookbook, category, interaction, diet history, nutriment) are built almost entirely with Element UI data tables and form components.

**ECharts 4.8**
Apache ECharts powers the data visualization layer. Three reusable chart components are built as Vue wrappers: `BarChart.vue`, `PieChart.vue`, and `LineChart.vue`. A `ResizeObserver` is attached in each component's `mounted` hook so charts reflow automatically when the browser window changes size.

**WangEditor 5 (@wangeditor/editor)**
Rich text content for food posts and gourmet entries is authored through WangEditor 5, a lightweight WYSIWYG editor with image upload support. The `Editor.vue` component wraps the editor-for-vue adapter and emits content changes back to the parent form as a plain HTML string.

**Axios**
All API communication uses Axios 0.21. A shared Axios instance is configured with the backend base URL and a request interceptor that attaches the stored JWT token to every outbound request header automatically.

**Security Utilities: crypto-js, js-md5, sm4util**
Passwords are hashed with MD5 (`js-md5`) before transmission. Additional field-level encryption uses SM4 (`sm4util`), China's national symmetric cipher standard, to protect sensitive data in transit. `crypto-js` provides supplementary cryptographic primitives where needed.

**SweetAlert2**
User-facing confirmation dialogs and success / error toasts are rendered with SweetAlert2 via the `vue-sweetalert2` wrapper, replacing the browser's native `alert` and `confirm` with styled, accessible modals.

---

## Architecture Overview

The project follows a layered architecture with clear separation of concerns:

```
Frontend (Vue.js)
      |
      v
  Controller Layer       -- Maps HTTP requests to service calls
      |
      v
  Service Layer          -- Business logic, validation, orchestration
      |
      v
  DAO / Mapper Layer     -- MyBatis mappers for database access
      |
      v
  Database (MySQL)
```

**Cross-cutting concerns:**

- **AOP** (`@Pager`, `@Protector`) for pagination injection and access control
- **JWT Interceptor** validates tokens on protected routes before reaching controllers
- **DTO / Query Objects** decouple HTTP input from domain entities
- **VO (View Objects)** shape response payloads for the frontend, including nested relations
- **ThreadLocal context** (`LocalThreadHolder`) carries the authenticated user identity per request

---

## Project Structure

```
food-note-fullstack/
├── backend-api/               # Spring Boot application
│   └── src/main/java/
│       └── cn/kmbeast/
│           ├── controller/    # REST controllers
│           ├── service/       # Business logic interfaces and implementations
│           ├── mapper/        # MyBatis mapper interfaces
│           ├── pojo/
│           │   ├── entity/    # Database entity classes
│           │   ├── dto/       # Data transfer objects (input)
│           │   ├── vo/        # View objects (output)
│           │   └── em/        # Enumerations (roles, audit status, etc.)
│           ├── aop/           # Custom aspects for pagination and protection
│           ├── Interceptor/   # JWT authentication interceptor
│           ├── config/        # Spring MVC and interceptor configuration
│           └── utils/         # Utility classes
├── frontend-view/             # Vue.js single-page application
├── sql/                       # Database schema and seed scripts
└── README.md
```

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Node.js 16 or higher
- MySQL 8.0 or higher
- Maven 3.8 or higher

### Backend Setup

1. Create a MySQL database and run the scripts in `sql/`.
2. Configure database credentials in `backend-api/src/main/resources/application.yml`.
3. Build and run the Spring Boot application:

```bash
cd backend-api
mvn clean package
java -jar target/food-share-api-1.0-SNAPSHOT.jar
```

The backend starts on `http://localhost:8080` by default.

### Frontend Setup

```bash
cd frontend-view
npm install
npm run serve
```

The frontend development server starts on `http://localhost:8081` by default.

---

## API Design

All endpoints follow RESTful conventions and return a unified response envelope:

```json
{
  "code": 200,
  "message": "success",
  "data": { }
}
```

Paginated responses use a `PageResult` wrapper that includes total count and the current page of records. Protected routes require a valid JWT token passed in the request header.

