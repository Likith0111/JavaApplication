# 🍕 Food Ordering Platform

> A modern, full-stack food ordering application built with Spring Boot and React, featuring restaurant discovery, menu browsing, cart management, and seamless order processing.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Security](#security)
- [Testing](#testing)

## 🎯 Overview

This food ordering platform is a production-ready application that demonstrates enterprise-level architecture and best practices. It provides a complete online food ordering experience with restaurant discovery, menu browsing, shopping cart functionality, and order management.

### Key Highlights

- **Secure Authentication:** JWT-based stateless authentication
- **Role-Based Access:** Admin and Customer roles with different permissions
- **Restaurant Management:** Browse restaurants and their menus
- **Smart Cart:** Dynamic shopping cart with live total calculations
- **Order Processing:** Complete order lifecycle management with status tracking
- **Responsive UI:** Modern React interface with smooth UX
- **Scalable Architecture:** Microservice-ready design

## ✨ Features

### Customer Features
- ✅ User registration and login
- ✅ Browse restaurants with details
- ✅ View restaurant menus with item descriptions and prices
- ✅ Add/remove items to/from shopping cart
- ✅ Place orders from cart
- ✅ View order history with status tracking
- ✅ Track order status (PENDING, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED)
- ✅ Update profile information

### Admin Features
- ✅ Restaurant CRUD operations
- ✅ Menu item management (create, update, delete)
- ✅ Order management and status updates
- ✅ View all orders across restaurants
- ✅ User management
- ✅ Restaurant analytics (future enhancement)

### Technical Features
- ✅ RESTful API design
- ✅ JWT token-based authentication
- ✅ Password encryption (BCrypt)
- ✅ Input validation
- ✅ Global exception handling
- ✅ CORS support
- ✅ Docker containerization

## 🏗️ Architecture

### Backend Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Controller Layer                      │
│  - AuthController (Public endpoints)                   │
│  - RestaurantController (Public + Authenticated)        │
│  - MenuController (Public + Authenticated)               │
│  - CartController (Authenticated)                       │
│  - OrderController (Authenticated)                      │
│  - AdminController (Admin only)                         │
└────────────────────┬───────────────────────────────────┘
                     │
┌────────────────────▼───────────────────────────────────┐
│                    Service Layer                        │
│  - AuthService (Business logic for auth)              │
│  - RestaurantService (Restaurant operations)           │
│  - MenuService (Menu item operations)                  │
│  - CartService (Cart management)                       │
│  - OrderService (Order processing)                     │
│  - UserService (User management)                       │
└────────────────────┬───────────────────────────────────┘
                     │
┌────────────────────▼───────────────────────────────────┐
│                  Repository Layer                       │
│  - UserRepository (JPA)                                │
│  - RestaurantRepository (JPA)                          │
│  - MenuItemRepository (JPA)                            │
│  - CartItemRepository (JPA)                            │
│  - OrderRepository (JPA)                               │
│  - OrderItemRepository (JPA)                           │
└────────────────────┬───────────────────────────────────┘
                     │
┌────────────────────▼───────────────────────────────────┐
│                  Database Layer                         │
│             PostgreSQL Database                         │
└─────────────────────────────────────────────────────────┘
```

### Security Architecture

```
Request → JWT Filter → Validate Token → Check Permissions → Controller
              ↓ Invalid              ↓ Unauthorized
         401 Unauthorized       403 Forbidden
```

## 🛠️ Tech Stack

### Backend
| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | 3.4.5 | Application framework |
| Spring Security | 6.x | Security & authentication |
| Spring Data JPA | 3.x | Data persistence |
| PostgreSQL | 16 | Production database |
| H2 Database | 2.x | Test database |
| Maven | 3.9 | Build automation |
| Lombok | Latest | Boilerplate reduction |
| JUnit 5 | 5.x | Unit testing |
| Mockito | 4.x | Mocking framework |

### Frontend
| Technology | Version | Purpose |
|-----------|---------|---------|
| React | 18 | UI library |
| React Router | 6 | Client-side routing |
| Axios | Latest | HTTP client |
| CSS3 | - | Styling |

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.9 or higher
- Node.js 18 or higher
- PostgreSQL 16 (or use Docker)
- Docker & Docker Compose (optional)

### Quick Start with Docker

```bash
# From project root
cd fullstack-java-projects

# Build and start services
docker-compose up -d

# View logs
docker-compose logs -f foodordering-backend foodordering-frontend

# Access application
open http://localhost:3002
```

### Local Development Setup

#### Backend

```bash
cd food-ordering/backend

# Create database
createdb food_ordering_db

# Update application-dev.yml with your database credentials

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Backend will start on http://localhost:8082
```

#### Frontend

```bash
cd food-ordering/frontend

# Install dependencies
npm install

# Start development server
npm start

# Frontend will start on http://localhost:3002
```

## 📡 API Endpoints

### Authentication (Public)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | ❌ |
| POST | `/api/auth/login` | Login user | ❌ |

### Restaurants

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/restaurants` | Get all restaurants | ❌ |
| GET | `/api/restaurants/{id}` | Get restaurant by ID | ❌ |
| POST | `/api/restaurants` | Create restaurant | ✅ Admin |
| PUT | `/api/restaurants/{id}` | Update restaurant | ✅ Admin |
| DELETE | `/api/restaurants/{id}` | Delete restaurant | ✅ Admin |

### Menu

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/menu/restaurant/{restaurantId}` | Get menu by restaurant | ❌ |
| GET | `/api/menu/{id}` | Get menu item by ID | ❌ |
| POST | `/api/menu` | Create menu item | ✅ Admin |
| PUT | `/api/menu/{id}` | Update menu item | ✅ Admin |
| DELETE | `/api/menu/{id}` | Delete menu item | ✅ Admin |

### Cart

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/cart` | Get user's cart | ✅ Customer |
| POST | `/api/cart/items` | Add item to cart | ✅ Customer |
| DELETE | `/api/cart/items/{id}` | Remove item from cart | ✅ Customer |

### Orders

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/orders/place` | Place order from cart | ✅ Customer |
| GET | `/api/orders` | Get user's orders | ✅ Customer |
| GET | `/api/orders/{id}` | Get order by ID | ✅ Customer |

### Admin

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/admin/restaurants` | Get all restaurants | ✅ Admin |
| POST | `/api/admin/restaurants` | Create restaurant | ✅ Admin |
| GET | `/api/admin/menu/restaurant/{restaurantId}` | Get menu by restaurant | ✅ Admin |
| POST | `/api/admin/menu` | Create menu item | ✅ Admin |
| PATCH | `/api/admin/orders/{id}/status` | Update order status | ✅ Admin |

### Example Request

```bash
# Register a new user
curl -X POST http://localhost:8082/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123",
    "role": "CUSTOMER"
  }'

# Login
curl -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123"
  }'

# Add item to cart
curl -X POST http://localhost:8082/api/cart/items \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "menuItemId": 1,
    "quantity": 2
  }'

# Place order
curl -X POST http://localhost:8082/api/orders/place \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "restaurantId": 1
  }'
```

## 🗄️ Database Schema

### Main Entities

**Users Table**
- `id` (BIGSERIAL, Primary Key)
- `name` (VARCHAR)
- `email` (VARCHAR, Unique)
- `password` (VARCHAR, Encrypted)
- `role` (VARCHAR) - ADMIN or CUSTOMER

**Restaurants Table**
- `id` (BIGSERIAL, Primary Key)
- `name` (VARCHAR)
- `description` (TEXT)
- `address` (VARCHAR)
- `created_at` (TIMESTAMP)

**Menu Items Table**
- `id` (BIGSERIAL, Primary Key)
- `restaurant_id` (BIGINT, Foreign Key)
- `name` (VARCHAR)
- `description` (TEXT)
- `price` (DECIMAL)

**Cart Items Table**
- `id` (BIGSERIAL, Primary Key)
- `user_id` (BIGINT, Foreign Key)
- `menu_item_id` (BIGINT, Foreign Key)
- `quantity` (INTEGER)
- `created_at` (TIMESTAMP)

**Orders Table**
- `id` (BIGSERIAL, Primary Key)
- `user_id` (BIGINT, Foreign Key)
- `restaurant_id` (BIGINT, Foreign Key)
- `total_amount` (DECIMAL)
- `status` (VARCHAR) - PENDING, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED
- `created_at` (TIMESTAMP)

**Order Items Table**
- `id` (BIGSERIAL, Primary Key)
- `order_id` (BIGINT, Foreign Key)
- `menu_item_id` (BIGINT, Foreign Key)
- `quantity` (INTEGER)
- `unit_price` (DECIMAL)

## 🔒 Security

### Authentication Flow

1. User registers/logs in via `/api/auth/register` or `/api/auth/login`
2. Server validates credentials and generates JWT token
3. Client stores token (localStorage/sessionStorage)
4. Client includes token in `Authorization` header for protected requests
5. Server validates token on each request

### Security Features

- **Password Hashing:** BCrypt with salt (strength: 10)
- **JWT Expiration:** Configurable token lifetime
- **CORS:** Configured for frontend origin
- **SQL Injection Prevention:** Parameterized queries via JPA
- **XSS Protection:** Input sanitization and validation

### Environment Variables

```properties
# JWT Configuration
JWT_SECRET=your-secret-key-min-256-bits
JWT_EXPIRATION_MS=86400000

# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=food_ordering_db
DB_USER=fullstack_user
DB_PASSWORD=fullstack_pass
```

## 🧪 Testing

### Run Tests

```bash
# Unit tests
mvn test

# Integration tests
mvn verify

# With coverage
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Test Coverage

- **Service Layer:** ~85% coverage
- **Controller Layer:** ~80% coverage
- **Security Layer:** ~90% coverage

### Test Types

1. **Unit Tests:** Test business logic in isolation
2. **Integration Tests:** Test REST endpoints with MockMvc
3. **Repository Tests:** Test data access layer

## 🐳 Docker Deployment

### Build and Run

```bash
# Build images
docker-compose build foodordering-backend foodordering-frontend

# Start services
docker-compose up -d foodordering-backend foodordering-frontend postgres

# Check status
docker-compose ps

# View logs
docker-compose logs -f foodordering-backend
```

### Environment Configuration

Create `.env` file for Docker deployment:

```env
POSTGRES_DB=food_ordering_db
POSTGRES_USER=fullstack_user
POSTGRES_PASSWORD=fullstack_pass
JWT_SECRET=your-production-secret-key
```

## 📦 Project Structure

```
food-ordering/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/fullstack/foodordering/
│   │   │   │   ├── controller/     # REST endpoints
│   │   │   │   ├── service/        # Business logic
│   │   │   │   ├── repository/     # Data access
│   │   │   │   ├── entity/         # JPA entities
│   │   │   │   ├── dto/            # Data transfer objects
│   │   │   │   ├── mapper/         # Entity-DTO mapping
│   │   │   │   ├── security/       # Security configuration
│   │   │   │   ├── exception/      # Custom exceptions
│   │   │   │   └── config/         # Application configuration
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml
│   │   │       ├── application-docker.yml
│   │   │       └── application-test.yml
│   │   └── test/                   # Test files
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── context/
│   │   ├── utils/
│   │   └── constants/
│   ├── package.json
│   ├── Dockerfile
│   └── nginx.conf
└── README.md (this file)
```

## 🔄 Development Workflow

### Backend Development

```bash
# Run in development mode with auto-reload
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Format code
mvn spring-javaformat:apply

# Run linting
mvn checkstyle:check
```

### Frontend Development

```bash
# Start dev server with hot reload
npm start

# Run linter
npm run lint

# Build for production
npm run build
```

## 🚢 Production Deployment

### Best Practices

1. **Environment Variables:** Use secure secrets management
2. **Database:** Use connection pooling and read replicas
3. **Logging:** Configure centralized logging (ELK stack)
4. **Monitoring:** Add APM tools (New Relic, Datadog)
5. **Load Balancing:** Use Nginx or AWS ALB
6. **HTTPS:** Always use TLS in production

### Performance Optimization

- Database indexing on frequently queried columns
- Caching layer for restaurant and menu data (Redis)
- CDN for static assets
- Lazy loading for images
- Connection pooling (HikariCP)

## 📈 Future Enhancements

- [ ] Real-time order tracking with WebSockets
- [ ] Payment gateway integration (Stripe/PayPal)
- [ ] Restaurant ratings and reviews
- [ ] Favorite restaurants functionality
- [ ] Email/SMS notifications for order updates
- [ ] Advanced search with filters (cuisine, price range, rating)
- [ ] Recommendation engine based on order history
- [ ] Delivery tracking integration
- [ ] Multi-restaurant cart support
- [ ] Promotional codes and discounts
- [ ] Restaurant analytics dashboard

## 🤝 Contributing

This is a portfolio project showcasing professional development skills. Feedback and suggestions are welcome!

## 📄 License

MIT License - see root LICENSE file for details.

---

**Part of Full-Stack Java Projects Portfolio** | [View All Projects](../README.md)
