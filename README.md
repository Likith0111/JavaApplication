# 🚀 Full-Stack Java Enterprise Applications Portfolio

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://reactjs.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> A comprehensive collection of production-ready, enterprise-grade full-stack applications demonstrating modern software architecture, clean code principles, and industry best practices.

## 📋 Table of Contents

- [Overview](#overview)
- [Projects](#projects)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Features](#features)
- [Quick Start](#quick-start)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)

## 🎯 Overview

This repository showcases **four enterprise-level full-stack applications** built with modern technologies and following industry best practices. Each application demonstrates:

- **Clean Architecture** with separation of concerns
- **RESTful API Design** with proper HTTP semantics
- **JWT-based Authentication** for secure access control
- **Role-based Authorization** for granular permissions
- **Docker Containerization** for consistent deployment
- **Comprehensive Testing** with unit and integration tests
- **Professional Documentation** for easy onboarding

## 📦 Projects

### 1. 🛒 E-Commerce Platform
**Port:** 3001 (Frontend) | 8081 (Backend)

A fully-featured online shopping platform with product management, shopping cart, and order processing.

**Key Features:**
- Product catalog with search and filtering
- Shopping cart with real-time updates
- Order management and tracking
- Admin dashboard for inventory control
- Customer role management (Admin/Customer)

**Use Cases:** B2C retail, marketplace platforms, inventory management

[📖 Detailed Documentation](./e-commerce/README.md)

---

### 2. 🍔 Food Ordering System
**Port:** 3002 (Frontend) | 8082 (Backend)

A restaurant ordering platform enabling customers to browse menus, place orders, and track delivery.

**Key Features:**
- Restaurant and menu management
- Real-time order tracking
- Multi-restaurant support
- Order status workflow
- Admin panel for restaurant owners

**Use Cases:** Food delivery apps, restaurant chains, cloud kitchens

[📖 Detailed Documentation](./food-ordering/README.md)

---

### 3. 🎫 Event Booking Platform
**Port:** 3003 (Frontend) | 8083 (Backend)

An event management system for creating, discovering, and booking events with seat management.

**Key Features:**
- Event creation and management
- Seat availability tracking
- Booking confirmation system
- Event search and filtering
- Automated booking ID generation

**Use Cases:** Concert ticketing, conference management, venue booking

[📖 Detailed Documentation](./event-booking/README.md)

---

### 4. 💼 Job Portal
**Port:** 3004 (Frontend) | 8084 (Backend)

A professional job board connecting employers with job seekers, featuring application tracking.

**Key Features:**
- Job posting and management
- Application submission and tracking
- Employer dashboard
- Job seeker profiles
- Application status workflow

**Use Cases:** Recruitment platforms, corporate job boards, staffing agencies

[📖 Detailed Documentation](./job-portal/README.md)

---

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.4.5
- **Language:** Java 17 (LTS)
- **Security:** Spring Security with JWT
- **Database:** PostgreSQL 16
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven 3.9+
- **Testing:** JUnit 5, Mockito, Spring Boot Test
- **Validation:** Bean Validation (JSR-380)
- **Logging:** SLF4J with Logback

### Frontend
- **Framework:** React 18
- **Routing:** React Router 6
- **HTTP Client:** Axios
- **Build Tool:** Vite / Create React App
- **Styling:** CSS3 with Modern Layout Techniques
- **State Management:** React Context API

### DevOps & Infrastructure
- **Containerization:** Docker with multi-stage builds
- **Orchestration:** Docker Compose
- **Web Server:** Nginx (for serving React apps)
- **Version Control:** Git
- **CI/CD Ready:** GitHub Actions compatible

### Database
- **Production:** PostgreSQL 16
- **Testing:** H2 (in-memory)
- **Connection Pooling:** HikariCP
- **Migrations:** Liquibase/Flyway ready

## 🏗️ Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Client Layer (Browser)                   │
│          React SPAs with Axios HTTP Client                  │
└────────────────────┬────────────────────────────────────────┘
                     │ HTTP/REST
┌────────────────────┴────────────────────────────────────────┐
│                   API Gateway Layer                          │
│              Nginx (Load Balancing Ready)                    │
└────────────────────┬────────────────────────────────────────┘
                     │
        ┌────────────┴────────────┬─────────────┐
        │                         │             │
┌───────▼────────┐  ┌────────────▼──────┐  ┌──▼────────────┐
│  Spring Boot   │  │   Spring Boot     │  │ Spring Boot   │
│  REST API      │  │   REST API        │  │   REST API    │
│  (Service 1)   │  │   (Service 2)     │  │  (Service N)  │
└───────┬────────┘  └────────────┬──────┘  └──┬────────────┘
        │                         │             │
        └────────────┬────────────┴─────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                PostgreSQL Database                           │
│         (Containerized with Docker)                         │
└──────────────────────────────────────────────────────────────┘
```

### Application Architecture (Each Service)

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│  Controllers (REST Endpoints) + Exception Handlers          │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                    Service Layer                             │
│  Business Logic + Transaction Management                     │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                  Persistence Layer                           │
│  JPA Repositories + Entity Models                           │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                    Database Layer                            │
│              PostgreSQL / H2 (Test)                         │
└──────────────────────────────────────────────────────────────┘
```

### Security Flow

```
Client Request
     │
     ▼
JWT Token Validation ──► Token Invalid ──► 401 Unauthorized
     │
     │ Token Valid
     ▼
Authorization Check ──► Insufficient Rights ──► 403 Forbidden
     │
     │ Authorized
     ▼
Process Request
     │
     ▼
Return Response
```

## ✨ Features

### Security Features
- ✅ JWT-based stateless authentication
- ✅ Role-based access control (RBAC)
- ✅ Password encryption with BCrypt
- ✅ CORS configuration for cross-origin requests
- ✅ SQL injection prevention (JPA/Hibernate)
- ✅ XSS protection headers

### Code Quality
- ✅ Clean Architecture principles
- ✅ SOLID design principles
- ✅ DRY (Don't Repeat Yourself)
- ✅ Comprehensive error handling
- ✅ Input validation with Bean Validation
- ✅ Professional logging

### Database Features
- ✅ JPA/Hibernate ORM
- ✅ Database migrations ready
- ✅ Connection pooling (HikariCP)
- ✅ Transaction management
- ✅ Cascade operations

### Testing
- ✅ Unit tests for services
- ✅ Integration tests for controllers
- ✅ H2 in-memory database for testing
- ✅ Mockito for mocking dependencies
- ✅ Test coverage for critical paths

### DevOps
- ✅ Multi-stage Dockerfiles for optimization
- ✅ Docker Compose for orchestration
- ✅ Environment-specific configurations
- ✅ Health check endpoints
- ✅ Graceful shutdown support

## 🚀 Quick Start

### Prerequisites

- **Java 17+** (JDK)
- **Maven 3.9+**
- **Node.js 18+** with npm
- **Docker & Docker Compose** (for containerized deployment)
- **PostgreSQL 16** (for local development)

### Option 1: Docker Deployment (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd fullstack-java-projects

# Build all services
docker-compose build

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Option 2: Local Development

```bash
# 1. Start PostgreSQL
docker run -d -p 5432:5432 \
  -e POSTGRES_DB=fullstack_db \
  -e POSTGRES_USER=fullstack_user \
  -e POSTGRES_PASSWORD=fullstack_pass \
  postgres:16-alpine

# 2. Run each backend (in separate terminals)
cd e-commerce/backend && mvn spring-boot:run
cd food-ordering/backend && mvn spring-boot:run
cd event-booking/backend && mvn spring-boot:run
cd job-portal/backend && mvn spring-boot:run

# 3. Run each frontend (in separate terminals)
cd e-commerce/frontend && npm install && npm start
cd food-ordering/frontend && npm install && npm start
cd event-booking/frontend && npm install && npm start
cd job-portal/frontend && npm install && npm start
```

### Access Applications

| Application | Frontend | Backend API |
|------------|----------|-------------|
| E-Commerce | http://localhost:3001 | http://localhost:8081 |
| Food Ordering | http://localhost:3002 | http://localhost:8082 |
| Event Booking | http://localhost:3003 | http://localhost:8083 |
| Job Portal | http://localhost:3004 | http://localhost:8084 |

## 📚 API Documentation

### Authentication Endpoints

All applications share common authentication patterns:

#### Register
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "role": "USER"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePass123"
}

Response:
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "id": 1,
  "email": "john@example.com",
  "name": "John Doe",
  "role": "USER"
}
```

### Using JWT Token

```http
GET /api/protected-endpoint
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

For detailed API documentation of each service, see individual project READMEs.

## 🧪 Testing

### Run All Tests

```bash
# Backend tests (all projects)
mvn test

# With coverage report
mvn test jacoco:report

# Frontend tests
npm test

# E2E tests
npm run test:e2e
```

### Test Coverage

- **Unit Tests:** Service layer business logic
- **Integration Tests:** Controller layer with MockMvc
- **Repository Tests:** Data access layer
- **Security Tests:** Authentication and authorization

## 🐳 Docker Details

### Multi-Stage Build Strategy

Each Dockerfile uses multi-stage builds to minimize image size:

1. **Build Stage:** Compile and package application
2. **Runtime Stage:** Run application with minimal dependencies

### Image Sizes (Approximate)

- Backend images: ~250-300MB (using Alpine JRE)
- Frontend images: ~25-30MB (using Nginx Alpine)

### Docker Compose Services

```yaml
services:
  - postgres: Database server
  - *-backend: 4 Spring Boot applications
  - *-frontend: 4 React applications (Nginx)
```

## 📖 Project Documentation

Each project includes comprehensive documentation:

- **README.md:** Project overview and setup
- **API_DOCS.md:** Complete API reference
- **ARCHITECTURE.md:** Design decisions and patterns
- **SETUP.md:** Detailed setup instructions
- **CONTRIBUTING.md:** Contribution guidelines

## 🤝 Contributing

This is a portfolio project, but feedback and suggestions are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**[Your Name]**

- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

## 🌟 Acknowledgments

- Spring Boot Team for excellent framework
- React Team for powerful UI library
- All open-source contributors

---

**Made with ❤️ for learning and professional growth**

*This repository demonstrates proficiency in full-stack development, microservices architecture, and modern DevOps practices.*
