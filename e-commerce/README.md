# 🛒 E-Commerce Platform

> A modern, full-stack e-commerce application built with Spring Boot and React, featuring product management, shopping cart functionality, and secure order processing.

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

This e-commerce platform is a production-ready application that demonstrates enterprise-level architecture and best practices. It provides a complete online shopping experience with user authentication, product catalog, shopping cart, and order management.

### Key Highlights

- **Secure Authentication:** JWT-based stateless authentication
- **Role-Based Access:** Admin and Customer roles with different permissions
- **Real-Time Cart:** Dynamic shopping cart with live total calculations
- **Order Processing:** Complete order lifecycle management
- **Responsive UI:** Modern React interface with smooth UX
- **Scalable Architecture:** Microservice-ready design

## ✨ Features

### Customer Features
- ✅ User registration and login
- ✅ Browse product catalog with search
- ✅ Add/remove items to/from shopping cart
- ✅ Place orders with order tracking
- ✅ View order history
- ✅ Update profile information

### Admin Features
- ✅ Product CRUD operations
- ✅ Category management
- ✅ Order management and status updates
- ✅ User management
- ✅ Inventory tracking
- ✅ Sales analytics (future enhancement)

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
│  - ProductController (Public + Authenticated)           │
│  - OrderController (Authenticated)                      │
│  - AdminController (Admin only)                         │
└────────────────────┬───────────────────────────────────┘
                     │
┌────────────────────▼───────────────────────────────────┐
│                    Service Layer                        │
│  - AuthService (Business logic for auth)              │
│  - ProductService (Product operations)                 │
│  - OrderService (Order processing)                     │
│  - UserService (User management)                       │
└────────────────────┬───────────────────────────────────┘
                     │
┌────────────────────▼───────────────────────────────────┐
│                  Repository Layer                       │
│  - UserRepository (JPA)                                │
│  - ProductRepository (JPA)                             │
│  - OrderRepository (JPA)                               │
│  - CategoryRepository (JPA)                            │
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
docker-compose logs -f ecommerce-backend ecommerce-frontend

# Access application
open http://localhost:3001
```

### Local Development Setup

#### Backend

```bash
cd e-commerce/backend

# Create database
createdb ecommerce_db

# Update application-dev.yml with your database credentials

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Backend will start on http://localhost:8081
```

#### Frontend

```bash
cd e-commerce/frontend

# Install dependencies
npm install

# Start development server
npm start

# Frontend will start on http://localhost:3001
```

## 📡 API Endpoints

### Authentication (Public)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | ❌ |
| POST | `/api/auth/login` | Login user | ❌ |

### Products

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/products` | Get all products | ❌ |
| GET | `/api/products/{id}` | Get product by ID | ❌ |
| GET | `/api/products/category/{category}` | Get products by category | ❌ |
| POST | `/api/products` | Create product | ✅ Admin |
| PUT | `/api/products/{id}` | Update product | ✅ Admin |
| DELETE | `/api/products/{id}` | Delete product | ✅ Admin |

### Orders

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/orders` | Create order | ✅ Customer |
| GET | `/api/orders` | Get user's orders | ✅ Customer |
| GET | `/api/orders/{id}` | Get order by ID | ✅ Customer |
| PUT | `/api/orders/{id}/status` | Update order status | ✅ Admin |

### Admin

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/admin/users` | Get all users | ✅ Admin |
| GET | `/api/admin/orders` | Get all orders | ✅ Admin |
| PUT | `/api/admin/users/{id}/role` | Update user role | ✅ Admin |

### Example Request

```bash
# Register a new user
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123",
    "role": "CUSTOMER"
  }'

# Login
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123"
  }'

# Use token to access protected endpoint
curl -X GET http://localhost:8081/api/orders \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
```

## 🗄️ Database Schema

### Main Entities

**Users Table**
- `id` (BIGSERIAL, Primary Key)
- `name` (VARCHAR)
- `email` (VARCHAR, Unique)
- `password` (VARCHAR, Encrypted)
- `role` (VARCHAR) - ADMIN or CUSTOMER

**Products Table**
- `id` (BIGSERIAL, Primary Key)
- `name` (VARCHAR)
- `description` (TEXT)
- `price` (DECIMAL)
- `stock` (INTEGER)
- `category_id` (BIGINT, Foreign Key)

**Orders Table**
- `id` (BIGSERIAL, Primary Key)
- `user_id` (BIGINT, Foreign Key)
- `total_amount` (DECIMAL)
- `status` (VARCHAR)
- `created_at` (TIMESTAMP)

**Order Items Table**
- `id` (BIGSERIAL, Primary Key)
- `order_id` (BIGINT, Foreign Key)
- `product_id` (BIGINT, Foreign Key)
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
DB_NAME=ecommerce_db
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
docker-compose build ecommerce-backend ecommerce-frontend

# Start services
docker-compose up -d ecommerce-backend ecommerce-frontend postgres

# Check status
docker-compose ps

# View logs
docker-compose logs -f ecommerce-backend
```

### Environment Configuration

Create `.env` file for Docker deployment:

```env
POSTGRES_DB=ecommerce_db
POSTGRES_USER=fullstack_user
POSTGRES_PASSWORD=fullstack_pass
JWT_SECRET=your-production-secret-key
```

## 📦 Project Structure

```
e-commerce/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/fullstack/ecommerce/
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
- Caching layer for product catalog (Redis)
- CDN for static assets
- Lazy loading for images
- Connection pooling (HikariCP)

## 📈 Future Enhancements

- [ ] Payment gateway integration (Stripe/PayPal)
- [ ] Product reviews and ratings
- [ ] Wishlist functionality
- [ ] Email notifications
- [ ] Advanced search with Elasticsearch
- [ ] Recommendation engine
- [ ] Analytics dashboard
- [ ] Multi-currency support
- [ ] Inventory alerts

## 🤝 Contributing

This is a portfolio project showcasing professional development skills. Feedback and suggestions are welcome!

## 📄 License

MIT License - see root LICENSE file for details.

---

**Part of Full-Stack Java Projects Portfolio** | [View All Projects](../README.md)
